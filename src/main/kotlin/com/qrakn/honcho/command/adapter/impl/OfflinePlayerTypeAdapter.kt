package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.lang.Exception

class OfflinePlayerTypeAdapter : NonNullableCommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(Bukkit.getOfflinePlayer(string))
    }

    override fun onException(exception: Exception, sender: CommandSender, input: String): Boolean {
        sender.sendMessage("${ChatColor.RED}The player '$input' could not be found.")
        return true
    }
}