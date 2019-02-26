package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.lang.Exception

class DoubleTypeAdapter : NonNullableCommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(string.toDoubleOrNull())
    }

    override fun onException(exception: Exception, sender: CommandSender, input: String): Boolean {
        sender.sendMessage("${ChatColor.RED}The value '$input' is not a double. ${ChatColor.GRAY}[2.0, 1.2, 1.5]")
        return true
    }
}