package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.lang.Exception

class IntegerTypeAdapter : NonNullableCommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(Integer.parseInt(string))
    }

    override fun onException(exception: Exception, sender: CommandSender, input: String): Boolean {
        sender.sendMessage("${ChatColor.RED}The value '$input' is not an integer. ${ChatColor.GRAY}[1, 2, 3]")
        return true
    }
}