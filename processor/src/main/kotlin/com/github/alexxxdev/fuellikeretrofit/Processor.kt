package com.github.alexxxdev.fuellikeretrofit

import com.github.alexxxdev.fuellikeretrofit.annotation.FuelInterface
import com.github.alexxxdev.fuellikeretrofit.annotation.Get
import com.github.alexxxdev.fuellikeretrofit.annotation.Param
import com.github.alexxxdev.fuellikeretrofit.annotation.Post
import com.github.alexxxdev.fuellikeretrofit.visitor.FuelInterfaceVisitor
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

class Processor : AbstractProcessor() {
    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elementUtils: Elements
    private var visitors: Set<FuelInterfaceVisitor> = emptySet()
    private var kaptKotlinGeneratedDir: String = ""

    override fun init(env: ProcessingEnvironment?) {
        super.init(env)
        filer = processingEnv.filer
        messager = processingEnv.messager
        elementUtils = processingEnv.elementUtils
        kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME].orEmpty()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(
            FuelInterface::class.java.canonicalName,
            Get::class.java.canonicalName,
            Post::class.java.canonicalName,
            Param::class.java.canonicalName
        )
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

    override fun process(annotations: MutableSet<out TypeElement>?, env: RoundEnvironment?): Boolean {
        if (kaptKotlinGeneratedDir.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Can't find the target directory for generated Kotlin files.")
            return false
        }
        if (env?.processingOver() == false) {
            env.getElementsAnnotatedWith(FuelInterface::class.java).mapNotNull { it as? TypeElement }.forEach { element ->
                if (element.kind != ElementKind.INTERFACE) {
                    messager.printMessage(
                        Diagnostic.Kind.WARNING,
                        "Only interfaces can be annotated with ${FuelInterface::class.simpleName}",
                        element
                    )
                } else {
                    val fuelInterfaceVisitor =
                        FuelInterfaceVisitor(element, elementUtils, filer, messager, kaptKotlinGeneratedDir)
                    visitors = visitors.plus(fuelInterfaceVisitor)
                    element.accept(fuelInterfaceVisitor, null)
                }
            }
            visitors.forEach { it.build() }
        }
        return true
    }
}