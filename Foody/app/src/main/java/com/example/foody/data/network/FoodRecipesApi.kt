package com.example.foody.data.network

import com.example.foody.model.FoodRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {

    //https://api.spoonacular.com/recipes/complexSearch?number=1&apiKey=c6c80b2897af4d91a030731232a424b7&type=drink&diet=vagen&addRecipeInformation=true&fillIngredients=true


    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String,String>
    ):Response<FoodRecipe>


    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap searchQuery: Map<String,String>
    ):Response<FoodRecipe>
}