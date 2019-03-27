package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import kotlinx.serialization.json.Json

class KotlinSerializationAdapter : SerializationAdapter {
    private val kotlinxDeserializerOf = ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf")
    private val listClassName = ClassName("kotlinx.serialization", "list")
    private val setClassName = ClassName("kotlinx.serialization", "set")
    private val mapClassName = ClassName("kotlinx.serialization", "map")
    private val serializerClassName = ClassName("kotlinx.serialization", "serializer")
    private val jsonClassName = ClassName("kotlinx.serialization.json", "Json")

    override fun deserializationAnyClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) = Unit

    override fun deserializationClass(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(%T.serializer())",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                returnType.typeArguments[0]
            )
        )
    }

    override fun deserializationList(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(listClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(%T.serializer().list, %T.json)",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                parameterizedType.typeArguments[0],
                SerializationStrategy::class.asClassName()
            )
        )
    }

    override fun deserializationSet(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(setClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(%T.serializer().set, %T.json)",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                parameterizedType.typeArguments[0],
                SerializationStrategy::class.asClassName()
            )
        )
    }

    override fun deserializationMap(returnType: TypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        returnType as ParameterizedTypeName
        import(mapClassName)
        import(serializerClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>((%T.serializer() to %T.serializer()).map, %T.json)",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                parameterizedType.typeArguments[0],
                parameterizedType.typeArguments[1],
                SerializationStrategy::class.asClassName()
            )
        )
    }

    override fun deserializationParameterizedClass(
        returnType: TypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        returnType as ParameterizedTypeName
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(%T.serializer(%T.serializer()), %T.json)",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                parameterizedType.typeArguments[0],
                SerializationStrategy::class.asClassName()
            )
        )
    }

    override fun deserializationParameterizedList(
        returnType: TypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        returnType as ParameterizedTypeName
        import(listClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(%T.serializer(%T.serializer().list), %T.json)",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType(),
                SerializationStrategy::class.asClassName()
            )
        )
    }

    override fun deserializationParameterizedSet(
        returnType: TypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        returnType as ParameterizedTypeName
        import(setClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(%T.serializer(%T.serializer().set), %T.json)",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType(),
                SerializationStrategy::class.asClassName()
            )
        )
    }

    override fun deserializationParameterizedMap(
        returnType: TypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        returnType as ParameterizedTypeName
        import(mapClassName)
        import(serializerClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(%T.serializer((%T.serializer() to %T.serializer()).map), %T.json)",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                parameterizedType.rawType.javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType(),
                (parameterizedType.typeArguments[0] as ParameterizedTypeName).typeArguments[1].javaToKotlinType(),
                SerializationStrategy::class.asClassName()
            )
        )
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
        statement("\t\t%T.stringify(%T.serializer(), %N)", arrayOf(Json::class, typeName.javaToKotlinType(), name))
    }

    override fun serializationArray(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        typeName as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "list"))
        statement(
            "\t\t%T.stringify(%T.serializer().list, %N.toList())",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }

    override fun serializationList(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        typeName as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "list"))
        statement(
            "\t\t%T.stringify(%T.serializer().list, %N)",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }

    override fun serializationSet(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        typeName as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "set"))
        statement(
            "\t\t%T.stringify(%T.serializer().set, %N)",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }

    override fun serializationMap(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        typeName as ParameterizedTypeName
        import(ClassName("kotlinx.serialization", "map"))
        import(ClassName("kotlinx.serialization", "serializer"))
        statement(
            "\t\t%T.stringify((%T.serializer() to %T.serializer()).map, %N)",
            arrayOf(
                Json::class,
                typeName.typeArguments[0].javaToKotlinType(),
                typeName.typeArguments[1].javaToKotlinType(),
                name
            )
        )
    }
}