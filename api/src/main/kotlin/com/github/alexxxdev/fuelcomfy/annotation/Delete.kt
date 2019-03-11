package com.github.alexxxdev.fuelcomfy.annotation

/**
 * Make a DELETE request.
 *
 * value - relative or absolute path
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Delete(val value: String)