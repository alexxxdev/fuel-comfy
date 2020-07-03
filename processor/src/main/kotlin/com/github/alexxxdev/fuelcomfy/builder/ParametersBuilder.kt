package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.annotation.Body
import com.github.alexxxdev.fuelcomfy.annotation.Param
import com.github.alexxxdev.fuelcomfy.annotation.Query
import com.github.alexxxdev.fuelcomfy.annotation.QueryMap
import com.github.alexxxdev.fuelcomfy.javaToKotlinType
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement
import javax.tools.Diagnostic
import kotlin.coroutines.Continuation
import org.jetbrains.annotations.NotNull

class ParametersBuilder(private val element: ExecutableElement, private val messager: Messager) {
    private val parameters: List<Pair<VariableElement, Annotation>> = element.parameters.mapNotNull { element ->
        val annotation = listOfNotNull(
            element.getAnnotation(Param::class.java),
            element.getAnnotation(Body::class.java),
            element.getAnnotation(Query::class.java),
            element.getAnnotation(QueryMap::class.java)
        ).firstOrNull()
        if (annotation != null) {
            element to annotation
        } else {
            null
        }
    }

    fun build(maybeReturnTypeNameForSuspendFunction: (TypeName) -> Unit): Map<String, Parameter> {

        element.parameters.forEach { element ->
            element.getAnnotation(NotNull::class.java)?.let {
                val asTypeName = element.asType().asTypeName()
                if (asTypeName is ParameterizedTypeName && asTypeName.rawType.canonicalName == Continuation::class.qualifiedName) {
                    val parameterizedTypeName =
                        (asTypeName.typeArguments[0] as WildcardTypeName).inTypes[0] as ParameterizedTypeName

                    messager.printMessage(Diagnostic.Kind.WARNING, parameterizedTypeName.typeArguments[1].javaToKotlinType().toString())

                    val parameterizedBy =
                        parameterizedTypeName.rawType.parameterizedBy(
                            when {
                                parameterizedTypeName.typeArguments[0] is ClassName -> {
                                    parameterizedTypeName.typeArguments[0].javaToKotlinType()
                                }
                                parameterizedTypeName.typeArguments[0] is WildcardTypeName -> {
                                    (parameterizedTypeName.typeArguments[0] as WildcardTypeName).outTypes[0].javaToKotlinType()
                                }
                                else -> (parameterizedTypeName.typeArguments[0] as ParameterizedTypeName).javaToKotlinType()
                            },
                            (parameterizedTypeName.typeArguments[1] as WildcardTypeName).outTypes[0].javaToKotlinType()
                        )

                    maybeReturnTypeNameForSuspendFunction.invoke(parameterizedBy.javaToKotlinType())
                }
            }
        }

        return parameters.associate { pair ->
            val builder = ParameterSpec.builder(
                pair.first.simpleName.toString(),
                pair.first.asType().asTypeName().javaToKotlinType()
            )
            pair.first.simpleName.toString() to
                    Parameter(
                        pair.second,
                        builder.build()
                    )
        }
    }
}

data class Parameter(val annotation: Annotation, val parameterSpec: ParameterSpec)
