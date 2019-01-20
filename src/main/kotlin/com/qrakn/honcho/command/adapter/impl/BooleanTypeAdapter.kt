package com.qrakn.honcho.command.adapter.impl

import com.qrakn.honcho.command.adapter.CommandTypeAdapter
import java.util.*

class BooleanTypeAdapter : CommandTypeAdapter {
    override fun <T> convert(string: String, type: Class<T>): T {
        return type.cast(MAP[string.toLowerCase()])
    }

    companion object {
        private val MAP = HashMap<String, Boolean>()

        init {
            MAP["true"] = true
            MAP["on"] = true
            MAP["yes"] = true

            MAP["false"] = false
            MAP["off"] = false
            MAP["no"] = false
        }
    }
}