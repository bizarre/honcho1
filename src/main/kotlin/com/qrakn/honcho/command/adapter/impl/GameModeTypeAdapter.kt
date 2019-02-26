package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import java.lang.Exception
import java.util.*

class GameModeTypeAdapter : NonNullableCommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(MAP[string.toLowerCase()])
    }

    override fun onException(exception: Exception, sender: CommandSender, input: String): Boolean {
        sender.sendMessage("${ChatColor.RED}The value '$input' is not a gamemode. ${ChatColor.GRAY}[creative, 0, a]")
        return true
    }

    companion object {
        private val MAP = HashMap<String, GameMode>()

        init {
            MAP["c"] = GameMode.CREATIVE
            MAP["creative"] = GameMode.CREATIVE
            MAP["1"] = GameMode.CREATIVE

            MAP["s"] = GameMode.SURVIVAL
            MAP["survival"] = GameMode.SURVIVAL
            MAP["0"] = GameMode.SURVIVAL

            MAP["a"] = GameMode.ADVENTURE
            MAP["adventure"] = GameMode.ADVENTURE
            MAP["2"] = GameMode.ADVENTURE

            MAP["sp"] = GameMode.SPECTATOR
            MAP["spectator"] = GameMode.SPECTATOR
            MAP["3"] = GameMode.SPECTATOR
        }
    }
}