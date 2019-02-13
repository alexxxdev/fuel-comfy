package com.github.alexxxdev.fuellikeretrofit.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
annotation class Param(val value: String)