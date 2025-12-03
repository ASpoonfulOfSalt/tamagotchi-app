package com.cse.tamagotchi.model

import com.cse.tamagotchi.R
import kotlinx.serialization.Serializable

@Serializable
enum class Hat(val drawableRes: Int, val yOffset: Int = -10, val xOffset: Int = 0) {
    BASEBALL(R.drawable.ic_hat_baseball, 4, 8),
    BEANIE(R.drawable.ic_hat_beanie, -10, -5),
    BUCKET(R.drawable.ic_hat_bucket, 0),
    COWBOY(R.drawable.ic_hat_cowboy, -15),
    PARTY(R.drawable.ic_hat_party, -10),
    TOP(R.drawable.ic_hat_top, -10, -10)
    // drawableRes = hat type shown
    // yOffset = fixes hats that are too far or low on Tamagotchi's head
    // xOffset = fixes hats that are too far horizontally on Tamagotchi's head
}
