package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.CommandOption
import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import org.bukkit.command.CommandSender

class CommandOptionTypeAdapter : CommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T? {
        return if (string.startsWith("-")) {
            type.cast(CommandOption(string.toLowerCase().substring(1)))
        } else null
    }

    override fun onException(exception: Exception, sender: CommandSender, input: String): Boolean {
        return false
    }
}
