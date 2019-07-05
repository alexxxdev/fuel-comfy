package com.github.alexxxdev.fuelcomfy

import com.github.alexxxdev.fuelcomfy.common.BaseTest
import com.github.alexxxdev.fuelcomfy.sources.KotlinSerializationService
import org.jetbrains.kotlin.cli.common.ExitCode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object KotlinSerializationTest : BaseTest({
    var kclass: KotlinSerializationService?

    group("Prepare class with suspend functions") {
        val resultGenerate = generateClass()
        val resultCompile = compileClass()
        kclass = LoadClass<KotlinSerializationService>()
        test("Generate success") { assertEquals(ExitCode.OK, resultGenerate.exitCode) }
        test("Compile success") { assertTrue(resultCompile) }
        test("Load success") { assertNotNull(kclass) }
    }
})
