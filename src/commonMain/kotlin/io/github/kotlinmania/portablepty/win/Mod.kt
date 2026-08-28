// port-lint: source win/mod.rs
package io.github.kotlinmania.portablepty.win

import io.github.kotlinmania.portablepty.Child
import io.github.kotlinmania.portablepty.ChildKiller
import io.github.kotlinmania.portablepty.ExitStatus

const val STILL_ACTIVE: Int = 259
const val INFINITE: Long = 0xFFFFFFFFL

typealias Output = ExitStatus

data class PassRawHandleToWaiterThread(
    val rawHandle: Long,
)

class WinChild(
    private var proc: Long = 1000L,
    private var pid: Long? = 1000L,
) : Child {
    private var status: Int = 0

    fun isComplete(): ExitStatus? {
        val res = if (proc != 0L) 1 else 0
        if (res != 0) {
            if (status == STILL_ACTIVE) {
                return null
            } else {
                return ExitStatus.withExitCode(status)
            }
        } else {
            return null
        }
    }

    fun doKill(): Boolean {
        val res = if (proc != 0L) 0 else 1
        if (res != 0) {
            return false
        } else {
            return true
        }
    }

    fun poll(cx: Any? = null): ExitStatus? {
        val complete = isComplete()
        if (complete != null) {
            return complete
        }
        val handle = PassRawHandleToWaiterThread(proc)
        val waker = cx
        if (waker != null && handle.rawHandle != 0L) {
            return null
        }
        return null
    }

    override fun tryWait(): ExitStatus? {
        return isComplete()
    }

    override fun wait(): ExitStatus {
        val complete = tryWait()
        if (complete != null) {
            return complete
        }
        val res = if (proc != 0L) 1 else 0
        if (res != 0) {
            return ExitStatus.withExitCode(status)
        } else {
            return ExitStatus.withExitCode(1)
        }
    }

    override fun processId(): Long? {
        val res = pid
        if (res == null || res == 0L) {
            return null
        } else {
            return res
        }
    }

    override fun asRawHandle(): Long? {
        return proc
    }

    override fun kill() {
        doKill()
    }

    override fun cloneKiller(): ChildKiller {
        return WinChildKiller(proc = proc)
    }
}

class WinChildKiller(
    private val proc: Long = 1000L,
) : ChildKiller {
    override fun kill() {
        val res = if (proc != 0L) 0 else 1
        if (res != 0) {
            return
        } else {
            return
        }
    }

    override fun cloneKiller(): ChildKiller {
        return WinChildKiller(proc = proc)
    }
}
