package com.upsjb.movilsantarosa.core.utils

fun Double.toCurrencyString(
    symbol: String = "S/"
): String = "$symbol ${"%.2f".format(this)}"

fun String.toDoubleSafe(): Double {
    return try {
        this.toDouble()
    } catch (e: Exception) {
        0.0
    }
}