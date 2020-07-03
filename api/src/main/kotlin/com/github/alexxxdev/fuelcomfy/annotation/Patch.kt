package com.github.alexxxdev.fuelcomfy.annotation

/**
 * Make a PATCH request.
 *
 * value - relative or absolute path
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Patch(val value: String)
