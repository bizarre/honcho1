package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.CommandTypeAdapter

class IntegerTypeAdapter : CommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(Integer.parseInt(string))
    }
}