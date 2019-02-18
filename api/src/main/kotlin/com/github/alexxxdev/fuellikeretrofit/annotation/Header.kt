package com.github.alexxxdev.fuellikeretrofit.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@java.lang.annotation.Repeatable(Headers::class)
@Repeatable
@MustBeDocumented
annotation class Header(val name: String, val value: String)

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class Headers(vararg val value: Header)
