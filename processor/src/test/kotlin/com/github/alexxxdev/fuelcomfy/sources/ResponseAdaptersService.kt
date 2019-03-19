package com.github.alexxxdev.fuelcomfy.sources

import com.github.alexxxdev.fuelcomfy.annotation.Delete
import com.github.alexxxdev.fuelcomfy.annotation.FuelInterface
import com.github.alexxxdev.fuelcomfy.annotation.Get
import com.github.alexxxdev.fuelcomfy.annotation.Head
import com.github.alexxxdev.fuelcomfy.annotation.Patch
import com.github.alexxxdev.fuelcomfy.annotation.Post
import com.github.alexxxdev.fuelcomfy.annotation.Put
import com.github.alexxxdev.fuelcomfy.data.Data
import com.github.alexxxdev.fuelcomfy.data.User
import com.github.kittinunf.result.Result

@FuelInterface
interface ResponseAdaptersService {
    @Get("/get")
    suspend fun getFunSuspend(): Result<Any, Exception>

    @Get("/get")
    suspend fun getUserFunSuspend(): Result<User, Exception>

    @Get("/get")
    suspend fun getUserListFunSuspend(): Result<List<User>, Exception>

    @Get("/get")
    suspend fun getUserSetFunSuspend(): Result<Set<User>, Exception>

    @Get("/get")
    suspend fun getUserMapFunSuspend(): Result<Map<Int, User>, Exception>

    @Get("/get")
    suspend fun getUserDataFunSuspend(): Result<Data<User>, Exception>

    @Get("/get")
    suspend fun getUserListDataFunSuspend(): Result<Data<List<User>>, Exception>

    @Get("/get")
    suspend fun getUserSetDataFunSuspend(): Result<Data<Set<User>>, Exception>

    @Get("/get")
    suspend fun getUserMapDataFunSuspend(): Result<Data<Map<Int, User>>, Exception>

    @Get("/get")
    fun getFun(): Result<Any, Exception>

    @Get("/get")
    fun getUserFun(): Result<User, Exception>

    @Get("/get")
    fun getUserListFun(): Result<List<User>, Exception>

    @Get("/get")
    fun getUserSetFun(): Result<Set<User>, Exception>

    @Get("/get")
    fun getUserMapFun(): Result<Map<Int, User>, Exception>

    @Get("/get")
    fun getUserDataFun(): Result<Data<User>, Exception>

    @Get("/get")
    fun getUserListDataFun(): Result<Data<List<User>>, Exception>

    @Get("/get")
    fun getUserSetDataFun(): Result<Data<Set<User>>, Exception>

    @Get("/get")
    fun getUserMapDataFun(): Result<Data<Map<Int, User>>, Exception>

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
