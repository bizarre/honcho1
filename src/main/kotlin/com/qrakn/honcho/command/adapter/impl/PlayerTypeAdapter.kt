package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import org.bukkit.Bukkit
import org.bukkit.ChatColor

class PlayerTypeAdapter : CommandTypeAdapter {

    override fun getUsageMessage(string: String): String {
        return "{${ChatColor.RED}Player with the name '{$string}' does not exist or is offline."
    }

    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(Bukkit.getPlayer(string))
    }
}