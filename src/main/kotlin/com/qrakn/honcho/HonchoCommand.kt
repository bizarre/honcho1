package com.qrakn.honcho

import com.qrakn.honcho.command.CommandMeta
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.mutable.Mutable
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import java.util.*

internal class HonchoCommand(label: String, private val executor: HonchoExecutor) : BukkitCommand(label) {

    companion object {
        fun getHierarchicalLabel(clazz: Class<Any>, list: MutableList<String>): List<String> {
            val toReturn: MutableList<String> = ArrayList()
            val superClass = clazz.superclass

            if (superClass != null) {
                if (clazz.superclass.isAnnotationPresent(CommandMeta::class.java)) {
                    list.addAll(getHierarchicalLabel(superClass, list))
                }
            }

            val meta = clazz.getAnnotation(CommandMeta::class.java) as CommandMeta

            if (list.isEmpty()) {
                toReturn.addAll(Arrays.asList(*meta.label))
            } else {
                for (prefix in list) {
                    for (label in meta.label) {
                        toReturn.add("$prefix $label")
                    }
                }
            }

            return toReturn
        }
    }

    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        val message = arrayOf(commandLabel, *args)
        var label = commandLabel

        for (i in message.size downTo 1) {
            label = StringUtils.join(message, " ", 0, i)

            if (executor.commands[label.toLowerCase()] != null) {
                break
            }
        }

        val correctArguments = ArrayList<String>(args.toMutableList().subList(label.split(' ').size-1, args.size))

        return executor.execute(sender, this, label.toLowerCase(), correctArguments.toTypedArray())
    }

}