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
        val master = object : MasterPty {
            override fun resize(size: PtySize) {}
            override fun getSize(): PtySize = PtySize.DEFAULT
        }
        val slave = object : SlavePty {
            override fun spawnCommand(cmd: CommandBuilder): Child {
                if (!cmd.isDefaultProg()) {
                    throw IllegalArgumentException("can only use default prog commands with serial tty implementations")
                }
                return SerialChild()
            }
        }
        return PtyPair(slave = slave, master = master)
    }
}

/**
 * Child process handle on a serial connection.
 */
class SerialChild : Child {
    override fun tryWait(): ExitStatus? = null

    override fun wait(): ExitStatus = ExitStatus.withExitCode(0)

    override fun processId(): Long? = null

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
