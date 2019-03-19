package com.github.alexxxdev.fuelcomfy.data

import kotlinx.serialization.Serializable

@Serializable
data class Data<T>(val d: T)
