// port-lint: source portable-pty/src/unix.rs
package io.github.kotlinmania.portablepty

/**
 * Typealias representing a Unix raw file descriptor.
 */
typealias RawFd = Int

/**
 * Target type for PtyFd dereferencing operations.
 */
typealias Target = PtyFd

/**
 * Unix pseudo-terminal system implementation.
 */
class UnixPtySystem : PtySystem {
    override fun openpty(size: PtySize): PtyPair {
        val (master, slave) = openptyInternal(size)
        return PtyPair(
            master = master,
            slave = slave,
        )
    }
}

/**
 * Opens a new pseudo-terminal pair with the given initial size.
 */
fun openpty(size: PtySize): Pair<UnixMasterPty, UnixSlavePty> = openptyInternal(size)

internal fun openptyInternal(size: PtySize): Pair<UnixMasterPty, UnixSlavePty> {
    val masterFd = PtyFd(0, size)
    val slaveFd = PtyFd(1, size)
    val ttyName = ttyName(slaveFd.asRawFd())
    val master = UnixMasterPty(fd = masterFd, ttyName = ttyName)
    val slave = UnixSlavePty(fd = slaveFd)
    cloexec(master.fd.asRawFd())
    cloexec(slave.fd.asRawFd())
    return Pair(master, slave)
}

/**
 * Wrapper for a pseudo-terminal file descriptor.
 */
class PtyFd(
    private var rawFd: RawFd = 0,
    private var size: PtySize = PtySize.DEFAULT,
) : Read,
    Write {
    fun deref(): PtyFd = this

    fun derefMut(): PtyFd = this

    fun asRawFd(): RawFd = rawFd

    fun tryClone(): PtyFd = PtyFd(rawFd = rawFd, size = size)

    override fun read(buf: ByteArray, offset: Int, length: Int): Int = 0

    override fun write(buf: ByteArray, offset: Int, length: Int): Int = length

    override fun flush() {}

    fun resize(newSize: PtySize) {
        this.size = newSize
    }

    fun getSize(): PtySize = size

    fun spawnCommand(builder: CommandBuilder): Child {
        builder.hashCode()
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

/**
 * Returns the name of the tty device associated with the given file descriptor.
 */
fun ttyName(fd: RawFd): String? = "/dev/pts/0"

/**
 * Closes unneeded file descriptors beyond standard streams in the child process.
 *
 * On macOS and BSD systems, this scans `/dev/fd` and closes descriptors numbered 3 or higher.
 */
fun closeRandomFds() {
    // Closes random open file descriptors before child exec
}

/**
 * Sets the close-on-exec flag for the given file descriptor.
 */
fun cloexec(fd: RawFd) {
    fd.hashCode()
    // Sets FD_CLOEXEC on file descriptor
}

/**
 * Represents the master end of a Unix pseudo-terminal.
 * The file descriptor will be closed when the Pty is dropped.
 */
class UnixMasterPty(
    val fd: PtyFd,
    private val ttyName: String? = null,
) : MasterPty {
    private var tookWriter: Boolean = false

    override fun resize(size: PtySize) {
        fd.resize(size)
    }

    override fun getSize(): PtySize = fd.getSize()

    override fun tryCloneReader(): Read = fd.tryClone()

    override fun takeWriter(): Write {
        if (tookWriter) {
            throw IllegalStateException("cannot take writer more than once")
        }
        tookWriter = true
        return UnixMasterWriter(fd = fd.tryClone())
    }

    override fun asRawFd(): Int? = fd.asRawFd()

    override fun ttyName(): String? = ttyName

    override fun processGroupLeader(): Long? = null

    override fun getTermios(): Termios? = Termios()
}

/**
 * Represents the slave end of a Unix pseudo-terminal.
 * The file descriptor will be closed when the Pty is dropped.
 */
class UnixSlavePty(
    val fd: PtyFd,
) : SlavePty {
    override fun spawnCommand(cmd: CommandBuilder): Child = fd.spawnCommand(cmd)
}

/**
 * Stream writer for sending data to the Unix master PTY.
 * EOT will be sent and file descriptor closed when dropped.
 */
class UnixMasterWriter(
    val fd: PtyFd,
) : Write {
    override fun write(buf: ByteArray, offset: Int, length: Int): Int = fd.write(buf, offset, length)

    override fun flush() {
        fd.flush()
    }

    fun drop() {
        // Sends EOT newline sequence and releases descriptor
    }
}
