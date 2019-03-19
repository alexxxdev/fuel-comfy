package com.github.alexxxdev.fuelcomfy.visitor

import com.github.alexxxdev.fuelcomfy.builder.FunSpecBuilder
import com.github.alexxxdev.fuelcomfy.builder.InitBuilder
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementScanner7
import javax.lang.model.util.Elements

private const val SUFFIX = "Impl"

class FuelInterfaceVisitor(
    original: TypeElement,
    private val elementUtils: Elements,
    filer: Filer,
    private val messager: Messager,
    kaptKotlinGeneratedDir: String
) : ElementScanner7<Any, Any>() {

    private var file: FileSpec.Builder? = null
    private var clazz: TypeSpec.Builder? = null
    private var funcs: List<FunSpecBuilder> = emptyList()
    private var outFile: File = File(kaptKotlinGeneratedDir).apply { mkdir() }
    private val initBuilder = InitBuilder(original, messager)

    override fun visitType(element: TypeElement?, p1: Any?): Any? {
        element?.let { element ->
            if (element.kind == ElementKind.INTERFACE) {
                val packageName = elementUtils.getPackageOf(element).qualifiedName.toString()
                val className = element.simpleName.toString()
                file = FileSpec.builder(packageName, className + SUFFIX)
                clazz = TypeSpec.classBuilder(className + SUFFIX)
                    .addSuperinterface(ClassName(packageName, className))
                clazz?.addInitializerBlock(initBuilder.build())
            }
        }
        return super.visitType(element, p1)
    }

    override fun visitExecutable(element: ExecutableElement?, p1: Any?): Any? {
        element?.let { funcs = funcs.plus(FunSpecBuilder(element, messager)) }
        return super.visitExecutable(element, p1)
    }

    fun build() {
        file?.let { file ->
            clazz?.let { clazz ->
                funcs.mapNotNull { func ->
                    func.build { className ->
                        file.addImport(className.packageName, className.simpleName)
                    }
                }.forEach { clazz.addFunction(it) }
                file.addType(clazz.build())
            }
            file.build().writeTo(outFile)
        }
    }
}