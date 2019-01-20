package com.qrakn.honcho

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import com.qrakn.honcho.command.adapter.impl.*
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Honcho(val plugin: JavaPlugin) {

    private val executor: HonchoExecutor = HonchoExecutor(this)

    init {
        registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        registerTypeAdapter(GameMode::class.java, GameModeTypeAdapter())
        registerTypeAdapter(Integer::class.java, IntegerTypeAdapter())
        registerTypeAdapter(OfflinePlayer::class.java, OfflinePlayerTypeAdapter())
        registerTypeAdapter(String::class.java, StringTypeAdapter())
        registerTypeAdapter(Player::class.java, PlayerTypeAdapter())
        registerTypeAdapter(World::class.java, WorldTypeAdapter())
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
        executor.adapters.putIfAbsent(clazz, adapter)
    }

}