package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName

interface SerializationAdapter {
    fun deserializationAnyClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationParameterizedClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationParameterizedList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationParameterizedSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationParameterizedMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)

    fun serializationPrimitive(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun serializationString(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun serializationClass(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun serializationArray(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun serializationList(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun serializationSet(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun serializationMap(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
}