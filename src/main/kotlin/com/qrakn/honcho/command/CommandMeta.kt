package com.qrakn.honcho.command

/**
 * Defines a class as a command.
 *
 * @param value the command's label(s)
 * @param permission the command's permission node
 * @param description the command's description
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CommandMeta(
        vararg val value: String,
        val permission: String = "",
        val description: String = "")