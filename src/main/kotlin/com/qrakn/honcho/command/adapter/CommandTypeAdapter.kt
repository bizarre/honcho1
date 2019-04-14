package com.qrakn.honcho.command.adapter

import org.bukkit.command.CommandSender
import java.lang.Exception

interface CommandTypeAdapter {
    fun <T> convert(string: String, type: Class<T>): T
    fun onException(exception: Exception, sender: CommandSender, input: String): Boolean { return false }
    fun onTabComplete(string: String): MutableList<String> { return mutableListOf() }
}