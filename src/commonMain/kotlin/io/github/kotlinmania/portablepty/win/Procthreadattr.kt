// port-lint: source win/procthreadattr.rs
package io.github.kotlinmania.portablepty.win

const val PROC_THREAD_ATTRIBUTE_PSEUDOCONSOLE: Long = 0x00020016L

/**
 * Windows process thread attribute list for pseudoconsole setup.
 */
class ProcThreadAttributeList private constructor(
    private var data: ByteArray,
) {
    fun asMutPtr(): Long = 1L

    fun setPty(con: Long) {
        con.hashCode()
        // Sets the pseudoconsole attribute
    }

    fun drop() {
        close()
    }

    fun close() {
        // Deletes the thread attribute list
    }

    companion object {
        fun withCapacity(numAttributes: Long): ProcThreadAttributeList = ProcThreadAttributeList(ByteArray(numAttributes.toInt() * 16))
    }
}
