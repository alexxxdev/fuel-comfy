package com.github.alexxxdev.fuelcomfy

import com.github.kittinunf.fuel.core.FuelManager
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun <T : Any> FuelManager.setInterface(kClass: KClass<T>): T {
    val clazz = Class.forName(kClass.qualifiedName + "Impl")
    val constructor = clazz.getConstructor()
    return constructor.newInstance() as T
}
