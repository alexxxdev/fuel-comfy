package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.annotation.Param
import com.github.kittinunf.fuel.core.Method

class RequestPathBuilder {
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
                parameters.filterValues { it.annotation is Param && it.annotation.value == substring }
                    .forEach { entry ->
                        val p = """${'$'}{${entry.key}}"""
                        request = request.replace(result.value, p)
                        return@forEach
                    }
            }
            return request
        }
        return ""
    }
}