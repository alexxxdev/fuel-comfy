package com.github.alexxxdev.fuelcomfy.annotation

/**
 * A template parameter that can be applied to a Map that contains query parameters, where the keys
 * are Strings that are the parameter names and the values are the parameter values.
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
annotation class QueryMap