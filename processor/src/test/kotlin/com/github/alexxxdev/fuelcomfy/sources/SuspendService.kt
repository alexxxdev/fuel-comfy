package com.github.alexxxdev.fuelcomfy.sources

import com.github.alexxxdev.fuelcomfy.annotation.Delete
import com.github.alexxxdev.fuelcomfy.annotation.FuelInterface
import com.github.alexxxdev.fuelcomfy.annotation.Get
import com.github.alexxxdev.fuelcomfy.annotation.Head
import com.github.alexxxdev.fuelcomfy.annotation.Patch
import com.github.alexxxdev.fuelcomfy.annotation.Post
import com.github.alexxxdev.fuelcomfy.annotation.Put
import com.github.kittinunf.result.Result

@FuelInterface
interface SuspendService {
    @Get("/get")
    suspend fun getFun(): Result<Any, Exception>

    @Post("/post")
    suspend fun postFun(): Result<Any, Exception>

    @Put("/put")
    suspend fun putFun(): Result<Any, Exception>

    @Delete("/delete")
    suspend fun deleteFun(): Result<Any, Exception>

    @Patch("/patch")
    suspend fun patchFun(): Result<Any, Exception>

    @Head("/head")
    suspend fun headFun(): Result<Any, Exception>
}
