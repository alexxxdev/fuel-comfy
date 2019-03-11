package com.github.alexxxdev.fuelcomfy.annotation

/**
 * Body of a PUT or POST command
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
annotation class Body