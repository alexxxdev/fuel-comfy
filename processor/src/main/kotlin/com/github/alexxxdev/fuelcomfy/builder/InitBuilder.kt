package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.annotation.Header
import com.github.kittinunf.fuel.core.FuelManager
import com.squareup.kotlinpoet.CodeBlock
import javax.annotation.processing.Messager
import javax.lang.model.element.TypeElement

class InitBuilder(private val element: TypeElement, private val messager: Messager) {
    fun build(): CodeBlock {
        val builder = CodeBlock.builder()
        val annotationsByType = element.getAnnotationsByType(Header::class.java)
        if (annotationsByType.isNotEmpty()) {
            builder.addStatement("%T.instance.baseHeaders = mapOf(", FuelManager::class.java)
            annotationsByType.reversedArray().forEachIndexed { index, header ->
                if (index == annotationsByType.lastIndex) {
                    builder.addStatement("\t%S to %S", header.name, header.value)
                } else {
                    builder.addStatement("\t%S to %S,", header.name, header.value)
                }
            }
            builder.addStatement(")")
        }
        return builder.build()
    }
}
