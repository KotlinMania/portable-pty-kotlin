// port-lint: source cmdbuilder.rs
package io.github.kotlinmania.portablepty

import kotlinx.serialization.Serializable

@Serializable
internal data class EnvEntry(
    val isFromBaseEnv: Boolean,
    val preferredKey: String,
    val value: String,
) {
    companion object {
        fun mapKey(key: String): String = key.lowercase()
    }
}

/**
 * Prepares a command to be spawned into a pty.
 */
@Serializable
class CommandBuilder private constructor(
    private val args: MutableList<String>,
    private val envs: MutableMap<String, EnvEntry>,
    private var cwd: String?,
    private var umaskValue: Int?,
    private var controllingTty: Boolean,
) {
    constructor(program: String) : this(
        args = mutableListOf(program),
        envs = mutableMapOf(),
        cwd = null,
        umaskValue = null,
        controllingTty = true,
    )

    fun isDefaultProg(): Boolean = args.isEmpty()

    fun arg(arg: String): CommandBuilder {
        if (isDefaultProg()) {
            throw IllegalStateException("attempted to add args to a default_prog builder")
        }
        args.add(arg)
        return this
    }

    fun args(args: Iterable<String>): CommandBuilder {
        for (a in args) {
            arg(a)
        }
        return this
    }

    fun getArgv(): List<String> = args.toList()

    internal fun getArgvMut(): MutableList<String> = args

    fun setControllingTty(controllingTty: Boolean) {
        this.controllingTty = controllingTty
    }

    fun getControllingTty(): Boolean = controllingTty

    fun umask(mask: Int?): CommandBuilder {
        this.umaskValue = mask
        return this
    }

    fun getUmask(): Int? = umaskValue

    fun env(key: String, value: String): CommandBuilder {
        envs[EnvEntry.mapKey(key)] = EnvEntry(
            isFromBaseEnv = false,
            preferredKey = key,
            value = value,
        )
        return this
    }

    fun envRemove(key: String): CommandBuilder {
        envs.remove(EnvEntry.mapKey(key))
        return this
    }

    fun envClear(): CommandBuilder {
        envs.clear()
        return this
    }

    fun getEnv(key: String): String? = envs[EnvEntry.mapKey(key)]?.value

    fun cwd(dir: String): CommandBuilder {
        cwd = dir
        return this
    }

    fun clearCwd(): CommandBuilder {
        cwd = null
        return this
    }

    fun getCwd(): String? = cwd

    fun iterExtraEnv(): List<Pair<String, String>> = iterExtraEnvAsStr()

    fun iterExtraEnvAsStr(): List<Pair<String, String>> {
        return envs.values
            .filter { !it.isFromBaseEnv }
            .map { it.preferredKey to it.value }
    }

    fun iterFullEnv(): List<Pair<String, String>> = iterFullEnvAsStr()

    fun iterFullEnvAsStr(): List<Pair<String, String>> {
        return envs.values.map { it.preferredKey to it.value }
    }

    fun getShell(): String {
        val shell = getEnv("SHELL") ?: getEnv("ComSpec")
        return shell ?: "/bin/sh"
    }

    fun getHomeDir(): String {
        return getEnv("HOME") ?: getEnv("USERPROFILE") ?: "/"
    }

    fun asUnixCommandLine(): String {
        val quoted = args.map { arg ->
            if (arg.isEmpty() || arg.any { it.isWhitespace() || it == '"' || it == '\'' || it == '\\' }) {
                "\"" + arg.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
            } else {
                arg
            }
        }
        return quoted.joinToString(" ")
    }

    fun searchPath(exe: String, cwd: String = "."): String {
        if (isCwdRelativePath(exe)) {
            return if (cwd == ".") exe else "$cwd/$exe"
        }
        val pathVar = getEnv("PATH")
        if (pathVar != null) {
            val paths = pathVar.split(':', ';')
            for (p in paths) {
                if (p.isNotEmpty()) {
                    return "$p/$exe"
                }
            }
        }
        return exe
    }

    fun currentDirectory(): List<UShort>? {
        val dir = cwd ?: getEnv("USERPROFILE") ?: getEnv("HOME") ?: return null
        return dir.map { it.code.toUShort() } + listOf(0u.toUShort())
    }

    fun environmentBlock(): List<UShort> {
        val block = mutableListOf<UShort>()
        for (entry in envs.values) {
            for (ch in entry.preferredKey) {
                block.add(ch.code.toUShort())
            }
            block.add('='.code.toUShort())
            for (ch in entry.value) {
                block.add(ch.code.toUShort())
            }
            block.add(0u.toUShort())
        }
        block.add(0u.toUShort())
        return block
    }

    fun cmdline(): Pair<List<UShort>, List<UShort>> {
        val exe = if (isDefaultProg()) {
            getEnv("ComSpec") ?: "cmd.exe"
        } else {
            searchPath(args[0])
        }

        val cmdlineChars = mutableListOf<UShort>()
        appendQuoted(exe, cmdlineChars)

        val exeChars = exe.map { it.code.toUShort() }.toMutableList().apply { add(0u.toUShort()) }

        for (arg in args.drop(1)) {
            cmdlineChars.add(' '.code.toUShort())
            appendQuoted(arg, cmdlineChars)
        }
        cmdlineChars.add(0u.toUShort())

        return Pair(exeChars, cmdlineChars)
    }

    companion object {
        fun new(program: String): CommandBuilder = CommandBuilder(program)

        fun fromArgv(args: List<String>): CommandBuilder = CommandBuilder(
            args = args.toMutableList(),
            envs = mutableMapOf(),
            cwd = null,
            umaskValue = null,
            controllingTty = true,
        )

        fun newDefaultProg(): CommandBuilder = CommandBuilder(
            args = mutableListOf(),
            envs = mutableMapOf(),
            cwd = null,
            umaskValue = null,
            controllingTty = true,
        )

        fun appendQuoted(arg: String, cmdline: MutableList<UShort>) {
            if (arg.isNotEmpty() && !arg.any { it == ' ' || it == '\t' || it == '\n' || it == '"' || it == '\\' }) {
                for (ch in arg) {
                    cmdline.add(ch.code.toUShort())
                }
                return
            }
            cmdline.add('"'.code.toUShort())

            var i = 0
            val len = arg.length
            while (i < len) {
                var numBackslashes = 0
                while (i < len && arg[i] == '\\') {
                    i++
                    numBackslashes++
                }

                if (i == len) {
                    for (k in 0 until numBackslashes * 2) {
                        cmdline.add('\\'.code.toUShort())
                    }
                    break
                } else if (arg[i] == '"') {
                    for (k in 0 until numBackslashes * 2 + 1) {
                        cmdline.add('\\'.code.toUShort())
                    }
                    cmdline.add(arg[i].code.toUShort())
                } else {
                    for (k in 0 until numBackslashes) {
                        cmdline.add('\\'.code.toUShort())
                    }
                    cmdline.add(arg[i].code.toUShort())
                }
                i++
            }
            cmdline.add('"'.code.toUShort())
        }
    }
}

internal fun isCwdRelativePath(path: String): Boolean {
    return path == "." || path.startsWith("./") || path == ".." || path.startsWith("../") ||
        path.startsWith(".\\") || path.startsWith("..\\")
}
