package com.qrakn.honcho

import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand

internal class HonchoCommand(label: String, private val executor: HonchoExecutor) : BukkitCommand(label) {

    override fun execute(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        return executor.execute(sender, this, label, args)
    }

}