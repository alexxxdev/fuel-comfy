package com.github.alexxxdev.fuellikeretrofit

import com.github.kittinunf.fuel.core.FuelManager
import kotlin.reflect.KClass

fun <T : Any> FuelManager.setInterface(kClass: KClass<T>): T {
    val clazz = Class.forName(kClass.qualifiedName + "Impl")
    val constructor = clazz.getConstructor()
    return constructor.newInstance() as T
}