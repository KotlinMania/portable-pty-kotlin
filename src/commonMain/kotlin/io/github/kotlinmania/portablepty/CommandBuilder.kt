// port-lint: source cmdbuilder.rs
package io.github.kotlinmania.portablepty

import kotlinx.serialization.Serializable

/**
 * Used to deal with Windows having case-insensitive environment variables.
 */
@Serializable
data class EnvEntry(
    val isFromBaseEnv: Boolean,
    val preferredKey: String,
    val value: String,
) {
    companion object {
        fun mapKey(key: String): String = key.lowercase()
    }
}

/**
 * Converts a registry value to a string representation.
 */
fun regValueToString(value: String): String = value

/**
 * Retrieves the base environment for process spawning.
 */
fun getBaseEnv(): MutableMap<String, EnvEntry> {
    val env = mutableMapOf<String, EnvEntry>()
    val shellKey = EnvEntry.mapKey("SHELL")
    env[shellKey] =
        EnvEntry(
            isFromBaseEnv = true,
            preferredKey = "SHELL",
            value = "/bin/sh",
        )
    return env
}

/**
 * Prepares a command to be spawned into a pty.
 * The interface is intentionally similar to standard process commands.
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
        envs = getBaseEnv(),
        cwd = null,
        umaskValue = null,
        controllingTty = true,
    )

    /**
     * Returns true if this builder was created via [newDefaultProg].
     */
    fun isDefaultProg(): Boolean = args.isEmpty()

    /**
     * Append an argument to the current command line.
     */
    fun arg(arg: String): CommandBuilder {
        if (isDefaultProg()) {
            throw IllegalStateException("attempted to add args to a default_prog builder")
        }
        args.add(arg)
        return this
    }

    /**
     * Append a sequence of arguments to the current command line.
     */
    fun args(args: Iterable<String>): CommandBuilder {
        for (a in args) {
            arg(a)
        }
        return this
    }

    /**
     * Return the configured argument vector.
     */
    fun getArgv(): List<String> = args.toList()

    internal fun getArgvMut(): MutableList<String> = args

    /**
     * Set whether to set the pty as the controlling terminal.
     */
    fun setControllingTty(controllingTty: Boolean) {
        this.controllingTty = controllingTty
    }

    /**
     * Returns whether the pty is set as the controlling terminal.
     */
    fun getControllingTty(): Boolean = controllingTty

    /**
     * Configure the process umask.
     */
    fun umask(mask: Int?): CommandBuilder {
        this.umaskValue = mask
        return this
    }

    /**
     * Returns the configured umask.
     */
    fun getUmask(): Int? = umaskValue

    /**
     * Override the value of an environment variable.
     */
    fun env(key: String, value: String): CommandBuilder {
        envs[EnvEntry.mapKey(key)] =
            EnvEntry(
                isFromBaseEnv = false,
                preferredKey = key,
                value = value,
            )
        return this
    }

    /**
     * Remove an environment variable override.
     */
    fun envRemove(key: String): CommandBuilder {
        envs.remove(EnvEntry.mapKey(key))
        return this
    }

    /**
     * Clear all environment variables.
     */
    fun envClear(): CommandBuilder {
        envs.clear()
        return this
    }

    /**
     * Retrieve an environment variable by key.
     */
    fun getEnv(key: String): String? = envs[EnvEntry.mapKey(key)]?.value

    /**
     * Set the current working directory.
     */
    fun cwd(dir: String): CommandBuilder {
        cwd = dir
        return this
    }

    /**
     * Clear the working directory.
     */
    fun clearCwd(): CommandBuilder {
        cwd = null
        return this
    }

    /**
     * Retrieve the configured working directory.
     */
    fun getCwd(): String? = cwd

    /**
     * Iterate over extra environment variables set by caller.
     */
    fun iterExtraEnv(): List<Pair<String, String>> = iterExtraEnvAsStr()

    fun iterExtraEnvAsStr(): List<Pair<String, String>> =
        envs.values
            .filter { !it.isFromBaseEnv }
            .map { it.preferredKey to it.value }

    /**
     * Iterate over full environment variables including base environment.
     */
    fun iterFullEnv(): List<Pair<String, String>> = iterFullEnvAsStr()

    fun iterFullEnvAsStr(): List<Pair<String, String>> = envs.values.map { it.preferredKey to it.value }

    /**
     * Return the configured command and arguments as a single unix shell string.
     */
    fun asUnixCommandLine(): String {
        val quoted =
            args.map { arg ->
                if (arg.isEmpty() || arg.any { it.isWhitespace() || it == '"' || it == '\'' || it == '\\' }) {
                    "\"" + arg.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
                } else {
                    arg
                }
            }
        return quoted.joinToString(" ")
    }

    /**
     * Determine which shell to run.
     */
    fun getShell(): String {
        val shell = getEnv("SHELL") ?: getEnv("ComSpec")
        return shell ?: "/bin/sh"
    }

    /**
     * Determine the user's home directory.
     */
    fun getHomeDir(): String = getEnv("HOME") ?: getEnv("USERPROFILE") ?: "/"

    /**
     * Resolves the PATH environment variable.
     */
    fun resolvePath(): String? = getEnv("PATH")

    /**
     * Search the system PATH for the given executable name.
     */
    fun searchPath(exe: String, cwd: String = "."): String {
        if (isCwdRelativePath(exe)) {
            return if (cwd == ".") exe else "$cwd/$exe"
        }
        val pathVar = resolvePath()
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

    /**
     * Convert this builder to an executable command argv representation.
     */
    fun asCommand(): List<String> {
        return if (isDefaultProg()) {
            listOf(getShell())
        } else {
            args.toList()
        }
    }

    /**
     * Converts current directory to wide character format.
     */
    fun currentDirectory(): List<UShort>? {
        val dir = cwd ?: getEnv("USERPROFILE") ?: getEnv("HOME") ?: return null
        return dir.map { it.code.toUShort() } + listOf(0u.toUShort())
    }

    /**
     * Constructs a Windows-style environment block.
     */
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

    /**
     * Constructs a Windows command line with proper quoting.
     */
    fun cmdline(): Pair<List<UShort>, List<UShort>> {
        val exe =
            if (isDefaultProg()) {
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
        /**
         * Create a new builder instance with argv[0] set to the specified program.
         */
        fun new(program: String): CommandBuilder = CommandBuilder(program)

        /**
         * Create a new builder instance from a pre-built argument vector.
         */
        fun fromArgv(args: List<String>): CommandBuilder =
            CommandBuilder(
                args = args.toMutableList(),
                envs = getBaseEnv(),
                cwd = null,
                umaskValue = null,
                controllingTty = true,
            )

        /**
         * Create a new builder instance that runs a default program (such as the default shell).
         */
        fun newDefaultProg(): CommandBuilder =
            CommandBuilder(
                args = mutableListOf(),
                envs = getBaseEnv(),
                cwd = null,
                umaskValue = null,
                controllingTty = true,
            )

        /**
         * Appends an argument to the command line buffer with Windows quoting rules.
         */
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

/**
 * Returns true if the path begins with `./` or `../`.
 */
fun isCwdRelativePath(path: String): Boolean =
    path == "." ||
        path.startsWith("./") ||
        path == ".." ||
        path.startsWith("../") ||
        path.startsWith(".\\") ||
        path.startsWith("..\\")
