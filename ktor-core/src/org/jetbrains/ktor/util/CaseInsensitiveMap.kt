package org.jetbrains.ktor.util

import org.jetbrains.ktor.org.apache.commons.collections4.map.*

class CaseInsensitiveMap<V>(initialCapacity: Int = 16) : AbstractLinkedMap<String, V>(Math.max(2, initialCapacity)), MutableMap<String, V> {
    override fun hash(key: Any?): Int {
        if (key == null) return 0
        if (key !is String) return key.hashCode()

        val s = key as String
        var hashCode = 0

        for (idx in 0 .. s.length - 1) {
            hashCode = 31 * hashCode + s[idx].toLowerCase().toInt()
        }

        return hashCode
    }

    override fun isEqualKey(key1: Any?, key2: Any?): Boolean {
        if (key1 is String && key2 is String) {
            return key1.equals(key2, ignoreCase = true)
        }

        return super.isEqualKey(key1, key2)
    }

    private fun Char.toLowerCase(): Char {
        return when {
            this < 'A' -> this
            this <= 'Z' -> this + 32
            this <= '~' -> this
            else -> Character.toLowerCase(this)
        }
    }
}