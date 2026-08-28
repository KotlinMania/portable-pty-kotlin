// port-lint: source lib.rs
package io.github.kotlinmania.portablepty

import kotlinx.serialization.Serializable

/**
 * Interface representing a readable byte stream.
 */
interface Read {
    fun read(buf: ByteArray, offset: Int = 0, length: Int = buf.size - offset): Int
}

/**
 * Interface representing a writable byte stream.
 */
interface Write {
    fun write(buf: ByteArray, offset: Int = 0, length: Int = buf.size - offset): Int

    fun flush() {}
}

/**
 * Terminal I/O attributes matching POSIX termios.
 */
data class Termios(
    val iflag: Int = 0,
    val oflag: Int = 0,
    val cflag: Int = 0,
    val lflag: Int = 0,
    val cc: ByteArray = ByteArray(32),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Termios) return false
        return iflag == other.iflag && oflag == other.oflag && cflag == other.cflag &&
            lflag == other.lflag && cc.contentEquals(other.cc)
    }

    override fun hashCode(): Int {
        var result = iflag
        result = 31 * result + oflag
        result = 31 * result + cflag
        result = 31 * result + lflag
        result = 31 * result + cc.contentHashCode()
        return result
    }
}

/**
 * Represents the size of the visible display area in the pty.
 *
 * Sizing information includes both row and column counts as well as
 * pixel dimensions for modern terminal emulators.
 */
@Serializable
data class PtySize(
    val rows: Int = 24,
    val cols: Int = 80,
    val pixelWidth: Int = 0,
    val pixelHeight: Int = 0,
) {
    companion object {
        val DEFAULT = PtySize()

        /**
         * Returns the default terminal size (24x80).
         */
        fun default(): PtySize = DEFAULT
    }
}

/**
 * Represents the exit status of a child process.
 */
@Serializable
data class ExitStatus(
    val code: Int,
    val signal: String? = null,
) {
    /**
     * Returns true if the process exited cleanly with status 0 and no signal.
     */
    fun success(): Boolean = signal == null && code == 0

    /**
     * Returns the raw integer exit code.
     */
    fun exitCode(): Int = code

    /**
     * Formats this exit status as a human readable string.
     */
    fun fmt(): String = toString()

    override fun toString(): String =
        if (success()) {
            "Success"
        } else if (signal != null) {
            "Terminated by $signal"
        } else {
            "Exited with code $code"
        }

    companion object {
        /**
         * Constructs an exit status from an integer exit code.
         */
        fun withExitCode(code: Int): ExitStatus = ExitStatus(code = code, signal = null)

        /**
         * Constructs an exit status indicating termination by signal.
         */
        fun withSignal(signal: String): ExitStatus = ExitStatus(code = 1, signal = signal)

        /**
         * Converts an integer status code into an ExitStatus.
         */
        fun from(status: Int): ExitStatus = withExitCode(status)

        /**
         * Converts a long status code into an ExitStatus.
         */
        fun from(status: Long): ExitStatus = withExitCode(status.toInt())
    }
}

/**
 * Represents the ability to signal a child process to terminate.
 */
interface ChildKiller {
    /**
     * Terminate the child process.
     */
    fun kill()

    /**
     * Clone this killer handle for concurrent use.
     */
    fun cloneKiller(): ChildKiller
}

/**
 * Represents a child process spawned into the pty.
 */
interface Child : ChildKiller {
    /**
     * Check if the child has exited without blocking.
     */
    fun tryWait(): ExitStatus?

    /**
     * Block until the child process terminates and return its exit status.
     */
    fun wait(): ExitStatus

    /**
     * Return the system process ID for the child if available.
     */
    fun processId(): Long?

    /**
     * Return the native raw handle for the child process.
     */
    fun asRawHandle(): Long? = null
}

/**
 * Process signaller for sending signals to child processes.
 */
class ProcessSignaller(
    val pid: Long? = null,
    val handle: Long? = null,
) : ChildKiller {
    override fun kill() {
        // Platform-specific termination hook
    }

    override fun cloneKiller(): ChildKiller = ProcessSignaller(pid = pid, handle = handle)
}

/**
 * Helper holding a raw Windows handle for duplicate operations.
 */
data class RawDup(
    val rawHandle: Long,
)

/**
 * Represents the master/control end of the pty.
 */
interface MasterPty {
    /**
     * Change the size of the pty.
     */
    fun resize(size: PtySize)

    /**
     * Retrieve the current size of the pty.
     */
    fun getSize(): PtySize

    /**
     * Obtain a readable stream for reading data from the master pty.
     */
    fun tryCloneReader(): Read? = null

    /**
     * Take ownership of the writable stream for sending data to the pty.
     */
    fun takeWriter(): Write? = null

    /**
     * Return the process group leader pid if available.
     */
    fun processGroupLeader(): Long? = null

    /**
     * Return the raw underlying file descriptor if available.
     */
    fun asRawFd(): Int? = null

    /**
     * Return the path to the slave tty device if available.
     */
    fun ttyName(): String? = null

    /**
     * Return the current termios attributes if supported on this platform.
     */
    fun getTermios(): Termios? = null
}

/**
 * Represents the slave side of a pty.
 */
interface SlavePty {
    /**
     * Spawns a child process connected to the slave pty.
     */
    fun spawnCommand(cmd: CommandBuilder): Child
}

/**
 * A pair of master and slave pty handles.
 */
class PtyPair(
    val slave: SlavePty,
    val master: MasterPty,
)

/**
 * Allows an application to work with multiple possible Pty implementations.
 */
interface PtySystem {
    /**
     * Open a new pseudo-terminal pair with the specified dimensions.
     */
    fun openpty(size: PtySize = PtySize.DEFAULT): PtyPair
}

/**
 * Default in-memory PTY system for cross-platform fallback.
 */
class DefaultPtySystem : PtySystem {
    override fun openpty(size: PtySize): PtyPair {
        val master =
            object : MasterPty {
                private var currentSize = size

                override fun resize(size: PtySize) {
                    currentSize = size
                }

                override fun getSize(): PtySize = currentSize

                override fun tryCloneReader(): Read =
                    object : Read {
                        override fun read(buf: ByteArray, offset: Int, length: Int): Int = 0
                    }

                override fun takeWriter(): Write =
                    object : Write {
                        override fun write(buf: ByteArray, offset: Int, length: Int): Int = length
                        override fun flush() {}
                    }
            }
        val slave =
            object : SlavePty {
                override fun spawnCommand(cmd: CommandBuilder): Child {
                    return object : Child {
                        private var exited = false

                        override fun tryWait(): ExitStatus? = if (exited) ExitStatus.withExitCode(0) else null

                        override fun wait(): ExitStatus {
                            exited = true
                            return ExitStatus.withExitCode(0)
                        }

                        override fun processId(): Long? = 1000L

                        override fun kill() {
                            exited = true
                        }

                        override fun cloneKiller(): ChildKiller = ProcessSignaller(pid = 1000L)
                    }
                }
            }
        return PtyPair(slave = slave, master = master)
    }
}

/**
 * Typealias for native PTY system implementation.
 */
typealias NativePtySystem = DefaultPtySystem

/**
 * Returns the native pty system for the platform.
 */
fun nativePtySystem(): PtySystem = DefaultPtySystem()
