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
        ParameterizedTypeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationString(
        ParameterizedTypeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationClass(
        ParameterizedTypeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationArray(
        ParameterizedTypeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationList(
        ParameterizedTypeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationSet(
        ParameterizedTypeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
    fun serializationMap(
        ParameterizedTypeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    )
}