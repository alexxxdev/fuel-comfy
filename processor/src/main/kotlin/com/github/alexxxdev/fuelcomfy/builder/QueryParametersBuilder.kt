package com.github.alexxxdev.fuelcomfy.builder

import com.github.alexxxdev.fuelcomfy.annotation.Query
import com.github.alexxxdev.fuelcomfy.annotation.QueryMap

class QueryParametersBuilder {
    private var parameters: Map<String, Parameter> = emptyMap()

    fun build(): String {
        var query = ""
        val pair = buildQueryMap(query)
        query = pair.second
        query = buildQuery(pair.first, query)
        return query
    }

    private fun buildQuery(
        firstMap: Boolean,
        query: String
    ): String {
        var query1 = query
        var firstList = true
        parameters.filterValues { it.annotation is Query }.map { it.value }.forEach { param ->
            param.annotation as Query
            if (!firstMap) {
                query1 += ".plus(" + "\"${param.annotation.value}\" to ${param.parameterSpec.name}" + ")"
            } else if (firstList) {
                query1 += ", listOf(" + "\"${param.annotation.value}\" to ${param.parameterSpec.name}" + ""
                firstList = false
            } else {
                query1 += ", \"${param.annotation.value}\" to ${param.parameterSpec.name}" + ""
            }
        }
        if (firstMap && !firstList) query1 += ")"
        return query1
    }

    private fun buildQueryMap(query: String): Pair<Boolean, String> {
        var firstMap = true
        var query1 = query
        parameters.filterValues { it.annotation is QueryMap }
            .forEach { entry ->
                if (firstMap) {
                    query1 = ", ${entry.key}.toList()"
                    firstMap = false
                } else {
                    query1 += ".plus(${entry.key}.toList())"
                }
            }
        return Pair(firstMap, query1)
    }

    fun setParameters(parameters: Map<String, Parameter>): QueryParametersBuilder {
        this.parameters = parameters
        return this
    }
}
