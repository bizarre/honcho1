package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import org.bukkit.ChatColor

class IntegerTypeAdapter : CommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(Integer.parseInt(string))
    }

    override fun usage(string: String): String {
        return "${ChatColor.RED}The value '$string' is not a valid integer."
    }
}