package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import org.bukkit.ChatColor
import org.bukkit.GameMode
import java.util.*

class GameModeTypeAdapter : CommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(MAP[string.toLowerCase()])
    }

    override fun usage(string: String): String {
        return "${ChatColor.RED}The value '$string' is not a valid GameMode."
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
        }
    }
}