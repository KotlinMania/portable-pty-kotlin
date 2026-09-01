// port-lint: tests cmdbuilder.rs
package io.github.kotlinmania.portablepty

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CommandBuilderTest {
    @Test
    fun testCwdRelative() {
        assertTrue(isCwdRelativePath("."))
        assertTrue(isCwdRelativePath("./foo"))
        assertTrue(isCwdRelativePath("../foo"))
        assertFalse(isCwdRelativePath("foo"))
        assertFalse(isCwdRelativePath("/foo"))
    }

    @Test
    fun testEnv() {
        val cmd = CommandBuilder.new("dummy")
        cmd.env("foo key", "foo value")
        cmd.env("bar key", "bar value")

        val iterated = cmd.iterExtraEnv()
        assertEquals(listOf("foo key" to "foo value", "bar key" to "bar value"), iterated)

        cmd.envRemove("foo key")
        assertEquals(listOf("bar key" to "bar value"), cmd.iterExtraEnv())

        cmd.envRemove("bar key")
        assertEquals(emptyList(), cmd.iterExtraEnv())

        cmd.env("test", "val")
        cmd.envClear()
        assertEquals(emptyList(), cmd.iterExtraEnv())
    }

    @Test
    fun testEnvCaseInsensitiveOverride() {
        val cmd = CommandBuilder.new("dummy")
        cmd.env("Cargo_Pkg_Authors", "Not Wez")
        assertEquals("Not Wez", cmd.getEnv("cargo_pkg_authors"))

        cmd.envRemove("cARGO_pKG_aUTHORS")
        assertNull(cmd.getEnv("CARGO_PKG_AUTHORS"))
    }

    @Test
    fun testArgvAndCmdline() {
        val cmd = CommandBuilder.new("myprog")
        cmd.arg("arg1").args(listOf("arg2", "hello world"))
        assertEquals(listOf("myprog", "arg1", "arg2", "hello world"), cmd.getArgv())

        val (exe, cmdline) = cmd.cmdline()
        assertTrue(exe.isNotEmpty())
        assertTrue(cmdline.isNotEmpty())
    }
}
