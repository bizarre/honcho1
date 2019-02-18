package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import org.bukkit.ChatColor

class BooleanTypeAdapter : CommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(MAP[string.toLowerCase()])
    }

    override fun usage(string: String): String {
        return "${ChatColor.RED}The value '$string' is not a valid boolean. (true, false)"
    }

    companion object {
        private val MAP = HashMap<String, Boolean>()

        init {
            MAP["true"] = true
            MAP["on"] = true
            MAP["yes"] = true

            MAP["false"] = false
            MAP["off"] = false
            MAP["no"] = false
        }
    }
}