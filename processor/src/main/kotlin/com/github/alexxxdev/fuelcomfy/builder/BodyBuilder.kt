package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.SerializationAdapter
import com.github.alexxxdev.fuelcomfy.annotation.Body
import com.github.alexxxdev.fuelcomfy.javaToKotlinType
import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName

class BodyBuilder(private val serializationAdapter: SerializationAdapter) {
    private var parameters: Map<String, Parameter> = emptyMap()

    fun setParameters(parameters: Map<String, Parameter>): BodyBuilder {
        this.parameters = parameters
        return this
    }

    fun build(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        parameters.filterValues { it.annotation is Body }.forEach { entry ->
            val typeName = entry.value.parameterSpec.type
            val name = entry.value.parameterSpec.name
            when {
                typeName is ParameterizedTypeName -> when (typeName.rawType.javaToKotlinType()) {
                    List::class.asTypeName() -> buildForList(typeName, name, statement, import)
                    Set::class.asTypeName() -> buildForSet(typeName, name, statement, import)
                    Map::class.asTypeName() -> buildForMap(typeName, name, statement, import)
                    ARRAY -> buildForArray(typeName, name, statement, import)
                    else -> Unit // buildForParameterizedClass(typeName, name, statement, import)
                }
                typeName.javaToKotlinType() == Any::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                typeName.javaToKotlinType() == String::class.asTypeName() -> buildForString(typeName, name, statement, import)
                typeName.javaToKotlinType() == Int::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                typeName.javaToKotlinType() == Float::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                typeName.javaToKotlinType() == Double::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                typeName.javaToKotlinType() == Boolean::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                typeName.javaToKotlinType() == Long::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                typeName.javaToKotlinType() == Char::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                typeName.javaToKotlinType() == Short::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                typeName.javaToKotlinType() == Byte::class.asTypeName() -> buildForPrimitive(typeName, name, statement, import)
                else -> buildForClass(typeName, name, statement, import)
            }
            return@forEach
        }
    }

    private fun buildForClass(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("com.github.kittinunf.fuel.core.extensions", "jsonBody"))
        statement("\t.jsonBody(", arrayOf())
        serializationAdapter.serializationClass(typeName, name, statement, import)
        statement("\t)", arrayOf())
    }

    private fun buildForPrimitive(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        statement("\t.body(", arrayOf())
        serializationAdapter.serializationPrimitive(typeName, name, statement, import)
        statement("\t)", arrayOf())
    }

    private fun buildForString(
        typeName: TypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        statement("\t.body(", arrayOf())
        serializationAdapter.serializationString(typeName, name, statement, import)
        statement("\t)", arrayOf())
    }

    /*private fun buildForParameterizedClass(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        // CustomType<CustomType<...>, ...>
        // JSON.stringify(Data.serializer(User.serializer()), userData) for userData: Data<User>
        // JSON.stringify(Data.serializer(User.serializer().list), userData) for userData: Data<List<User>>
        // JSON.stringify(Data.serializer(User.serializer().set), userData) for userData: Data<Set<User>>
        // JSON.stringify(Data2.serializer(String.serializer(), User.serializer()), userData) for userData: Data2<String, User>
    }*/

    private fun buildForArray(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("com.github.kittinunf.fuel.core.extensions", "jsonBody"))
        statement("\t.jsonBody(", arrayOf())
        serializationAdapter.serializationArray(typeName, name, statement, import)
        statement("\t)", arrayOf())
    }

    private fun buildForMap(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("com.github.kittinunf.fuel.core.extensions", "jsonBody"))
        statement("\t.jsonBody(", arrayOf())
        serializationAdapter.serializationMap(typeName, name, statement, import)
        statement("\t)", arrayOf())
    }

    private fun buildForSet(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("com.github.kittinunf.fuel.core.extensions", "jsonBody"))
        statement("\t.jsonBody(", arrayOf())
        serializationAdapter.serializationSet(typeName, name, statement, import)
        statement("\t)", arrayOf())
    }

    private fun buildForList(
        typeName: ParameterizedTypeName,
        name: String,
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("com.github.kittinunf.fuel.core.extensions", "jsonBody"))
        statement("\t.jsonBody(", arrayOf())
        serializationAdapter.serializationList(typeName, name, statement, import)
        statement("\t)", arrayOf())
    }
}
