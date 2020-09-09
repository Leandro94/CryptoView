package com.leandro.cryptoview.util

import java.math.BigDecimal
import java.math.RoundingMode

fun formatterToDecimal(str: String, scale: Int): BigDecimal {
    var newValue = BigDecimal.ZERO
    try {
        newValue = BigDecimal(str).setScale(scale, RoundingMode.HALF_EVEN)

    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }
    return newValue
}