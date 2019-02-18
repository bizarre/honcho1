package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import org.bukkit.Bukkit
import org.bukkit.ChatColor

class StringTypeAdapter : CommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(string)
    }

    override fun usage(string: String): String {
        return "${ChatColor.RED}The value '$string' is not valid."
    }
}