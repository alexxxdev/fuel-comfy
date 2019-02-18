package com.github.alexxxdev.fuellikeretrofit.builder

import com.github.alexxxdev.fuellikeretrofit.annotation.Body
import com.github.alexxxdev.fuellikeretrofit.annotation.Param
import com.github.alexxxdev.fuellikeretrofit.annotation.Query
import com.github.alexxxdev.fuellikeretrofit.annotation.QueryMap
import com.github.alexxxdev.fuellikeretrofit.javaToKotlinType
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.VariableElement

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

    fun build(): Map<String, Parameter> {
        // messager.printMessage(Diagnostic.Kind.WARNING, metadata.toString())
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