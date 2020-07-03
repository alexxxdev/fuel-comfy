package com.github.alexxxdev.fuelcomfy.common

import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.config.Services
import java.io.File

internal object JvmCompile {
    fun exe(input: File, output: File): Boolean = K2JVMCompiler().run {
        val args = K2JVMCompilerArguments().apply {
            freeArgs = listOf(input.absolutePath)
            destination = output.absolutePath
            classpath = System.getProperty("java.class.path")
                .split(System.getProperty("path.separator"))
                .filter {
                    File(it).exists() && File(it).canRead()
                }.joinToString(":")
            noStdlib = true
            noReflect = true
            skipRuntimeVersionCheck = true
            reportPerf = true
        }
        output.deleteOnExit()
        execImpl(
            PrintingMessageCollector(
                System.out,
                MessageRenderer.WITHOUT_PATHS, true
            ),
            Services.EMPTY,
            args
        )
    }.code == 0
}
