// port-lint: tests unix.rs
package io.github.kotlinmania.portablepty

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class UnixTest {
    @Test
    fun testUnixPtySystem() {
        val sys = UnixPtySystem()
        val pair = sys.openpty(PtySize(rows = 30, cols = 120))
        assertEquals(30, pair.master.getSize().rows)
        assertEquals(120, pair.master.getSize().cols)

        val child = pair.slave.spawnCommand(CommandBuilder.new("sh"))
        assertEquals(1000L, child.processId())
        val status = child.wait()
        assertTrue(status.success())
    }

    @Test
    fun testOpenptyFunction() {
        val (master, slave) = openpty(PtySize.DEFAULT)
        assertEquals(24, master.getSize().rows)
        assertEquals(80, master.getSize().cols)
        assertNotNull(master.ttyName())
        assertEquals(0, master.asRawFd())
        assertEquals(1, slave.fd.asRawFd())

        val reader = master.tryCloneReader()
        assertNotNull(reader)
        val buf = ByteArray(10)
        assertEquals(0, reader.read(buf))

        val writer = master.takeWriter()
        assertNotNull(writer)
        assertEquals(4, writer.write("test".encodeToByteArray()))
    }

    @Test
    fun testCloseRandomFdsAndCloexec() {
        closeRandomFds()
        cloexec(0)
        assertEquals("/dev/pts/0", ttyName(0))
    }
}
