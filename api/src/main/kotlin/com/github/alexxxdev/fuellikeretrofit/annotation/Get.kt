package com.github.alexxxdev.fuellikeretrofit.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Get(val value: String)