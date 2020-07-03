package com.github.alexxxdev.fuelcomfy.annotation

/**
 * Make a GET request.
 *
 * value - relative or absolute path
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Get(val value: String)
