package com.github.alexxxdev.fuellikeretrofit.builder

import com.github.alexxxdev.fuellikeretrofit.annotation.Header
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement

class HeaderBuilder(private val element: ExecutableElement, private val messager: Messager) {

    fun build(block: (String, Array<Any>) -> Unit) {
        val annotationsByType = element.getAnnotationsByType(Header::class.java)
        if (annotationsByType.isNotEmpty()) {
            annotationsByType.reversedArray().forEach {
                block("\t.header(%S, %S)", arrayOf(it.name, it.value))
            }
        }
    }
}