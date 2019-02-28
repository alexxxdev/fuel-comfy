package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.annotation.Body
import com.github.alexxxdev.fuelcomfy.javaToKotlinType
import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import kotlinx.serialization.json.Json

class BodyBuilder {
    private var parameters: Map<String, Parameter> = emptyMap()

    fun setParameters(parameters: Map<String, Parameter>): BodyBuilder {
        this.parameters = parameters
        return this
    }

    fun build(body: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        parameters.filterValues { it.annotation is Body }.forEach { entry ->
            val typeName = entry.value.parameterSpec.type
            val name = entry.value.parameterSpec.name
            when {
                typeName is ParameterizedTypeName -> when (typeName.rawType.javaToKotlinType()) {
                    List::class.asTypeName() -> buildForList(typeName, name, body, import)
                    Set::class.asTypeName() -> buildForSet(typeName, name, body, import)
                    Map::class.asTypeName() -> buildForMap(typeName, name, body, import)
                    ARRAY -> buildForArray(typeName, name, body, import)
                    else -> buildForParameterizedClass(typeName, name, body, import)
                }
                typeName.javaToKotlinType() == String::class.asTypeName() -> buildForString(
                    typeName,
                    name,
                    body,
                    import
                )
                typeName.javaClass.isPrimitive -> buildForPrimitive(typeName, name, body, import)
                else -> buildForClass(typeName, name, body, import)
            }
            return@forEach
        }
    }

    private fun buildForClass(
        typeName: TypeName,
        name: String,
        body: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        body("\t.body(%T.stringify(%T.serializer(), %N))", arrayOf(Json::class, typeName.javaToKotlinType(), name))
    }

    private fun buildForPrimitive(
        typeName: TypeName,
        name: String,
        body: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        body("\t.body(%N.toString())", arrayOf(name))
    }

    private fun buildForString(
        typeName: TypeName,
        name: String,
        body: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        body(
            "\t.body(%N)",
            arrayOf(name)
        )
    }

    private fun buildForParameterizedClass(
        typeName: ParameterizedTypeName,
        name: String,
        body: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        // CustomType<CustomType<...>, ...>
        // JSON.stringify(Data.serializer(User.serializer()), userData) for userData: Data<User>
        // JSON.stringify(Data.serializer(User.serializer().list), userData) for userData: Data<List<User>>
        // JSON.stringify(Data.serializer(User.serializer().set), userData) for userData: Data<Set<User>>
        // JSON.stringify(Data2.serializer(String.serializer(), User.serializer()), userData) for userData: Data2<String, User>
    }

    private fun buildForArray(
        typeName: ParameterizedTypeName,
        name: String,
        body: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("kotlinx.serialization", "list"))
        body(
            "\t.body(%T.stringify(%T.serializer().list, %N.toList()))",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }

    private fun buildForMap(
        typeName: ParameterizedTypeName,
        name: String,
        body: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("kotlinx.serialization", "map"))
        import(ClassName("kotlinx.serialization", "serializer"))
        body(
            "\t.body(%T.stringify((%T.serializer() to %T.serializer()).map, %N))",
            arrayOf(
                Json::class,
                typeName.typeArguments[0].javaToKotlinType(),
                typeName.typeArguments[1].javaToKotlinType(),
                name
            )
        )
    }

    private fun buildForSet(
        typeName: ParameterizedTypeName,
        name: String,
        body: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("kotlinx.serialization", "set"))
        body(
            "\t.body(%T.stringify(%T.serializer().set, %N))",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }

    private fun buildForList(
        typeName: ParameterizedTypeName,
        name: String,
        body: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        import(ClassName("kotlinx.serialization", "list"))
        body(
            "\t.body(%T.stringify(%T.serializer().list, %N))",
            arrayOf(Json::class, typeName.typeArguments[0].javaToKotlinType(), name)
        )
    }
}