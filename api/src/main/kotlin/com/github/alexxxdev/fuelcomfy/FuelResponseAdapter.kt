package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName

class FuelResponseAdapter : BaseResponseAdapter {
    override fun writeString(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit, block: () -> Unit) {
        statement("\t.responseString()", arrayOf())
    }

    override fun writeObject(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit, block: () -> Unit) {
        statement("\t.responseObject(", arrayOf())
        block()
        statement("\t)", arrayOf())
    }
}
