package com.github.alexxxdev.fuellikeretrofit.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@MustBeDocumented
annotation class Head(val value: String)