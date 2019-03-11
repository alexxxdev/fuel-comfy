package com.github.alexxxdev.fuelcomfy.annotation

/**
 * Query parameter appended to the URL.
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
annotation class Query(val value: String)