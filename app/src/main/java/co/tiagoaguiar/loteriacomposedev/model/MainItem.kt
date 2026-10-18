package co.tiagoaguiar.loteriacomposedev.model

import androidx.annotation.DrawableRes

data class MainItem(
    val id: Int,
    val name: String,
    val color: androidx.compose.ui.graphics.Color,
    @DrawableRes val icon: Int
)
