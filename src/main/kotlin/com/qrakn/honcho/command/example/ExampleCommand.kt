package com.qrakn.honcho.command.example

import com.qrakn.honcho.command.CPL
import com.qrakn.honcho.command.CommandMeta
import com.qrakn.honcho.command.CommandOption
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandMeta(label = ["example"], options = ["s"], description = "Example command", subcommands = true)
open class ExampleCommand {

    fun execute(sender: CommandSender, option: CommandOption?, player: Player) {
        sender.sendMessage("You poked ${player.name}.")

        if(option == null) {
            player.sendMessage("${sender.name} poked you.")
        } else {
            player.sendMessage("Someone poked you, but who?")
        }

        player.damage(0.0)
    }

    @CommandMeta(label = ["ver", "version"], description = "Displays honcho version")
    inner class ExampleSubCommand : ExampleCommand() {

        fun execute(sender: CommandSender) {
            sender.sendMessage("${ChatColor.RED}honcho v1.3-SNAPSHOT")
        }
    }
}