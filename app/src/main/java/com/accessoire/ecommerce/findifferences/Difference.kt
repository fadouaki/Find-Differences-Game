package com.accessoire.ecommerce.findifferences

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Difference(
    val x: Float,
    val y: Float,
    val radius: Float,
    var isFound: Boolean = false
) {
    var isFoundState by mutableStateOf(isFound)
}