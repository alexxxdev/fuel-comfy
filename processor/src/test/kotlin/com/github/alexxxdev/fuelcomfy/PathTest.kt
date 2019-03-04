package com.github.alexxxdev.fuelcomfy

import com.github.alexxxdev.fuelcomfy.common.BaseTest
import com.github.alexxxdev.fuelcomfy.sources.GitHubService
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.jetbrains.kotlin.cli.common.ExitCode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object PathTest : BaseTest({
    val port = 10005
    var kclass: GitHubService? = null
    FuelManager.instance.basePath = "http://localhost:$port/"

    group("Prepare class") {
        val resultGenerate = generateClass()
        val resultCompile = compileClass()
        kclass = LoadClass<GitHubService>()
        test("Generate success") { assertEquals(ExitCode.OK, resultGenerate.exitCode) }
        test("Compile success") { assertTrue(resultCompile) }
        test("Load success") { assertNotNull(kclass) }
    }
    group("GET Request with params headers") {
        var request: RecordedRequest? = null
        val server = MockWebServer()

        beforeGroup {
            server.enqueue(MockResponse().setResponseCode(200))
            server.start(port)

            kclass?.getFunWithParams("alexxxdev", "repo")
            request = server.takeRequest()
        }
        test("method = GET") {
            assertEquals(Method.GET.value, request?.method)
        }
        test("path = /get/alexxxdev/repo") {
            assertEquals("/get/alexxxdev/repo", request?.path)
        }
        afterGroup {
            server.shutdown()
        }
    }
})



