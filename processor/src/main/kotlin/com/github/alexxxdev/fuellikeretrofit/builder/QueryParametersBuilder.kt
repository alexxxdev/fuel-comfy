package com.github.alexxxdev.fuellikeretrofit.builder

import com.github.alexxxdev.fuellikeretrofit.annotation.Query
import com.github.alexxxdev.fuellikeretrofit.annotation.QueryMap

class QueryParametersBuilder {
    private var parameters: Map<String, Parameter> = emptyMap()

    fun build(): String {
        var query = ""
        var firstMap = true
        var firstList = true
        parameters.filterValues { it.annotation is QueryMap }
            .forEach {
                if (firstMap) {
                    query = ", ${it.key}.toList()"
                    firstMap = false
                } else {
                    query += ".plus(${it.key}.toList())"
                }
            }
        parameters.filterValues { it.annotation is Query }.map { it.value }.forEach {
            it.annotation as Query
            if (!firstMap) {
                query += ".plus(" + "\"${it.annotation.value}\" to ${it.parameterSpec.name}" + ")"
            } else if (firstList) {
                query += ", listOf(" + "\"${it.annotation.value}\" to ${it.parameterSpec.name}" + ""
                firstList = false
            } else {
                query += ", \"${it.annotation.value}\" to ${it.parameterSpec.name}" + ""
            }
        }
        if (firstMap && !firstList) query += ")"
        return query
    }

    fun setParameters(parameters: Map<String, Parameter>): QueryParametersBuilder {
        this.parameters = parameters
        return this
    }
}