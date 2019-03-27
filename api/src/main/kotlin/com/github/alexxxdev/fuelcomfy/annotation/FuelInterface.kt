package com.github.alexxxdev.fuelcomfy.annotation

import com.github.alexxxdev.fuelcomfy.SerializationAdapter
import kotlin.reflect.KClass

/**
 * Defines the interface for code generation
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class FuelInterface(val value: KClass<out SerializationAdapter>)
