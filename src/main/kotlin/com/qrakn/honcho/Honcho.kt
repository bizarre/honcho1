package com.qrakn.honcho

import com.qrakn.honcho.command.CommandOption
import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import com.qrakn.honcho.command.adapter.impl.*
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class Honcho(val plugin: JavaPlugin) {

    private val executor: HonchoExecutor = HonchoExecutor(this)
    var noPermissionMessage = "${ChatColor.RED}You don't have permission to do this."

    init {
        registerTypeAdapter(Boolean::class.java, BooleanTypeAdapter())
        registerTypeAdapter(ChatColor::class.java, ChatColorTypeAdapter())
        registerTypeAdapter(Double::class.java, DoubleTypeAdapter())
        registerTypeAdapter(GameMode::class.java, GameModeTypeAdapter())
        registerTypeAdapter(Integer::class.java, IntegerTypeAdapter())
        registerTypeAdapter(OfflinePlayer::class.java, OfflinePlayerTypeAdapter())
        registerTypeAdapter(CommandOption::class.java, CommandOptionTypeAdapter())
        registerTypeAdapter(Player::class.java, PlayerTypeAdapter())
        registerTypeAdapter(String::class.java, StringTypeAdapter())
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