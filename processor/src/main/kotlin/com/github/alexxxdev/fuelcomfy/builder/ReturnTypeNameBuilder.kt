package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.javaToKotlinType
import com.github.kittinunf.result.Result
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.tools.Diagnostic

class ReturnTypeNameBuilder(private val element: ExecutableElement, private val messager: Messager) {
    private val returnType: TypeName = element.returnType.asTypeName().javaToKotlinType()

    fun build(): TypeName? {
        return if (returnType is ParameterizedTypeName && returnType.rawType.canonicalName == Result::class.java.canonicalName) {
            returnType
        } else {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                "return type only " + Result::class.java.canonicalName + "<V,E> " + (returnType as ClassName).canonicalName,
                element
            )
            null
        }
    }
}