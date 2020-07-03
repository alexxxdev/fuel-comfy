package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName

class CoroutinesResponseAdapter : BaseResponseAdapter {
    private val awaitStringResponseResultClassName = ClassName("com.github.kittinunf.fuel.coroutines", "awaitStringResponseResult")
    private val awaitObjectResponseResultClassName = ClassName("com.github.kittinunf.fuel.coroutines", "awaitObjectResponseResult")

    override fun writeString(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit, block: () -> Unit) {
        statement("\t.%T()", arrayOf(awaitStringResponseResultClassName))
    }

    override fun writeObject(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit, block: () -> Unit) {
        statement("\t.%T(", arrayOf(awaitObjectResponseResultClassName))
        block()
        statement("\t)", arrayOf())
    }
}
