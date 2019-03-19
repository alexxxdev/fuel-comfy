package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName

class FuelResponseAdapter : BaseResponseAdapter {
    override fun writeAnyClass(statement: (String, Array<Any>) -> Unit) {
        statement("\t.response()", arrayOf())
    }

    override fun writeClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit) {
        returnType as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer()))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                returnType.typeArguments[0],
                returnType.typeArguments[0]
            )
        )
    }

    override fun writeList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "list"))
        statement(
            "\t.responseObject(%T<%T>(%T.serializer().list))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                returnType.typeArguments[0],
                (returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0]
            )
        )
    }

    override fun writeSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "set"))
        statement(
            "\t.responseObject(%T<%T>(%T.serializer().set))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                returnType.typeArguments[0],
                (returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0]
            )
        )
    }

    override fun writeMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "map"))
        import(ClassName("kotlinx.serialization", "serializer"))
        statement(
            "\t.responseObject(%T<%T>((%T.serializer() to %T.serializer()).map))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                returnType.typeArguments[0],
                (returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0],
                (returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[1]
            )
        )
    }

    override fun writeParameterizedClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit) {
        returnType as ParameterizedTypeName
        statement(
            "\t.responseObject(%T<%T>(%T.serializer(%T.serializer())))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                returnType.typeArguments[0],
                (returnType.typeArguments[0] as ParameterizedTypeName).rawType.javaToKotlinType(),
                (returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0]
            )
        )
    }

    override fun writeParameterizedList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "list"))
        statement(
            "\t.responseObject(%T<%T>(%T.serializer(%T.serializer().list)))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                returnType.typeArguments[0],
                (returnType.typeArguments[0] as ParameterizedTypeName).rawType.javaToKotlinType(),
                ((returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType()
            )
        )
    }

    override fun writeParameterizedSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "set"))
        statement(
            "\t.responseObject(%T<%T>(%T.serializer(%T.serializer().set)))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                returnType.typeArguments[0],
                (returnType.typeArguments[0] as ParameterizedTypeName).rawType.javaToKotlinType(),
                ((returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType()
            )
        )
    }

    override fun writeParameterizedMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "map"))
        import(ClassName("kotlinx.serialization", "serializer"))
        statement(
            "\t.responseObject(%T<%T>(%T.serializer((%T.serializer() to %T.serializer()).map)))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                returnType.typeArguments[0],
                (returnType.typeArguments[0] as ParameterizedTypeName).rawType.javaToKotlinType(),
                ((returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType(),
                ((returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).typeArguments[1].javaToKotlinType()
            )
        )
    }
}