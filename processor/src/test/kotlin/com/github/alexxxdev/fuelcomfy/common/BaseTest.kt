package com.github.alexxxdev.fuelcomfy.common

import org.spekframework.spek2.Spek
import org.spekframework.spek2.dsl.Root
import java.io.File
import javax.annotation.processing.Processor

abstract class BaseTest(root: Root.() -> Unit) : Spek(root) {

    companion object {
        val root = File(System.getProperty("user.dir"))

        fun generateClass(): KotlinCompilerResult {
            val call = KotlinCompilerCall(root)
            call.inheritClasspath = true
            call.addService(Processor::class, com.github.alexxxdev.fuelcomfy.Processor::class)
            return call.execute()
        }

        inline fun <reified T> LoadClass(): T? {
            return Initializer(File(root, "build/classes")).createInstance<T>(T::class.qualifiedName + "Impl")
        }

        fun compileClass(): Boolean {
            return JvmCompile.exe(File(root, "build/kapt/sources"), File(root, "build/classes"))
        }
    }
}