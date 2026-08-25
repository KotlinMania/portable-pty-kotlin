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
        fun mapKey(key: String): String = key
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
    private var controllingTty: Boolean,
) {
    constructor(program: String) : this(
        args = mutableListOf(program),
        envs = mutableMapOf(),
        cwd = null,
        controllingTty = true,
    )

    fun isDefaultProg(): Boolean = args.isEmpty()

    fun arg(arg: String): CommandBuilder {
        if (isDefaultProg()) {
            throw IllegalStateException("Attempted to add args to a default_prog builder")
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

    fun setControllingTty(controllingTty: Boolean) {
        this.controllingTty = controllingTty
    }

    fun getControllingTty(): Boolean = controllingTty

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

    fun getEnv(key: String): String? {
        return envs[EnvEntry.mapKey(key)]?.value
    }

    fun cwd(dir: String): CommandBuilder {
        cwd = dir
        return this
    }

    fun clearCwd(): CommandBuilder {
        cwd = null
        return this
    }

    fun getCwd(): String? = cwd

    fun iterExtraEnv(): List<Pair<String, String>> {
        return envs.values
            .filter { !it.isFromBaseEnv }
            .map { it.preferredKey to it.value }
    }

    fun iterFullEnv(): List<Pair<String, String>> {
        return envs.values.map { it.preferredKey to it.value }
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

    companion object {
        fun new(program: String): CommandBuilder = CommandBuilder(program)

        fun fromArgv(args: List<String>): CommandBuilder = CommandBuilder(
            args = args.toMutableList(),
            envs = mutableMapOf(),
            cwd = null,
            controllingTty = true,
        )

        fun newDefaultProg(): CommandBuilder = CommandBuilder(
            args = mutableListOf(),
            envs = mutableMapOf(),
            cwd = null,
            controllingTty = true,
        )
    }
}

internal fun isCwdRelativePath(path: String): Boolean {
    return path == "." || path.startsWith("./") || path == ".." || path.startsWith("../")
}
