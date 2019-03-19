package com.github.alexxxdev.fuelcomfy

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

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
