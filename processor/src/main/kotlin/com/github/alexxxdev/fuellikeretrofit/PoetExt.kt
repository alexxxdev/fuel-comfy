package com.github.alexxxdev.fuellikeretrofit

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import javax.lang.model.element.TypeElement
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName
import kotlinx.metadata.jvm.KotlinClassHeader

fun TypeName.javaToKotlinType(): TypeName = if (this is ParameterizedTypeName) {
    (rawType.javaToKotlinType() as ClassName).parameterizedBy(
        *typeArguments.map { it.javaToKotlinType() }.toTypedArray()
    )
} else {
    val className = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(toString()))?.asSingleFqName()?.asString()
    if (className == null) {
        if (toString() == "out java.lang.Object") ClassName.bestGuess("Any")
        else this
    } else ClassName.bestGuess(className)
}

fun TypeElement.readMetadata(): KotlinClassHeader {
    return getAnnotation(Metadata::class.java).run {
        KotlinClassHeader(kind, metadataVersion, bytecodeVersion, data1, data2, extraString, packageName, extraInt)
    }
}