package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.annotation.Param
import com.github.kittinunf.fuel.core.Method
import javax.annotation.processing.Messager
import javax.lang.model.element.ExecutableElement
import javax.tools.Diagnostic

class RequestPathBuilder(private val element: ExecutableElement, private val messager: Messager) {
    private var parameters: Map<String, Parameter> = emptyMap()
    private var method: Pair<Method, String>? = null

    fun setMethod(method: Pair<Method, String>): RequestPathBuilder {
        this.method = method
        return this
    }

    fun setParameters(parameters: Map<String, Parameter>): RequestPathBuilder {
        this.parameters = parameters
        return this
    }

    fun build(): String {
        method?.let { pair ->
            var request = pair.second
            val regex = """\{\w+\}""".toRegex()
            val matchResults: Sequence<MatchResult> = regex.findAll(request)

            matchResults.forEach { result ->
                val substring = result.value.substring(1..(result.value.length - 2))
                val filterValues =
                    parameters.filterValues { it.annotation is Param && it.annotation.value == substring }
                if (filterValues.isEmpty()) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Param \"$substring\" not found", element)
                } else {
                    val p = """${'$'}{${filterValues.entries.first().key}}"""
                    request = request.replace(result.value, p)
                }
            }
            return request
        }
        return ""
    }
}