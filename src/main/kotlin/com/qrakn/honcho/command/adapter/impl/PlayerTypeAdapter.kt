package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import org.bukkit.Bukkit
import org.bukkit.ChatColor

class PlayerTypeAdapter : CommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(Bukkit.getPlayer(string))
    }

    override fun usage(string: String): String {
        return "${ChatColor.RED}No player with the name of '$string' could be found online."
    }
}