package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName

interface SerializationAdapter {
    fun deserializationAnyClass(returnType: ParameterizedTypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationClass(returnType: ParameterizedTypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationList(returnType: ParameterizedTypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationSet(returnType: ParameterizedTypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationMap(returnType: ParameterizedTypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit)
    fun deserializationParameterizedClass(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun deserializationParameterizedList(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun deserializationParameterizedSet(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun deserializationParameterizedMap(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )

    fun serializationPrimitive(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationString(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationClass(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationArray(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationList(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationSet(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationMap(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
}