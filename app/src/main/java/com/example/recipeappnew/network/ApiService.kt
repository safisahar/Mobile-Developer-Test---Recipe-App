package com.example.recipeappnew.network

import com.example.recipeappnew.model.RecipeData
import retrofit2.http.GET

interface ApiService {

    @GET("db")
    suspend fun getRecipes(): RecipeData
}