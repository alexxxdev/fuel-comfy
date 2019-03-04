package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.annotation.Header
import com.github.alexxxdev.fuelcomfy.annotation.Param
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement

class HeaderBuilder(private val element: ExecutableElement, private val messager: Messager) {
    private var parameters: Map<String, Parameter> = emptyMap()

    fun setParameters(parameters: Map<String, Parameter>): HeaderBuilder {
        this.parameters = parameters
        return this
    }

    fun build(block: (String, Array<Any>) -> Unit) {
        val annotationsByType = element.getAnnotationsByType(Header::class.java)
        if (annotationsByType.isNotEmpty()) {
            annotationsByType.reversedArray().forEach { header ->
                val regex = """\{\w+\}""".toRegex()
                val matchResults: Sequence<MatchResult> = regex.findAll(header.value)
                if (matchResults.count() == 0) {
                    block("\t.header(%S, %S)", arrayOf(header.name, header.value))
                } else {
                    var value = header.value
                    matchResults.forEach { result ->
                        val substring = result.value.substring(1..(result.value.length - 2))
                        parameters.filterValues { it.annotation is Param && it.annotation.value == substring }
                            .forEach { entry ->
                                val p = """${'$'}{${entry.key}}"""
                                value = value.replace(result.value, p)
                                return@forEach
                            }
                    }
                    block("\t.header(%S, %P)", arrayOf(header.name, value))
                }
            }
        }
    }
}