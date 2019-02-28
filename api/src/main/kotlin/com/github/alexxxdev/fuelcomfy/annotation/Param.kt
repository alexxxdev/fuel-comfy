package com.github.alexxxdev.fuelcomfy.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
annotation class Param(val value: String)