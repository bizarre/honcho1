package com.qrakn.honcho

import com.qrakn.honcho.command.CommandMeta
import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.mutable.Mutable
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player
import java.lang.Exception
import java.lang.NullPointerException
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

        val correctArguments = ArrayList<String>(args.toMutableList().subList(label.split(' ').size - 1, args.size))

        return executor.execute(sender, this, label.toLowerCase(), correctArguments.toTypedArray())
    }

    override fun tabComplete(sender: CommandSender, commandLabel: String, args: Array<out String>): MutableList<String> {
        val message = arrayOf(commandLabel, *args)
        var label: String
        var binding: HonchoExecutor.CommandBinding? = null

        for (i in message.size downTo 1) {
            label = StringUtils.join(message, " ", 0, i)

            binding = executor.commands[label.toLowerCase()]
            if (binding != null) {
                break
            }
        }

        if (binding == null) return mutableListOf()

        val instance = binding.command
        val arg = args.last()

        outer@ for (method in binding.methods) {
            val parameters = method.parameters

            if (parameters[0].type is Player && sender !is Player) continue
            if (method.declaringClass != instance.javaClass) continue

            /**
             * Prioritizes the method that is exact type of sender + lowest argument count
             */
            for (other in binding.methods) {
                if (other != method) {

                    if (method.parameterCount == other.parameterCount) {
                        if (parameters[0].type is CommandSender && sender is Player && other.parameters[0].type is Player) {
                            continue@outer
                        }
                    }

                    if (method.parameterCount - 1 != args.size) {
                        if (method.parameterCount < other.parameterCount) {
                            continue@outer
                        }
                    }

                }
            }

            val parameter = parameters[args.indexOf(arg)]
            val adapter = executor.adapters[parameter.type]

            if (adapter == null) {
                val toReturn = ArrayList<String>()

                Bukkit.getOnlinePlayers().filter { it.name.toLowerCase().startsWith(arg.toLowerCase()) }.forEach {
                    toReturn.add(it.name)
                }

                return toReturn
            }

            return adapter.onTabComplete(arg)
        }

        return mutableListOf()
    }

}