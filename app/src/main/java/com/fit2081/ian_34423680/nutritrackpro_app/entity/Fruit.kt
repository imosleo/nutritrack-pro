package com.fit2081.ian_34423680.nutritrackpro_app.entity

data class Fruit(
    val name: String = "",
    val id: Int = 0,
    val family: String = "",
    val order: String = "",
    val genus: String = "",
    val nutritions: Nutritions = Nutritions()
)

data class Nutritions(
    val calories: Int = 0,
    val fat: Double = 0.0,
    val sugar: Double = 0.0,
    val carbohydrates: Double = 0.0,
    val protein: Double = 0.0
)