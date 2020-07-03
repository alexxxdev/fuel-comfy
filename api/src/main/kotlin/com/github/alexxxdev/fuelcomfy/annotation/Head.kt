package com.github.alexxxdev.fuelcomfy.annotation

/**
 * Make a HEAD request.
 *
 * value - relative or absolute path
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Head(val value: String)
