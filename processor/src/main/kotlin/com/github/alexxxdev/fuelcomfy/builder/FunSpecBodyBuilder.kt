package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.BaseResponseAdapter
import com.github.alexxxdev.fuelcomfy.CoroutinesResponseAdapter
import com.github.alexxxdev.fuelcomfy.FuelResponseAdapter
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
    private val requestPathBuilder = RequestPathBuilder(element, messager)
    private val bodyBuilder = BodyBuilder()
    private val headerBuilder = HeaderBuilder(element, messager)
    var parameters: Map<String, Parameter> = emptyMap()
    var returnType: TypeName = Unit::class.asTypeName()
    var method: Pair<Method, String>? = null
    var suspended: Boolean = false
    private val responseAdapter: BaseResponseAdapter by lazy { if (suspended) CoroutinesResponseAdapter() else FuelResponseAdapter() }

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
                    if (((returnType as ParameterizedTypeName).typeArguments[0] as ClassName).simpleName == Any::class.simpleName) {
                        responseAdapter.writeAnyClass(statement)
                    } else {
                        responseAdapter.writeClass(returnType, statement)
                    }
                }
                is ParameterizedTypeName -> {
                    when (((returnType as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).rawType.javaToKotlinType()) {
                        List::class.asTypeName() -> responseAdapter.writeList(returnType, statement, import)
                        Set::class.asTypeName() -> responseAdapter.writeSet(returnType, statement, import)
                        Map::class.asTypeName() -> responseAdapter.writeMap(returnType, statement, import)
                        ARRAY -> notSupport()
                        else -> buildForParameterizedClass(statement, import)
                    }
                }
                else -> notSupport()
            }
        }

        statement("return response.third", emptyArray())
    }

    private fun notSupport() {
        messager.printMessage(
            Diagnostic.Kind.ERROR,
            "return type only " + Result::class.java.canonicalName + "<V,E> V:" +
                    returnType.javaToKotlinType() +
                    " - only List, Set, Map, CustomClass",
            element
        )
    }

    private fun buildForParameterizedClass(statement: (String, Array<Any>) -> Unit, import: (ClassName) -> Unit) {
        (returnType as? ParameterizedTypeName)?.let { returnType ->
            if ((returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0] is ClassName) {
                responseAdapter.writeParameterizedClass(returnType, statement)
            } else if ((returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0] is ParameterizedTypeName) {
                when (((returnType.typeArguments[0] as ParameterizedTypeName).typeArguments[0] as ParameterizedTypeName).rawType.javaToKotlinType()) {
                    List::class.asTypeName() -> responseAdapter.writeParameterizedList(returnType, statement, import)
                    Set::class.asTypeName() -> responseAdapter.writeParameterizedSet(returnType, statement, import)
                    Map::class.asTypeName() -> responseAdapter.writeParameterizedMap(returnType, statement, import)
                    else -> notSupport()
                }
            }
        }
    }
}