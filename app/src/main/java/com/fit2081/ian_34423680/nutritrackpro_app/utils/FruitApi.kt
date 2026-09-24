package com.fit2081.ian_34423680.nutritrackpro_app.utils

import com.fit2081.ian_34423680.nutritrackpro_app.entity.Fruit
import retrofit2.http.GET

interface FruitApi {
    @GET("api/fruit")
    suspend fun getFruit(): List<Fruit>

    @GET("api/fruit/{name}")
    suspend fun getFruitByName(@retrofit2.http.Path("name") name: String): Fruit
}