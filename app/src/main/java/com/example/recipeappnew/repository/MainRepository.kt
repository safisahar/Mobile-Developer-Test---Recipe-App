package com.example.recipeappnew.repository

import com.example.recipeappnew.network.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getRecipes() = apiHelper.getRecipes()
}