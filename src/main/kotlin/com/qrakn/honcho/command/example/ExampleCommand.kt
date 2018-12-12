package com.qrakn.honcho.command.example

import com.qrakn.honcho.command.CommandMeta
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandMeta(label = ["example"])
class ExampleCommand {

    fun onCommand(sender: CommandSender) {
        sender.sendMessage("Hello!")
    }

    fun onCommand(sender: CommandSender, player: Player) {
        sender.sendMessage("You poked ${player.name}.")

        player.sendMessage("${sender.name} poked you.")
        player.damage(0.0)
    }

}