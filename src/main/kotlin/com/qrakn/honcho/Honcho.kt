package com.qrakn.honcho

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import com.qrakn.honcho.command.adapter.impl.PlayerTypeAdapter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class Honcho(val plugin: JavaPlugin) {

    private val adapters: MutableMap<Class<out Any>, CommandTypeAdapter> = HashMap()
    private val commands: MutableMap<String, Any> = HashMap()

    init {
        registerTypeAdapter(Player::class.java, PlayerTypeAdapter())
    }

    /**
     * Registers a command type adapter
     *
     * Registers a command type adapter to be used in translating
     * user input into POJO's to be utilized by registered command classes
     * annotated with @[CommandMeta].
     *
     * @param clazz the class that will the adapter will be translating
     * @param adapter the command type adapter
     */
    fun registerTypeAdapter(clazz: Class<out Any>, adapter: CommandTypeAdapter) {
        adapters.putIfAbsent(clazz, adapter)
    }

}