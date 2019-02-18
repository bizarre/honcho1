package com.qrakn.honcho

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import com.qrakn.honcho.command.adapter.impl.PlayerTypeAdapter
import com.qrakn.honcho.command.adapter.impl.StringTypeAdapter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Honcho(val plugin: JavaPlugin) {

    private val executor: HonchoExecutor = HonchoExecutor(this)

    init {
        registerTypeAdapter(String::class.java, StringTypeAdapter())
        registerTypeAdapter(Player::class.java, PlayerTypeAdapter())
    }

    fun registerCommand(command: Any) {
        executor.registerCommand(command)
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
        executor.adapters[clazz] = adapter
    }

}