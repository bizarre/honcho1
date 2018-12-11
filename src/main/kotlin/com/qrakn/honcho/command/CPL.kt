package com.qrakn.honcho.command


/**
 * Defines a command parameter's display text/label
 *
 * @param value the label of the parameter
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class CPL(val value: String)