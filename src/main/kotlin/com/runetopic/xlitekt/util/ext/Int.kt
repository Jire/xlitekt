package com.runetopic.xlitekt.util.ext

fun Int.packInterface(childId: Int = 0) = this.shl(16).or(childId)
fun Int.toBoolean(): Boolean = this == 1
