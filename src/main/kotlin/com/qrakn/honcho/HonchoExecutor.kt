package com.qrakn.honcho

import com.qrakn.honcho.command.CommandMeta
import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.SimplePluginManager
import java.lang.NullPointerException
import java.lang.reflect.Method
import java.util.HashMap

internal class HonchoExecutor(private val honcho: Honcho) : CommandExecutor {

    internal val adapters: MutableMap<Class<out Any>, CommandTypeAdapter> = HashMap()
    private val commands: MutableMap<String, CommandBinding> = HashMap()
    private val commandMap: CommandMap = getCommandMap()

    fun registerCommand(command: Any) {
        val meta: CommandMeta = command::class.java.getAnnotation(CommandMeta::class.java)!!

        val methods: MutableList<Method> = ArrayList()
        for (method in command::class.java.declaredMethods) {
            if (method.parameters.isNotEmpty() && method.parameters[0].type.isAssignableFrom(CommandSender::class.java)) {
                methods.add(method)
            }
        }

        val binding = CommandBinding(methods.toTypedArray(), command)
        for (label in meta.label) {
            commands[label] = binding

            if (commandMap.getCommand(label) == null) {
                commandMap.register(honcho.plugin.name, HonchoCommand(label, this))
            }
        }
    }

    fun execute(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val binding: CommandBinding = commands[label] ?: return false
        val meta = binding.command.javaClass.getAnnotation(CommandMeta::class.java)
        val instance = binding.command

        if (meta.permission.isNotEmpty() && !sender.hasPermission(meta.permission)) {
            sender.sendMessage("Nope.") // TODO send configurable no permission message (make command specific or impl specific?)
            return true
        }

        // TODO: optimize
        outer@ for (method in binding.methods) {
            val parameters = method.parameters

            if (parameters[0].type is Player && sender !is Player) continue
            if (method.declaringClass != instance.javaClass) continue

            if (method.parameterCount - 1 > args.size) {
                continue
            }

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
                        if (args.size - method.parameterCount > args.size - other.parameterCount) {
                            continue@outer
                        }
                    }

                }
            }

            val arguments: MutableList<Any> = arrayListOf(sender)
            for (i in 1 until parameters.size) {
                val parameter = parameters[i]
                val adapter = adapters[parameter.type]!!

                val translation: Any
                translation = if (i == parameters.lastIndex) {
                    adapter.convert(StringUtils.join(args, " ", i - 1, args.size), parameter.type)
                } else {
                    adapter.convert(args[i - 1], parameter.type)
                }

                arguments.add(translation)
            }

            if (arguments.size == parameters.size) {
                method.invoke(instance, *arguments.toTypedArray())
            }

            return true
        }

        return false
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return execute(sender, command, label, args)
    }

    private fun getCommandMap(): CommandMap {
        val manager: PluginManager = Bukkit.getPluginManager()

        if (manager is SimplePluginManager) {
            val field = manager.javaClass.getDeclaredField("commandMap")

            field.isAccessible = true

            return field.get(manager) as CommandMap
        }

        throw NullPointerException()
    }

    private inner class CommandBinding(val methods: Array<Method>, val command: Any)

}