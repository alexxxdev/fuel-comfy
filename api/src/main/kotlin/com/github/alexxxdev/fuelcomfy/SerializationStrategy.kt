package com.github.alexxxdev.fuelcomfy

import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json

object SerializationStrategy {
    @UnstableDefault
    var json = Json.nonstrict
}
