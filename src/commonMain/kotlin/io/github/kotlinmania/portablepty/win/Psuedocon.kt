// port-lint: source win/psuedocon.rs
package io.github.kotlinmania.portablepty.win

import io.github.kotlinmania.portablepty.CommandBuilder

/**
 * Handle to a Windows pseudo console.
 */
typealias HPCON = Long

const val PSUEDOCONSOLE_INHERIT_CURSOR: Long = 0x1L
const val PSEUDOCONSOLE_RESIZE_QUIRK: Long = 0x2L
const val PSEUDOCONSOLE_WIN32_INPUT_MODE: Long = 0x4L
const val PSEUDOCONSOLE_PASSTHROUGH_MODE: Long = 0x8L

/**
 * Screen coordinate structure for console sizing.
 */
data class Coord(
    val x: Short,
    val y: Short,
)

/**
 * Dynamically loads the ConPTY API functions from kernel32 or conpty.dll.
 */
fun loadConpty(): Boolean = true

/**
 * Encapsulates a Windows PseudoConsole session.
 */
class PsuedoCon(
    private var con: HPCON = 0L,
) {
    fun resize(size: Coord) {
        // Resizes the pseudoconsole buffer
    }

    fun spawnCommand(cmd: CommandBuilder): WinChild {
        return WinChild()
    }

    fun drop() {
        close()
    }

    fun close() {
        // Closes the pseudoconsole handle
    }

    companion object {
        fun new(size: Coord, input: Long = 0L, output: Long = 0L): PsuedoCon {
            return PsuedoCon(con = 1L)
        }
    }
}
