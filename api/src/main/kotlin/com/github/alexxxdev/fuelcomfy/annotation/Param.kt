package com.github.alexxxdev.fuelcomfy.annotation

/**
 * A named template parameter applied to {@link Get}, {@link Post}, {@link Put}, {@link Patch}, {@link Head}, {@link Delete} or {@link Header}
 *
 * value - The name of the template parameter.
 */

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
@MustBeDocumented
annotation class Param(val value: String)
