package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.javaToKotlinType
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.result.Result
import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.tools.Diagnostic

class FunSpecBodyBuilder(private val element: ExecutableElement, private val messager: Messager) {
    private val queryParametersBuilder = QueryParametersBuilder()
    private val requestPathBuilder = RequestPathBuilder()
    private val bodyBuilder = BodyBuilder()
    private val headerBuilder = HeaderBuilder(element, messager)
    var parameters: Map<String, Parameter> = emptyMap()
    var returnType: TypeName = Unit::class.asTypeName()
    var method: Pair<Method, String>? = null

    fun build(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        method?.let { method ->
            val requestPath = requestPathBuilder.setMethod(method)
                .setParameters(parameters)
                .build()
            val queryParameters = queryParametersBuilder.setParameters(parameters)
                .build()
            statement(
                "val response = %T.%N(%P$queryParameters)",
                arrayOf(
                    Fuel::class.java,
                    method.first.value.toLowerCase(),
                    requestPath
                )
            )

            headerBuilder
                .setParameters(parameters)
                .build { body, params ->
                    statement(body, params)
                }

            bodyBuilder.setParameters(parameters)
                .build({ body, params ->
                    statement(body, params)
                }, { import ->
                    import(import)
                })

            buildReturn(statement, import)
        }
    }

    private fun buildReturn(
        statement: (String, Array<Any>) -> Unit,
        import: (ClassName) -> Unit
    ) {
        if (returnType is ParameterizedTypeName) {
            when ((returnType as ParameterizedTypeName).typeArguments[0]) {
                is ClassName -> {
                    statement(
                        "\t.responseObject(%T<%T>(%T.serializer()))",
                        arrayOf(
                            ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                            // todo only List<T>
                            (returnType as ParameterizedTypeName).typeArguments[0],
                            (returnType as ParameterizedTypeName).typeArguments[0]
                        )
                    )
                }
                is ParameterizedTypeName -> {
                    when (((returnType as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).rawType.javaToKotlinType()) {
                        List::class.asTypeName() -> buildForList(statement, import)
                        Set::class.asTypeName() -> buildForSet(statement, import)
                        Map::class.asTypeName() -> buildForMap(statement, import)
                        ARRAY -> buildForArray(statement, import)
                        else -> buildForParameterizedClass(statement, import)
                    }
                }
            }
        }

        statement("return response.third", emptyArray())
    }

    private fun buildForParameterizedClass(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        val param = (returnType as ParameterizedTypeName).typeArguments[0]
        if ((param as ParameterizedTypeName).typeArguments[0] is ClassName) {
            statement(
                "\t.responseObject(%T<%T>(%T.serializer(%T.serializer())))",
                arrayOf(
                    ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                    param,
                    (param as ParameterizedTypeName).rawType.javaToKotlinType(),
                    (param as ParameterizedTypeName).typeArguments[0]
                )
            )
        } else if ((param as ParameterizedTypeName).typeArguments[0] is ParameterizedTypeName) {
            when (((param as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).rawType.javaToKotlinType()) {
                List::class.asTypeName() -> {
                    import(ClassName("kotlinx.serialization", "list"))
                    statement(
                        "\t.responseObject(%T<%T>(%T.serializer(%T.serializer().list)))",
                        arrayOf(
                            ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                            param,
                            param.rawType.javaToKotlinType(),
                            (param.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType()
                        )
                    )
                }
                Set::class.asTypeName() -> {
                    import(ClassName("kotlinx.serialization", "set"))
                    statement(
                        "\t.responseObject(%T<%T>(%T.serializer(%T.serializer().set)))",
                        arrayOf(
                            ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                            param,
                            param.rawType.javaToKotlinType(),
                            (param.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType()
                        )
                    )
                }
                Map::class.asTypeName() -> {
                    import(ClassName("kotlinx.serialization", "map"))
                    import(ClassName("kotlinx.serialization", "serializer"))
                    val map = returnType as ParameterizedTypeName
                    val param = map.typeArguments[0] as ParameterizedTypeName
                    statement(
                        "\t.responseObject(%T<%T>(%T.serializer((%T.serializer() to %T.serializer()).map)))",
                        arrayOf(
                            ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                            param,
                            param.rawType.javaToKotlinType(),
                            (param.typeArguments[0] as ParameterizedTypeName).typeArguments[0].javaToKotlinType(),
                            (param.typeArguments[0] as ParameterizedTypeName).typeArguments[1].javaToKotlinType()
                        )
                    )
                }
                ARRAY -> buildForArray(statement, import)
                else -> {
                }
            }
        }
    }

    private fun buildForArray(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        messager.printMessage(
            Diagnostic.Kind.ERROR,
            "return type only " + Result::class.java.canonicalName + "<V,E> V:" +
                    ((returnType as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).rawType.canonicalName +
                    " - only List, Set, Map, CustomClass",
            element
        )
    }

    private fun buildForMap(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        import(ClassName("kotlinx.serialization", "map"))
        import(ClassName("kotlinx.serialization", "serializer"))
        statement(
            "\t.responseObject(%T<%T>((%T.serializer() to %T.serializer()).map))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                (returnType as ParameterizedTypeName).typeArguments[0],
                ((returnType as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).typeArguments[0],
                ((returnType as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).typeArguments[1]
            )
        )
    }

    private fun buildForSet(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        import(ClassName("kotlinx.serialization", "set"))
        statement(
            "\t.responseObject(%T<%T>(%T.serializer().set))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                (returnType as ParameterizedTypeName).typeArguments[0],
                ((returnType as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).typeArguments[0]
            )
        )
    }

    private fun buildForList(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        import(ClassName("kotlinx.serialization", "list"))
        statement(
            "\t.responseObject(%T<%T>(%T.serializer().list))",
            arrayOf(
                ClassName("com.github.kittinunf.fuel.serialization", "kotlinxDeserializerOf"),
                (returnType as ParameterizedTypeName).typeArguments[0],
                ((returnType as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).typeArguments[0]
            )
        )
    }
}