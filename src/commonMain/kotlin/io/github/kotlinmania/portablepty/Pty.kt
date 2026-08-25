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
}

/**
 * Represents the master/control end of the pty.
 */
interface MasterPty {
    fun resize(size: PtySize)
    fun getSize(): PtySize
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
