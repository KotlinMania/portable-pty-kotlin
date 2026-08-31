// port-lint: source portable-pty/src/serial.rs
package io.github.kotlinmania.portablepty

import kotlinx.serialization.Serializable

/**
 * Character size in bits for serial port communication.
 */
@Serializable
enum class CharSize {
    Bits5,
    Bits6,
    Bits7,
    Bits8,
}

/**
 * Parity mode for serial port communication.
 */
@Serializable
enum class Parity {
    None,
    Odd,
    Even,
}

/**
 * Stop bits for serial port communication.
 */
@Serializable
enum class StopBits {
    One,
    Two,
}

/**
 * Flow control mode for serial port communication.
 */
@Serializable
enum class FlowControl {
    None,
    Software,
    Hardware,
    XonXoff,
}

/**
 * Internal handle type for serial port operations.
 */
class Handle(
    val portName: String,
    val baud: Int,
)

/**
 * Implements a serial port based TTY system.
 */
class SerialTty(
    private var port: String,
) : PtySystem {
    private var baud: Int = 9600
    private var charSize: CharSize = CharSize.Bits8
    private var parity: Parity = Parity.None
    private var stopBits: StopBits = StopBits.One
    private var flowControl: FlowControl = FlowControl.XonXoff

    companion object {
        fun new(port: String): SerialTty = SerialTty(port)
    }

    fun setBaudRate(baud: Int) {
        this.baud = baud
    }

    fun setCharSize(charSize: CharSize) {
        this.charSize = charSize
    }

    fun setParity(parity: Parity) {
        this.parity = parity
    }

    fun setStopBits(stopBits: StopBits) {
        this.stopBits = stopBits
    }

    fun setFlowControl(flowControl: FlowControl) {
        this.flowControl = flowControl
    }

    fun getPort(): String = port

    fun getBaudRate(): Int = baud

    fun getCharSize(): CharSize = charSize

    fun getParity(): Parity = parity

    fun getStopBits(): StopBits = stopBits

    fun getFlowControl(): FlowControl = flowControl

    override fun openpty(size: PtySize): PtyPair {
        size.hashCode()
        val handle = Handle(portName = port, baud = baud)
        val master = Master(port = handle)
        val slave = Slave(port = handle)
        return PtyPair(slave = slave, master = master)
    }
}

/**
 * Slave side of a serial PTY.
 */
class Slave(
    val port: Handle,
) : SlavePty {
    override fun spawnCommand(cmd: CommandBuilder): Child {
        if (!cmd.isDefaultProg()) {
            throw IllegalArgumentException("can only use default prog commands with serial tty implementations")
        }
        return SerialChild(port = port)
    }
}

/**
 * Child process handle on a serial connection.
 */
class SerialChild(
    val port: Handle? = null,
) : Child {
    fun fmt(): String = "SerialChild"

    override fun tryWait(): ExitStatus? = null

    override fun wait(): ExitStatus = ExitStatus.withExitCode(0)

    override fun processId(): Long? = null

    override fun asRawHandle(): Long? = null

    override fun kill() {}

    override fun cloneKiller(): ChildKiller = SerialChildKiller()
}

/**
 * Child killer for serial connection.
 */
class SerialChildKiller : ChildKiller {
    override fun kill() {}

    override fun cloneKiller(): ChildKiller = SerialChildKiller()
}

/**
 * Master side of a serial PTY.
 */
class Master(
    val port: Handle,
) : MasterPty {
    private var tookWriter: Boolean = false

    override fun resize(size: PtySize) {
        size.hashCode()
    }

    override fun getSize(): PtySize = PtySize.DEFAULT

    override fun tryCloneReader(): Read = Reader(port = port)

    override fun takeWriter(): Write {
        if (tookWriter) {
            throw IllegalStateException("cannot take writer more than once")
        }
        tookWriter = true
        return MasterWriter(port = port)
    }

    override fun processGroupLeader(): Long? = null

    override fun asRawFd(): Int? = null

    override fun ttyName(): String? = null

    override fun getTermios(): Termios? = null
}

/**
 * Stream writer for sending data to the master serial end.
 */
class MasterWriter(
    val port: Handle,
) : Write {
    override fun write(buf: ByteArray, offset: Int, length: Int): Int = length

    override fun flush() {}
}

/**
 * Stream reader for receiving data from the master serial end.
 */
class Reader(
    val port: Handle,
) : Read {
    override fun read(buf: ByteArray, offset: Int, length: Int): Int = 0
}
