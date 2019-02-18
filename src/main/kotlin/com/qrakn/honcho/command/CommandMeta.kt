package com.qrakn.honcho.command

/**
 * Defines a class as a command.
 *
 * @param label the command's label(s)
 * @param permission the command's permission node
 * @param description the command's description
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandMeta(
        vararg val label: String,
        val permission: String = "",
        val permissionMessage: String = "&cNo permission.",
        val description: String = "",
        val subcommands: Boolean = false,
        val async: Boolean = false)