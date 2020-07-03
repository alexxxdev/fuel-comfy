package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName

class GsonAdapter : SerializationAdapter {
    private val gsonDeserializerClassName = ClassName("com.github.kittinunf.fuel.gson", "gsonDeserializer")
    private val jsonClassName = ClassName("kotlinx.serialization.json", "Json")

    override fun deserializationAnyClass(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) = Unit

    override fun deserializationClass(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationList(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationSet(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationMap(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationParameterizedClass(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationParameterizedList(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationParameterizedSet(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationParameterizedMap(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        deserialization(returnType, statement, import)
    }

    override fun serializationPrimitive(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        statement("\t\t%N.toString()", arrayOf(name))
    }

    override fun serializationString(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        statement("\t\t%N", arrayOf(name))
    }

    override fun serializationClass(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        serialization(typeName, name, statement)
    }

    override fun serializationArray(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        serialization(typeName, name, statement)
    }

    override fun serializationList(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        serialization(typeName, name, statement)
    }

    override fun serializationSet(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        serialization(typeName, name, statement)
    }

    override fun serializationMap(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        serialization(typeName, name, statement)
    }

    private fun serialization(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit) {
        statement(
            "\t\t%T().toJson(%N, object : %T<%T>() {}.type)",
            arrayOf(
                ClassName("com.google.gson", "Gson"),
                name,
                ClassName("com.google.gson.reflect", "TypeToken"),
                typeName.javaToKotlinType()
            )
        )
    }

    private fun deserialization(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(jsonClassName)
        statement(
            "\t\t%T<%T>()",
            arrayOf(
                gsonDeserializerClassName,
                returnType.typeArguments[0]
            )
        )
    }
}
