package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.annotation.Delete
import com.github.alexxxdev.fuelcomfy.annotation.Get
import com.github.alexxxdev.fuelcomfy.annotation.Head
import com.github.alexxxdev.fuelcomfy.annotation.Patch
import com.github.alexxxdev.fuelcomfy.annotation.Post
import com.github.alexxxdev.fuelcomfy.annotation.Put
import com.github.kittinunf.fuel.core.Method
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.tools.Diagnostic

class FunSpecBuilder(private val element: ExecutableElement, private val messager: Messager) {
    private val httpMethod: Pair<Method, String>? = listOfNotNull(
        element.getAnnotation(Get::class.java)?.let { Method.GET to it.value },
        element.getAnnotation(Post::class.java)?.let { Method.POST to it.value },
        element.getAnnotation(Put::class.java)?.let { Method.PUT to it.value },
        element.getAnnotation(Delete::class.java)?.let { Method.DELETE to it.value },
        element.getAnnotation(Patch::class.java)?.let { Method.PATCH to it.value },
        element.getAnnotation(Head::class.java)?.let { Method.HEAD to it.value }
    ).firstOrNull()
    private val func = FunSpec.builder(element.simpleName.toString()).addModifiers(KModifier.OVERRIDE)
    private val parametersBuilder = ParametersBuilder(element, messager)
    private val returnTypeNameBuilder = ReturnTypeNameBuilder(element, messager)
    private val bodyBuilder = FunSpecBodyBuilder(element, messager)
    val name = element.simpleName.toString()

    fun build(import: (ClassName) -> Unit): FunSpec? {
        if (httpMethod == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "", element)
            return null
        }

        var maybeReturnTypeNameForSuspendFunction: TypeName? = null
        var suspended = false
        val parameters: Map<String, Parameter> = parametersBuilder.build { typeName ->
            suspended = true
            func.addModifiers(KModifier.SUSPEND)
            func.returns(typeName)
            maybeReturnTypeNameForSuspendFunction = typeName
        }.apply {
            forEach { func.addParameter(it.value.parameterSpec) }
        }

        val returnType = maybeReturnTypeNameForSuspendFunction ?: returnTypeNameBuilder.build()?.apply {
            func.returns(this)
        }

        bodyBuilder.apply {
            this.method = httpMethod
            this.parameters = parameters
            this.suspended = suspended
            returnType?.let { this.returnType = it }

            build({ str, params ->
                func.addStatement(str, *params)
            }, { importClassName ->
                import(importClassName)
            })
        }

        return func.build()
    }
}