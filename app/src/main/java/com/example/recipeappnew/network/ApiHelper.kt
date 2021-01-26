package com.example.recipeappnew.network

class ApiHelper(private val apiService: ApiService) {

    suspend fun getRecipes() = apiService.getRecipes()
}