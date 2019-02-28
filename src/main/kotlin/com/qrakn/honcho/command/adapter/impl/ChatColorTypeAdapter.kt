package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.lang.Exception

class ChatColorTypeAdapter : NonNullableCommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T? {
        return type.cast(ChatColor.valueOf(string.toUpperCase()))
    }

    override fun onException(exception: Exception, sender: CommandSender, input: String): Boolean {
        sender.sendMessage("${ChatColor.RED}The value '$input' is not a chatcolor. ${ChatColor.GRAY}[RED, DARK_RED]")
        return true
    }
}