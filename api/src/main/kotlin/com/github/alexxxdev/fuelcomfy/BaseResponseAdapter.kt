package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName

interface BaseResponseAdapter {
    fun writeString(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit, block: () -> Unit)
    fun writeObject(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit, block: () -> Unit)
}
