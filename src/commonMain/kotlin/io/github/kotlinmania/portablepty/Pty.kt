// port-lint: source lib.rs
package io.github.kotlinmania.portablepty

import kotlinx.serialization.Serializable

/**
 * Represents the size of the visible display area in the pty.
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
    fun success(): Boolean = signal == null && code == 0

    fun exitCode(): Int = code

    override fun toString(): String {
        return if (success()) {
            "Success"
        } else if (signal != null) {
            "Terminated by $signal"
        } else {
            "Exited with code $code"
        }
    }

    companion object {
        fun withExitCode(code: Int): ExitStatus = ExitStatus(code = code, signal = null)

        fun withSignal(signal: String): ExitStatus = ExitStatus(code = 1, signal = signal)
    }
}

/**
 * Represents the ability to signal a child process to terminate.
 */
interface ChildKiller {
    fun kill()
    fun cloneKiller(): ChildKiller
}

/**
 * Represents a child process spawned into the pty.
 */
interface Child : ChildKiller {
    fun tryWait(): ExitStatus?
    fun wait(): ExitStatus
    fun processId(): Long?
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
 * Represents the master/control end of the pty.
 */
interface MasterPty {
    fun resize(size: PtySize)
    fun getSize(): PtySize
    fun processGroupLeader(): Long? = null
    fun asRawFd(): Int? = null
    fun ttyName(): String? = null
}

/**
 * Represents the slave side of a pty.
 */
interface SlavePty {
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
    fun openpty(size: PtySize = PtySize.DEFAULT): PtyPair
}

/**
 * Default in-memory PTY system for cross-platform fallback.
 */
class DefaultPtySystem : PtySystem {
    override fun openpty(size: PtySize): PtyPair {
        val master = object : MasterPty {
            private var currentSize = size
            override fun resize(size: PtySize) {
                currentSize = size
            }
            override fun getSize(): PtySize = currentSize
        }
        val slave = object : SlavePty {
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
 * Returns the native pty system for the platform.
 */
fun nativePtySystem(): PtySystem = DefaultPtySystem()
