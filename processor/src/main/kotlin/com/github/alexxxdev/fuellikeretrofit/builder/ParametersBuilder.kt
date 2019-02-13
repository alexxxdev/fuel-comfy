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
    private val parameters: List<Pair<VariableElement, Annotation>> = element.parameters.mapNotNull {
        val annotation = listOfNotNull(
            it.getAnnotation(Param::class.java)?.let { it },
            it.getAnnotation(Body::class.java)?.let { it },
            it.getAnnotation(Query::class.java)?.let { it },
            it.getAnnotation(QueryMap::class.java)?.let { it }
        ).firstOrNull()
        if (annotation != null) {
            it to annotation
        } else {
            null
        }
    }

    fun build(): Map<String, Parameter> {
        //messager.printMessage(Diagnostic.Kind.WARNING, metadata.toString())
        return parameters.associate {
            val builder = ParameterSpec.builder(
                it.first.simpleName.toString(),
                it.first.asType().asTypeName().javaToKotlinType()
            )
            it.first.simpleName.toString() to
                    Parameter(
                        it.second,
                        builder.build()
                    )
        }
    }
}

data class Parameter(val annotation: Annotation, val parameterSpec: ParameterSpec)