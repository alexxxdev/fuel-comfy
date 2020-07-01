package com.github.alexxxdev.fuelcomfy.sources

import com.github.alexxxdev.fuelcomfy.GsonAdapter
import com.github.alexxxdev.fuelcomfy.annotation.Body
import com.github.alexxxdev.fuelcomfy.annotation.FuelInterface
import com.github.alexxxdev.fuelcomfy.annotation.Post
import com.github.alexxxdev.fuelcomfy.data.Data
import com.github.alexxxdev.fuelcomfy.data.User
import com.github.kittinunf.result.Result

@FuelInterface(GsonAdapter::class)
interface GsonSerializationService {

    @Post("/post")
    fun postIntFun(@Body user: Int): Result<Int, Exception>

    @Post("/post")
    fun postStringFun(@Body user: String): Result<String, Exception>

    @Post("/post")
    fun postUserFun(@Body user: User): Result<User, Exception>

    @Post("/post")
    fun postUsersListFun(@Body users: List<User>): Result<List<User>, Exception>

    @Post("/post")
    fun postUsersArrayFun(@Body users: Array<User>): Result<List<User>, Exception>

    @Post("/post")
    fun postUsersSetFun(@Body users: Set<User>): Result<Set<User>, Exception>

    @Post("/post")
    fun postUsersMapFun(@Body users: Map<Int, User>): Result<Map<Int, User>, Exception>

    @Post("/post")
    fun postDataUsersFun(@Body user: User): Result<Data<User>, Exception>

    @Post("/post")
    fun postDataUsersListFun(@Body user: User): Result<Data<List<User>>, Exception>

    @Post("/post")
    fun postDataUsersArrayFun(@Body user: User): Result<Data<List<User>>, Exception>

    @Post("/post")
    fun postDataUsersSetFun(@Body user: User): Result<Data<Set<User>>, Exception>

    @Post("/post")
    fun postDataUsersMapFun(@Body user: User): Result<Data<Map<Int, User>>, Exception>
}
