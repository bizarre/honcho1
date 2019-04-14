package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class PlayerTypeAdapter : NonNullableCommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(Bukkit.getPlayer(string))
    }

    override fun onException(exception: Exception, sender: CommandSender, input: String): Boolean {
        if (exception is NullPointerException) {
            sender.sendMessage("${ChatColor.RED}No player named \"$input\" found.")
            return true
        }

        return false
    }

    override fun onTabComplete(string: String): MutableList<String> {
        val toReturn = ArrayList<String>()

        Bukkit.getOnlinePlayers().filter { it.name.toLowerCase().startsWith(string.toLowerCase()) }.forEach {
            toReturn.add(it.name)
        }

        return toReturn
    }

}