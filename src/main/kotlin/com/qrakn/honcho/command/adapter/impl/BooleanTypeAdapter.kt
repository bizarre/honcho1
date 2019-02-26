package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.lang.Exception

class BooleanTypeAdapter : NonNullableCommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(MAP[string.toLowerCase()])
    }

    override fun onException(exception: Exception, sender: CommandSender, input: String): Boolean {
        sender.sendMessage("${ChatColor.RED}The value '$input' is not a boolean. ${ChatColor.GRAY}[yes, no]")
        return true
    }

    companion object {
        private val MAP = HashMap<String, Boolean>()

        init {
            MAP["true"] = true
            MAP["on"] = true
            MAP["yes"] = true

            MAP["false"] = false
            MAP["off"] = false
            MAP["no"] = false
        }
    }
}