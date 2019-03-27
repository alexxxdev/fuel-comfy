package com.github.alexxxdev.fuelcomfy.sources

import com.github.alexxxdev.fuelcomfy.KotlinSerializationAdapter
import com.github.alexxxdev.fuelcomfy.annotation.Delete
import com.github.alexxxdev.fuelcomfy.annotation.FuelInterface
import com.github.alexxxdev.fuelcomfy.annotation.Get
import com.github.alexxxdev.fuelcomfy.annotation.Head
import com.github.alexxxdev.fuelcomfy.annotation.Header
import com.github.alexxxdev.fuelcomfy.annotation.Param
import com.github.alexxxdev.fuelcomfy.annotation.Patch
import com.github.alexxxdev.fuelcomfy.annotation.Post
import com.github.alexxxdev.fuelcomfy.annotation.Put
import com.github.kittinunf.result.Result

@FuelInterface(KotlinSerializationAdapter::class)
@Header("Accept", "application/json")
@Header("Cache-Control", "no-cache")
interface GitHubService {
    @Get("/get")
    fun getFun(): Result<Any, Exception>

    @Post("/post")
    fun postFun(): Result<Any, Exception>

    @Put("/put")
    fun putFun(): Result<Any, Exception>

    @Delete("/delete")
    fun deleteFun(): Result<Any, Exception>

    @Patch("/patch")
    fun patchFun(): Result<Any, Exception>

    @Head("/head")
    fun headFun(): Result<Any, Exception>

    @Get("/get")
    @Header("Content-Type", "application/{type}")
    @Header("Accept-Encoding", "{encoding}")
    fun getFunWithParamsHeaders(@Param("type") type: String, @Param("encoding") encoding: String): Result<Any, Exception>

    @Post("/post")
    @Header("Content-Type", "application/json")
    @Header("Accept-Encoding", "gzip")
    fun postFunWithHeaders(): Result<Any, Exception>

    @Get("/get/{user}/{resource}")
    fun getFunWithParams(@Param("user") user: String, @Param("resource") resource: String): Result<Any, Exception>
}
