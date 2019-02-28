package com.github.alexxxdev.fuelcomfy.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Delete(val value: String)