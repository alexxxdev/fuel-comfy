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

object HeaderTest : BaseTest({
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

            kclass?.getFunWithParamsHeaders("json", "gzip")
            request = server.takeRequest()
        }
        test("method = GET") {
            assertEquals(Method.GET.value, request?.method)
        }
        test("path = /get") {
            assertEquals("/get", request?.path)
        }
        test("base header Accept: application/json") {
            assertEquals("application/json", request?.getHeader("Accept"))
        }
        test("base header Cache-Control: no-cache") {
            assertEquals("no-cache", request?.getHeader("Cache-Control"))
        }
        test("fun header Content-Type: application/json") {
            assertEquals("application/json", request?.getHeader("Content-Type"))
        }
        test("fun header Accept-Encoding: gzip") {
            assertEquals("gzip", request?.getHeader("Accept-Encoding"))
        }
        afterGroup {
            server.shutdown()
        }
    }

    group("POST Request with headers") {
        var request: RecordedRequest? = null
        val server = MockWebServer()

        beforeGroup {
            server.enqueue(MockResponse().setResponseCode(200))
            server.start(port)

            kclass?.postFunWithHeaders()
            request = server.takeRequest()
        }
        test("method = POST") {
            assertEquals(Method.POST.value, request?.method)
        }
        test("path = /post") {
            assertEquals("/post", request?.path)
        }
        test("base header Accept: application/json") {
            assertEquals("application/json", request?.getHeader("Accept"))
        }
        test("base header Cache-Control: no-cache") {
            assertEquals("no-cache", request?.getHeader("Cache-Control"))
        }
        test("fun header Content-Type: application/json") {
            assertEquals("application/json", request?.getHeader("Content-Type"))
        }
        test("fun header Accept-Encoding: gzip") {
            assertEquals("gzip", request?.getHeader("Accept-Encoding"))
        }
        afterGroup {
            server.shutdown()
        }
    }
})



