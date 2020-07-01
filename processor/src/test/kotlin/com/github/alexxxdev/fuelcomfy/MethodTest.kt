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

object MethodTest : BaseTest({
    val port = 10101
    var kclass: GitHubService? = null
    FuelManager.instance.basePath = "http://localhost:$port/"

    group("Prepare class") {
        val resultGenerate = generateClass()
        val resultCompile = compileClass()
        kclass = LoadClass<GitHubService>()
        //test("Generate success") { assertEquals(ExitCode.OK, resultGenerate.exitCode) }
        test("Compile success") { assertTrue(resultCompile) }
        test("Load success") { assertNotNull(kclass) }
    }
    group("GET Request") {
        var request: RecordedRequest? = null
        val server = MockWebServer()

        beforeGroup {
            server.enqueue(MockResponse().setResponseCode(200))
            server.start(port)

            kclass?.getFun()
            request = server.takeRequest()
        }
        test("method = GET") {
            assertEquals(Method.GET.value, request?.method)
        }
        test("path = /get") {
            assertEquals("/get", request?.path)
        }
        afterGroup {
            server.shutdown()
        }
    }
    group("POST Request") {
        var request: RecordedRequest? = null
        val server = MockWebServer()

        beforeGroup {
            server.enqueue(MockResponse().setResponseCode(200))
            server.start(port)

            kclass?.postFun()
            request = server.takeRequest()
        }
        test("method = POST") {
            assertEquals(Method.POST.value, request?.method)
        }
        test("path = /post") {
            assertEquals("/post", request?.path)
        }
        afterGroup {
            server.shutdown()
        }
    }
    group("PUT Request") {
        var request: RecordedRequest? = null
        val server = MockWebServer()

        beforeGroup {
            server.enqueue(MockResponse().setResponseCode(200))
            server.start(port)

            kclass?.putFun()
            request = server.takeRequest()
        }
        test("method = PUT") {
            assertEquals(Method.PUT.value, request?.method)
        }
        test("path = /put") {
            assertEquals("/put", request?.path)
        }
        afterGroup {
            server.shutdown()
        }
    }
    group("DELETE Request") {
        var request: RecordedRequest? = null
        val server = MockWebServer()

        beforeGroup {
            server.enqueue(MockResponse().setResponseCode(200))
            server.start(port)

            kclass?.deleteFun()
            request = server.takeRequest()
        }
        test("method = DELETE") {
            assertEquals(Method.DELETE.value, request?.method)
        }
        test("path = /delete") {
            assertEquals("/delete", request?.path)
        }
        afterGroup {
            server.shutdown()
        }
    }
    group("PATCH Request") {
        var request: RecordedRequest? = null
        val server = MockWebServer()

        beforeGroup {
            server.enqueue(MockResponse().setResponseCode(200))
            server.start(port)

            kclass?.patchFun()
            request = server.takeRequest()
        }
        test("method = POST") {
            assertEquals(Method.POST.value, request?.method)
        }
        test("path = /patch") {
            assertEquals("/patch", request?.path)
        }
        test("header X-HTTP-Method-Override: PATCH") {
            assertEquals(Method.PATCH.value, request?.getHeader("X-HTTP-Method-Override"))
        }
        afterGroup {
            server.shutdown()
        }
    }
    group("HEAD Request") {
        var request: RecordedRequest? = null
        val server = MockWebServer()

        beforeGroup {
            server.enqueue(MockResponse().setResponseCode(200))
            server.start(port)

            kclass?.headFun()
            request = server.takeRequest()
        }
        test("method = HEAD") {
            assertEquals(Method.HEAD.value, request?.method)
        }
        test("path = /head") {
            assertEquals("/head", request?.path)
        }
        afterGroup {
            server.shutdown()
        }
    }
})



