package com.qrakn.honcho

import com.qrakn.honcho.command.CPL
import com.qrakn.honcho.command.CommandMeta
import com.qrakn.honcho.command.CommandOption
import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import com.qrakn.honcho.command.adapter.NonNullableCommandTypeAdapter
import org.apache.commons.lang.StringUtils
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.SimplePluginManager
import org.bukkit.scheduler.BukkitRunnable
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.reflect.Method
import java.util.*

internal class HonchoExecutor(private val honcho: Honcho) : CommandExecutor {

    internal val adapters: MutableMap<Class<out Any>, CommandTypeAdapter> = HashMap()
    internal val commands: MutableMap<String, CommandBinding> = HashMap()
    private val commandMap: CommandMap = getCommandMap()

    fun registerCommand(command: Any) {
        val meta: CommandMeta = command::class.java.getAnnotation(CommandMeta::class.java)!!

        val methods: MutableList<Method> = ArrayList()
        for (method in command::class.java.declaredMethods) {
            if (method.parameters.isNotEmpty() && method.parameters[0].type.isAssignableFrom(Player::class.java)) {
                methods.add(method)
            }
        }

        val binding = CommandBinding(methods.toTypedArray(), meta, command)
        for (label in HonchoCommand.getHierarchicalLabel(command.javaClass, ArrayList())) {
            commands[label] = binding

            if (commandMap.getCommand(label) == null) {
                val honchoCommand = HonchoCommand(label, this)

                honchoCommand.usage = getCommandUsage(label)
                honchoCommand.description = meta.description

                commandMap.register(honcho.plugin.name, honchoCommand)
            }
        }

        if (meta.subcommands) {
            for (clazz in command::class.java.declaredClasses) {
                registerCommand(clazz.getDeclaredConstructor(command::class.java).newInstance(command))
            }
        }
    }

    fun execute(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
        val binding: CommandBinding = commands[label] ?: return false
        val meta = binding.command.javaClass.getAnnotation(CommandMeta::class.java)
        val instance = binding.command
        var option = false

        if (meta.permission.isNotEmpty() && !sender.hasPermission(meta.permission)) {
            var message = honcho.noPermissionMessage

            if (meta.noPermissionMessage.isNotEmpty()) {
                message = meta.noPermissionMessage
            }

            sender.sendMessage(message)
            return true
        }

        // TODO: optimize
        val runnable = object : BukkitRunnable() {
            override fun run() {
                outer@ for (method in binding.methods) {
                    val parameters = method.parameters
                    var doContinue = true

                    if (parameters[0].type is Player && sender !is Player) continue
                    if (method.declaringClass != instance.javaClass) continue

                    if (method.parameterCount - 1 > args.size) {
                        for(param in parameters) {
                            if(param.type == CommandOption::class.java && method.parameterCount - 2 <= args.size) {
                                doContinue = false
                                break
                            }
                        }

                        if(doContinue) {
                            continue
                        }
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
                                if (method.parameterCount < other.parameterCount) {
                                    continue@outer
                                }
                            }

                        }
                    }

                    val arguments: MutableList<Any?> = arrayListOf(sender)
                    for (i in 1 until parameters.size) {
                        val parameter = parameters[i]
                        val adapter = adapters[parameter.type]!!

                        val input = if (i == parameters.lastIndex) {
                            StringUtils.join(args, " ", i - 1, args.size)
                        } else {
                            args[i - 1]
                        }

                        try {
                            val conversion = adapter.convert(input!!, parameter.type)

                            if (conversion == null && adapter is NonNullableCommandTypeAdapter) {
                                throw NullPointerException()
                            }

                            if(conversion is CommandOption) {
                                if(!meta.options.contains(conversion.tag.toLowerCase())) {
                                    sender.sendMessage("${ChatColor.RED}Unrecognized command option '-${conversion.tag}'!")
                                    sender.sendMessage("${ChatColor.RED}Usage: ${command.usage}")
                                    return
                                }
                            }

                            arguments.add(conversion)
                        } catch (exception: Exception) {
                            if (!adapter.onException(exception, sender, input!!)) { // if exception not handled by adapter
                                sender.sendMessage("${ChatColor.RED}An error occurred (${exception.message}), please contact an administrator")
                            }
                            return
                        }

                    }

                    if (arguments.size == parameters.size) {
                        method.invoke(instance, *arguments.toTypedArray())
                        return
                    }
                }

                sender.sendMessage("${ChatColor.RED}Usage: ${command.usage}") // todo: make configurable
            }
        }

        if (meta.async) {
            runnable.runTaskAsynchronously(honcho.plugin)
        } else {
            runnable.runTask(honcho.plugin)
        }

        return true
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String?>): Boolean {
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

    //TODO: redo this so that every method gets its own usage
    private fun getCommandUsage(label: String): String {
        val command = commands[label]!!
        val builder = StringBuilder("/").append(label)
        val arguments: MutableMap<Int, CommandArguments?> = HashMap()

        if(command.meta.options.isNotEmpty()) {
            val options = ArrayList<String>()

            command.meta.options.forEach {
                options.add("-" + it.toLowerCase())
            }

            builder.append(" [")
            builder.append(StringUtils.join(options, ","))
            builder.append("]")
        }

        for (method in command.methods) {
            val parameters = method.parameters

            for (i in 1 until parameters.size) {
                val argument = arguments.getOrDefault(i - 1, CommandArguments(arrayListOf()))
                val parameter = parameters[i]

                val name: String = if (parameter.isAnnotationPresent(CPL::class.java)) {
                    parameter.getAnnotation(CPL::class.java).value
                } else {
                    parameter.name
                }

                if(parameter.type == CommandOption::class.java) {
                    arguments[i - 1] = null
                } else {
                    if (argument != null) {
                        if (!(argument.arguments.contains(name))) {
                            argument.arguments.add(name)
                            arguments[i - 1] = argument
                        }
                    }
                }
            }
        }

        for (index in 0 until arguments.size) {
            val argument = arguments[index]

            if(argument != null) {
                builder.append(" <").append(StringUtils.join(argument.arguments, "/")).append(">")
            }
        }

        return builder.toString()
    }

    private inner class CommandArguments(val arguments: MutableList<String>)
    internal inner class CommandBinding(val methods: Array<Method>, val meta: CommandMeta, val command: Any)

}