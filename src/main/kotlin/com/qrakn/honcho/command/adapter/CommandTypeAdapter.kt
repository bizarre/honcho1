package com.qrakn.honcho.command.adapter

interface CommandTypeAdapter {
    fun <T> convert(string: String, type: Class<T>): T

    fun usage(string: String): String
}