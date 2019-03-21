package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName

class FuelResponseAdapter : BaseResponseAdapter {
    private val kotlinxDeserializerOfClassName = ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf")
    private val listClassName = ClassName("kotlinx.serialization", "list")
    private val setClassName = ClassName("kotlinx.serialization", "set")
    private val mapClassName = ClassName("kotlinx.serialization", "map")
    private val serializerClassName = ClassName("kotlinx.serialization", "serializer")

    override fun writeAnyClass(statement: (String, Array<Any>) -> Unit) {
        statement("\t.response()", arrayOf())
    }

    override fun writeClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer()))",
            arrayOf(
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                returnType.typeArguments[0]
            )
        )
    }

    override fun writeList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(listClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer().list))",
            arrayOf(
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.typeArguments[0]
            )
        )
    }

    override fun writeSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(setClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer().set))",
            arrayOf(
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
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>((%T.serializer() to %T.serializer()).map))",
            arrayOf(
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.typeArguments[0],
                parameterizedType.typeArguments[1]
            )
        )
    }

    override fun writeParameterizedClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer(%T.serializer())))",
            arrayOf(
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
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer(%T.serializer().list)))",
            arrayOf(
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
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer(%T.serializer().set)))",
            arrayOf(
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
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer((%T.serializer() to %T.serializer()).map)))",
            arrayOf(
                kotlinxDeserializerOfClassName,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[1].javaToKotlinType()
            )
        )
    }
}