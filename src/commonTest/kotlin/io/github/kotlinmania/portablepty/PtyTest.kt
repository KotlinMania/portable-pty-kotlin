// port-lint: tests lib.rs
package io.github.kotlinmania.portablepty

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PtyTest {
    @Test
    fun testPtySizeDefault() {
        val size = PtySize.DEFAULT
        assertEquals(24, size.rows)
        assertEquals(80, size.cols)
        assertEquals(0, size.pixelWidth)
        assertEquals(0, size.pixelHeight)
    }

    @Test
    fun testExitStatusSuccess() {
        val status = ExitStatus.withExitCode(0)
        assertTrue(status.success())
        assertEquals(0, status.exitCode())
        assertNull(status.signal)
        assertEquals("Success", status.toString())
    }

    @Test
    fun testExitStatusFailure() {
        val status = ExitStatus.withExitCode(127)
        assertFalse(status.success())
        assertEquals(127, status.exitCode())
        assertNull(status.signal)
        assertEquals("Exited with code 127", status.toString())
    }

    @Test
    fun testExitStatusSignal() {
        val status = ExitStatus.withSignal("SIGTERM")
        assertFalse(status.success())
        assertEquals(1, status.exitCode())
        assertEquals("SIGTERM", status.signal)
        assertEquals("Terminated by SIGTERM", status.toString())
    }

    @Test
    fun testCommandBuilderBasics() {
        val cmd = CommandBuilder.new("ls")
            .arg("-la")
            .arg("/tmp")
            .env("FOO", "BAR")
            .cwd("/var")

        assertEquals(listOf("ls", "-la", "/tmp"), cmd.getArgv())
        assertEquals("BAR", cmd.getEnv("FOO"))
        assertEquals("/var", cmd.getCwd())
        assertTrue(cmd.getControllingTty())
        assertEquals("ls -la /tmp", cmd.asUnixCommandLine())
    }

    @Test
    fun testCommandBuilderQuoting() {
        val cmd = CommandBuilder.new("echo")
            .arg("hello world")
            .arg("quote\"test")

        assertEquals("echo \"hello world\" \"quote\\\"test\"", cmd.asUnixCommandLine())
    }

    @Test
    fun testCommandBuilderDefaultProg() {
        val cmd = CommandBuilder.newDefaultProg()
        assertTrue(cmd.isDefaultProg())
        assertEquals(emptyList(), cmd.getArgv())
    }
}
