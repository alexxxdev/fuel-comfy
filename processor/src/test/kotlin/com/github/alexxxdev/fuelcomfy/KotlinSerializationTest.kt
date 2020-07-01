package com.github.alexxxdev.fuelcomfy

import com.github.alexxxdev.fuelcomfy.common.BaseTest
import com.github.alexxxdev.fuelcomfy.data.User
import com.github.alexxxdev.fuelcomfy.sources.KotlinSerializationService
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.result.Result
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.jetbrains.kotlin.cli.common.ExitCode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object KotlinSerializationTest : BaseTest({
    var kclass: KotlinSerializationService? = null
    val port = 10101

    group("Prepare class with suspend functions") {
        val resultGenerate = generateClass()
        val resultCompile = compileClass()
        kclass = LoadClass<KotlinSerializationService>()
        //test("Generate success") { assertEquals(ExitCode.OK, resultGenerate.exitCode) }
        test("Compile success") { assertTrue(resultCompile) }
        test("Load success") { assertNotNull(kclass) }
    }
    group("POST Request") {
        var response: Result<String, Exception>? = null
        val server = MockWebServer()
        beforeGroup {
            server.dispatcher = object :Dispatcher(){
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(Json.stringify(String.serializer(), "123"))
                }
            }

            server.start(port)
            FuelManager.instance.basePath = "http://localhost:$port"
            SerializationStrategy.json = Json(JsonConfiguration.Stable.copy(isLenient = true))
            response = kclass?.postStringFun("user")
        }
        test("response not null") {
            assertEquals(true, response?.get()?.isNotEmpty())
        }
        test("response = 123") {
            assertEquals("123", response?.get())
        }
        afterGroup {
            server.shutdown()
        }
    }
    group("POST Map<Int, User> Request") {
        var response: Result<Map<Int, User>, Exception>? = null
        val server = MockWebServer()
        val userMap = mapOf(1 to User(1), 2 to User(2))
        beforeGroup {
            server.dispatcher = object :Dispatcher(){
                override fun dispatch(request: RecordedRequest): MockResponse {
                    return MockResponse()
                        .setResponseCode(200)
                        .setBody(Json.stringify(MapSerializer(Int.serializer(), User.serializer()), userMap))
                }
            }

            server.start(port)

            FuelManager.instance.basePath = "http://localhost:$port"
            SerializationStrategy.json = Json(JsonConfiguration.Stable.copy(isLenient = true))
            response = kclass?.postUsersMapFun(userMap)
        }
        test("response not null") {
            assertEquals(true, response?.get()?.isNotEmpty())
        }
        test("response first = <1, User(1)>") {
            assertEquals(1, response?.get()?.entries?.first()?.key)
            assertEquals(1, response?.get()?.entries?.first()?.value?.id)
        }
        test("response last = <2, User(2)>") {
            assertEquals(2, response?.get()?.entries?.last()?.key)
            assertEquals(2, response?.get()?.entries?.last()?.value?.id)
        }
        afterGroup {
            server.shutdown()
        }
    }
})
