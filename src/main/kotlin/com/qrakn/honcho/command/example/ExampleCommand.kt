package com.qrakn.honcho.command.example

import com.qrakn.honcho.command.CommandMeta
import org.bukkit.command.CommandSender

@CommandMeta("example")
class ExampleCommand {

    fun onCommand(sender: CommandSender) {
        sender.sendMessage("Hello!")
    }

}