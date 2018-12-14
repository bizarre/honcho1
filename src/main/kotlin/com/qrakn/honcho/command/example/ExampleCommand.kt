package com.qrakn.honcho.command.example

import com.qrakn.honcho.command.CommandMeta
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandMeta(label = ["example"], description = "Example command", subcommands = true)
open class ExampleCommand {

    fun execute(sender: CommandSender, player: Player?) {

        if (player == null) {
            sender.sendMessage("${ChatColor.RED}Player not found.")
            return
        }

        sender.sendMessage("You poked ${player.name}.")

        player.sendMessage("${sender.name} poked you.")
        player.damage(0.0)
    }

    @CommandMeta(label = ["ver", "version"], description = "Displays honcho version")
    inner class ExampleSubCommand : ExampleCommand() {

        fun execute(sender: CommandSender) {
            sender.sendMessage("${ChatColor.RED}honcho v1.0-SNAPSHOT")
        }

    }

}