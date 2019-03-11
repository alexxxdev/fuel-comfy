package com.github.alexxxdev.fuelcomfy.annotation

/**
 * Make a PUT request.
 *
 * value - relative or absolute path
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Put(val value: String)