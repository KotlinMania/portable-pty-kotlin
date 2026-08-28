// port-lint: source win/conpty.rs
package io.github.kotlinmania.portablepty.win

import io.github.kotlinmania.portablepty.Child
import io.github.kotlinmania.portablepty.CommandBuilder
import io.github.kotlinmania.portablepty.MasterPty
import io.github.kotlinmania.portablepty.PtyPair
import io.github.kotlinmania.portablepty.PtySize
import io.github.kotlinmania.portablepty.PtySystem
import io.github.kotlinmania.portablepty.Read
import io.github.kotlinmania.portablepty.SlavePty
import io.github.kotlinmania.portablepty.Write

/**
 * Windows ConPTY system implementation.
 */
class ConPtySystem : PtySystem {
    override fun openpty(size: PtySize): PtyPair {
        val coord = Coord(x = size.cols.toShort(), y = size.rows.toShort())
        val con = PsuedoCon.new(coord)
        val inner = Inner(con = con, size = size)
        val master = ConPtyMasterPty(inner = inner)
        val slave = ConPtySlavePty(inner = inner)
        return PtyPair(slave = slave, master = master)
    }
}

/**
 * Internal state holder for ConPTY master and slave.
 */
class Inner(
    val con: PsuedoCon,
    var size: PtySize,
) {
    fun resize(numRows: Int, numCols: Int, pixelWidth: Int, pixelHeight: Int) {
        val coord = Coord(x = numCols.toShort(), y = numRows.toShort())
        con.resize(coord)
        this.size = PtySize(rows = numRows, cols = numCols, pixelWidth = pixelWidth, pixelHeight = pixelHeight)
    }
}

/**
 * Master end of a ConPTY pseudo-console.
 */
class ConPtyMasterPty(
    val inner: Inner,
) : MasterPty {
    override fun resize(size: PtySize) {
        inner.resize(size.rows, size.cols, size.pixelWidth, size.pixelHeight)
    }

    override fun getSize(): PtySize = inner.size

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

/**
 * Slave end of a ConPTY pseudo-console.
 */
class ConPtySlavePty(
    val inner: Inner,
) : SlavePty {
    override fun spawnCommand(cmd: CommandBuilder): Child = inner.con.spawnCommand(cmd)
}
