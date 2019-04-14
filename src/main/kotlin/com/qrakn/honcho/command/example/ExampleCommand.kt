package com.qrakn.honcho.command.example

import com.qrakn.honcho.command.CommandMeta
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandMeta(label = ["example"], description = "Example command", subcommands = true)
open class ExampleCommand {

    @CommandMeta(label = ["ver", "version"], description = "Displays honcho version")
    inner class ExampleSubCommand : ExampleCommand() {

        fun execute(sender: CommandSender, player: Player) {
            sender.sendMessage("${ChatColor.RED}${player.name}")
        }

    }

}