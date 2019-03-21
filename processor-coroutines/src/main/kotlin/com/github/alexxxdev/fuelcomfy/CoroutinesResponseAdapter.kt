package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName

class CoroutinesResponseAdapter : BaseResponseAdapter {
    private val awaitStringResponseResultClassName = ClassName("com.github.kittinunf.fuel.coroutines", "awaitStringResponseResult")
    private val awaitObjectResponseResultClassName = ClassName("com.github.kittinunf.fuel.coroutines", "awaitObjectResponseResult")
    private val kotlinxDeserializerOfClassName = ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf")
    private val listClassName = ClassName("kotlinx.serialization", "list")
    private val setClassName = ClassName("kotlinx.serialization", "set")
    private val mapClassName = ClassName("kotlinx.serialization", "map")
    private val serializerClassName = ClassName("kotlinx.serialization", "serializer")
    private val jsonClassName = ClassName("kotlinx.serialization.json", "Json")

    override fun writeAnyClass(statement: (String, Array<Any>) -> Unit) {
        statement(
            "\t.%T()",
            arrayOf(awaitStringResponseResultClassName)
        )
    }

    override fun writeClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(jsonClassName)
        statement(
            "\t.%T(%T<%T>(%T.serializer(), json = Json.nonstrict))",
            arrayOf(
                awaitObjectResponseResultClassName,
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                returnType.typeArguments[0]
            )
        )
    }

    override fun writeList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(listClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.%T(%T<%T>(%T.serializer().list, json = Json.nonstrict))",
            arrayOf(
                awaitObjectResponseResultClassName,
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.typeArguments[0]
            )
        )
    }

    override fun writeSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(setClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.%T(%T<%T>(%T.serializer().set, json = Json.nonstrict))",
            arrayOf(
                awaitObjectResponseResultClassName,
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.typeArguments[0]
            )
        )
    }

    override fun writeMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(mapClassName)
        import(serializerClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.%T(%T<%T>((%T.serializer() to %T.serializer()).map, json = Json.nonstrict))",
            arrayOf(
                awaitObjectResponseResultClassName,
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.typeArguments[0],
                parameterizedType.typeArguments[1]
            )
        )
    }

    override fun writeParameterizedClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.%T(%T<%T>(%T.serializer(%T.serializer()), json = Json.nonstrict))",
            arrayOf(
                awaitObjectResponseResultClassName,
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                parameterizedType.typeArguments[0]
            )
        )
    }

    override fun writeParameterizedList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(listClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.%T(%T<%T>(%T.serializer(%T.serializer().list), json = Json.nonstrict))",
            arrayOf(
                awaitObjectResponseResultClassName,
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType()
            )
        )
    }

    override fun writeParameterizedSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(setClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.%T(%T<%T>(%T.serializer(%T.serializer().set), json = Json.nonstrict))",
            arrayOf(
                awaitObjectResponseResultClassName,
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType()
            )
        )
    }

    override fun writeParameterizedMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(mapClassName)
        import(serializerClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.%T(%T<%T>(%T.serializer((%T.serializer() to %T.serializer()).map), json = Json.nonstrict))",
            arrayOf(
                awaitObjectResponseResultClassName,
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[1].javaToKotlinType()
            )
        )
    }
}