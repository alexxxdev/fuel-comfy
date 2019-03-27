package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName

class GsonAdapter : SerializationAdapter {
    private val gsonDeserializerClassName = ClassName("com.github.kittinunf.fuel.gson", "gsonDeserializer")
    private val jsonClassName = ClassName("kotlinx.serialization.json", "Json")

    override fun deserializationAnyClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) = Unit

    override fun deserializationClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationParameterizedClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationParameterizedList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationParameterizedSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        deserialization(returnType, statement, import)
    }

    override fun deserializationParameterizedMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        deserialization(returnType, statement, import)
    }

    override fun serializationPrimitive(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        statement("\t\t%N.toString()", arrayOf(name))
    }

    override fun serializationString(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        statement("\t\t%N", arrayOf(name))
    }

    override fun serializationClass(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        serialization(typeName, name, statement, import)
    }

    override fun serializationArray(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        serialization(typeName, name, statement, import)
    }

    override fun serializationList(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        serialization(typeName, name, statement, import)
    }

    override fun serializationSet(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        serialization(typeName, name, statement, import)
    }

    override fun serializationMap(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        serialization(typeName, name, statement, import)
    }

    private fun serialization(typeName: TypeName, name: String, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
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

    private fun deserialization(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
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