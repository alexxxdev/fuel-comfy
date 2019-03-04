package com.github.alexxxdev.fuelcomfy.common

import okio.Buffer
import okio.buffer
import okio.sink
import org.jetbrains.kotlin.cli.common.CLITool
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.com.google.common.collect.LinkedHashMultimap
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.io.PrintStream
import java.net.URLClassLoader
import java.net.URLDecoder
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.reflect.KClass

internal class KotlinCompilerCall(var scratchDir: File) {
    val sourcesDir = File(scratchDir, "src/test/kotlin/com/github/alexxxdev/fuelcomfy/sources")
    val classesDir = File(scratchDir, "build/classes")
    val servicesJar = File(scratchDir, "build/services.jar")

    var inheritClasspath = false

    val args = mutableListOf<String>()
    val kaptArgs = mutableMapOf<String, String>()
    val classpath = mutableListOf<String>()
    val services = LinkedHashMultimap.create<KClass<*>, KClass<*>>()

    init {
        kaptArgs += mutableMapOf("kapt.kotlin.generated" to "./processor/build/kapt/sources")
    }

    fun addKt(path: String, source: String) {
        val sourceFile = File(sourcesDir, path)
        sourceFile.parentFile.mkdirs()
        sourceFile.sink().buffer().use {
            it.writeUtf8(source)
        }
    }

    fun addService(serviceClass: KClass<*>, implementation: KClass<*>) {
        services.put(serviceClass, implementation)
    }

    fun execute(): KotlinCompilerResult {
        val fullArgs = mutableListOf<String>()
        fullArgs.addAll(args)

        fullArgs.add("-d")
        fullArgs.add(classesDir.toString())

        val fullClasspath = fullClasspath()
        if (fullClasspath.isNotEmpty()) {
            fullArgs.add("-classpath")
            fullArgs.add(fullClasspath.joinToString(separator = ":"))
        }

        for (source in sourcesDir.listFiles()) {
            fullArgs.add(source.toString())
        }

        fullArgs.addAll(annotationProcessorArgs())
        if (kaptArgs.isNotEmpty()) {
            fullArgs.apply {
                add("-P")
                add("plugin:org.jetbrains.kotlin.kapt3:apoptions=${encodeOptions(kaptArgs)}")
            }
        }

        val systemErrBuffer = Buffer()
        val oldSystemErr = System.err
        System.setErr(PrintStream(systemErrBuffer.outputStream()))
        try {
            val exitCode = CLITool.doMainNoExit(K2JVMCompiler(), fullArgs.toTypedArray())
            val systemErr = systemErrBuffer.readUtf8()
            return KotlinCompilerResult(systemErr, exitCode)
        } finally {
            System.setErr(oldSystemErr)
        }
    }

    private fun annotationProcessorArgs(): List<String> {
        val kaptSourceDir = File(scratchDir, "build/kapt/sources")
        val kaptStubsDir = File(scratchDir, "build/kapt/stubs")

        return listOf(
            "-Xplugin=${kapt3Jar()}",
            "-P", "plugin:org.jetbrains.kotlin.kapt3:sources=$kaptSourceDir",
            "-P", "plugin:org.jetbrains.kotlin.kapt3:classes=$classesDir",
            "-P", "plugin:org.jetbrains.kotlin.kapt3:stubs=$kaptStubsDir",
            "-P", "plugin:org.jetbrains.kotlin.kapt3:apclasspath=$servicesJar",
            "-P", "plugin:org.jetbrains.kotlin.kapt3:correctErrorTypes=true"
        )
    }

    private fun kapt3Jar(): File {
        for (file in classpathFiles()) {
            if (file.name.startsWith("kotlin-annotation-processing-embeddable")) return file
        }
        throw IllegalStateException(
            "no kotlin-annotation-processing-embeddable jar on classpath:\n  " +
                    "${classpathFiles().joinToString(separator = "\n  ")}}"
        )
    }

    private fun classpathFiles(): List<File> {
        val classLoader = KotlinCompilerCall::class.java.classLoader
        if (classLoader !is URLClassLoader) {
            throw UnsupportedOperationException("unable to extract classpath from $classLoader")
        }

        val result = mutableListOf<File>()
        for (url in classLoader.urLs) {
            if (url.protocol != "file") {
                throw UnsupportedOperationException("unable to handle classpath element $url")
            }
            result.add(File(URLDecoder.decode(url.path, "UTF-8")))
        }
        return result.toList()
    }

    private fun fullClasspath(): List<String> {
        val result = mutableListOf<String>()
        result.addAll(classpath)

        // Copy over the classpath of the running application.
        if (inheritClasspath) {
            for (classpathFile in classpathFiles()) {
                result.add(classpathFile.toString())
            }
        }

        if (!services.isEmpty) {
            writeServicesJar()
            result.add(servicesJar.toString())
        }

        return result.toList()
    }

    private fun writeServicesJar() {
        ZipOutputStream(FileOutputStream(servicesJar)).use { zipOutputStream ->
            for (entry in services.asMap()) {
                zipOutputStream.putNextEntry(
                    ZipEntry("META-INF/services/${entry.key.qualifiedName}")
                )
                val serviceFile = zipOutputStream.sink().buffer()
                for (implementation in entry.value) {
                    serviceFile.writeUtf8(implementation.qualifiedName!!)
                    serviceFile.writeUtf8("\n")
                }
                serviceFile.emit() // Don't close the entry; that closes the file.
                zipOutputStream.closeEntry()
            }
        }
    }

    private fun encodeOptions(options: Map<String, String>): String {
        val buffer = Buffer()
        ObjectOutputStream(buffer.outputStream()).use { oos ->
            oos.writeInt(options.size)
            for ((key, value) in options.entries) {
                oos.writeUTF(key)
                oos.writeUTF(value)
            }
        }
        return buffer.readByteString().base64()
    }
}

class KotlinCompilerResult(
    val systemErr: String,
    var exitCode: ExitCode
)