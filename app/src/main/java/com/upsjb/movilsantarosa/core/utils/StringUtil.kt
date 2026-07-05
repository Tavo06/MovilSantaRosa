package com.upsjb.movilsantarosa.core.utils

fun Double.toCurrencyString(
    symbol: String = "S/"
): String = "$symbol ${"%.2f".format(this)}"