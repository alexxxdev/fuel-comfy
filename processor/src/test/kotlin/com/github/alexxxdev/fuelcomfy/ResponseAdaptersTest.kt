package com.github.alexxxdev.fuelcomfy

import com.github.alexxxdev.fuelcomfy.common.BaseTest
import com.github.alexxxdev.fuelcomfy.sources.ResponseAdaptersService
import org.jetbrains.kotlin.cli.common.ExitCode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object ResponseAdaptersTest : BaseTest({
    var kclass: ResponseAdaptersService? = null

    group("Prepare class with suspend functions") {
        val resultGenerate = generateClass()
        val resultCompile = compileClass()
        kclass = LoadClass<ResponseAdaptersService>()
        test("Generate success") { assertEquals(ExitCode.OK, resultGenerate.exitCode) }
        test("Compile success") { assertTrue(resultCompile) }
        test("Load success") { assertNotNull(kclass) }
    }
})
