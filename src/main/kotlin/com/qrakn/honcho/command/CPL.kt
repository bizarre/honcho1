package com.qrakn.honcho.command


/**
 * Defines a command parameter's display text/label
 *
 * @param value the label of the parameter
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CPL(val value: String)