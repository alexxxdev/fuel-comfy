package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import kotlinx.serialization.json.Json

const val BUILTINS = "kotlinx.serialization.builtins"

class KotlinSerializationAdapter : SerializationAdapter {
    private val kotlinxDeserializerOf = ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf")
    private val listClassName = ClassName(BUILTINS, "list")
    private val setClassName = ClassName(BUILTINS, "set")
    private val mapSerializerClassName = ClassName(BUILTINS, "MapSerializer")
    private val serializerClassName = ClassName(BUILTINS, "serializer")
    private val jsonClassName = ClassName("kotlinx.serialization.json", "Json")

    override fun deserializationAnyClass(returnType: ParameterizedTypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) = Unit

    override fun deserializationClass(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(serializerClassName)
        statement(
            "\t\t%T<%T>(%T.serializer())",
            arrayOf(
                kotlinxDeserializerOf,
                returnType.typeArguments[0],
                returnType.typeArguments[0]
            )
        )
    }

    override fun deserializationList(
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
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

    override fun deserializationSet(returnType: ParameterizedTypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
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

    override fun deserializationMap(returnType: ParameterizedTypeName, statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        import(mapSerializerClassName)
        import(serializerClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(MapSerializer(%T.serializer(), %T.serializer()), %T.json)",
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
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
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
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
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
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
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
        returnType: ParameterizedTypeName,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(mapSerializerClassName)
        import(serializerClassName)
        import(jsonClassName)
        val parameterizedType = returnType.typeArguments[0] as ParameterizedTypeName
        statement(
            "\t\t%T<%T>(%T.serializer(MapSerializer(%T.serializer(), %T.serializer())), %T.json)",
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
        import(serializerClassName)
        statement("\t\t%T.stringify(%T.serializer(), %N)", arrayOf(Json::class, typeName.javaToKotlinType(), name))
    }

    override fun serializationString(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        serializationPrimitive(typeName, name, statement, import)
    }

    override fun serializationClass(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(serializerClassName)
        serializationPrimitive(typeName, name, statement, import)
    }

    override fun serializationArray(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(listClassName)
        statement(
            "\t\t%T.stringify(%T.serializer().list, %N.toList())",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }

    override fun serializationList(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(listClassName)
        statement(
            "\t\t%T.stringify(%T.serializer().list, %N)",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }

    override fun serializationSet(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(setClassName)
        statement(
            "\t\t%T.stringify(%T.serializer().set, %N)",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }

    override fun serializationMap(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(mapSerializerClassName)
        import(serializerClassName)
        statement(
            "\t\t%T.stringify(MapSerializer(%T.serializer(), %T.serializer()), %N)",
            arrayOf(
                Json::class,
                typeName.typeArguments[0].javaToKotlinType(),
                typeName.typeArguments[1].javaToKotlinType(),
                name
            )
        )
    }
}