package com.github.alexxxdev.fuelcomfy.annotation

/**
 * Make a POST request.
 *
 * value - relative or absolute path
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Post(val value: String)
