package com.accessoire.ecommerce.findifferences

import androidx.compose.ui.graphics.vector.ImageVector

data class Level(
    val name: String,
    val image_1: Int,
    val image_2: Int,
    val difference: List<Difference>
)


