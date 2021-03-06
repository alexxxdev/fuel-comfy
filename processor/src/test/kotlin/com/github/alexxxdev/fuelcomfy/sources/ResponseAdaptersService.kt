package com.github.alexxxdev.fuelcomfy.sources

import com.github.alexxxdev.fuelcomfy.KotlinSerializationAdapter
import com.github.alexxxdev.fuelcomfy.annotation.FuelInterface
import com.github.alexxxdev.fuelcomfy.annotation.Get
import com.github.alexxxdev.fuelcomfy.data.Data
import com.github.alexxxdev.fuelcomfy.data.User
import com.github.kittinunf.result.Result

@FuelInterface(KotlinSerializationAdapter::class)
interface ResponseAdaptersService {

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
}
