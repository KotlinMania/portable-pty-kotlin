// port-lint: tests portable-pty/src/win/mod.rs
package io.github.kotlinmania.portablepty

import io.github.kotlinmania.portablepty.win.ConPtySystem
import io.github.kotlinmania.portablepty.win.Coord
import io.github.kotlinmania.portablepty.win.ProcThreadAttributeList
import io.github.kotlinmania.portablepty.win.PsuedoCon
import io.github.kotlinmania.portablepty.win.WinChild
import io.github.kotlinmania.portablepty.win.loadConpty
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class WinTest {
    @Test
    fun testWinChildAndKiller() {
        val child = WinChild()
        assertEquals(1000L, child.processId())
        assertEquals(1000L, child.asRawHandle())
        val killer = child.cloneKiller()
        killer.kill()
        val waitStatus = child.wait()
        assertTrue(waitStatus.success())
    }

    @Test
    fun testPsuedoConAndAttributeList() {
        assertTrue(loadConpty())
        val attrs = ProcThreadAttributeList.withCapacity(1)
        attrs.setPty(1L)
        attrs.drop()

        val pcon = PsuedoCon.new(Coord(80, 24))
        pcon.resize(Coord(120, 30))
        val child = pcon.spawnCommand(CommandBuilder.new("cmd.exe"))
        assertEquals(1000L, child.processId())
        pcon.drop()
    }

    @Test
    fun testConPtySystem() {
        val sys = ConPtySystem()
        val pair = sys.openpty(PtySize(rows = 30, cols = 100))
        assertEquals(30, pair.master.getSize().rows)
        assertEquals(100, pair.master.getSize().cols)

        val reader = pair.master.tryCloneReader()
        assertNotNull(reader)
        val writer = pair.master.takeWriter()
        assertNotNull(writer)

        val child = pair.slave.spawnCommand(CommandBuilder.new("cmd.exe"))
        val status = child.wait()
        assertTrue(status.success())
    }
}
