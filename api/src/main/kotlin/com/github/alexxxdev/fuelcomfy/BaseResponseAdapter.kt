package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

interface BaseResponseAdapter {
    fun writeAnyClass(statement: (String, Array<Any>) -> Unit)
    fun writeClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun writeList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun writeSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun writeMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun writeParameterizedClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun writeParameterizedList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun writeParameterizedSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun writeParameterizedMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
}