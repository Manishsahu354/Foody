package com.example.foody.data

import com.example.foody.data.database.RecipesDao
import com.example.foody.data.database.RecipesEntity

import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
)  {

     fun readDatabase() = recipesDao.readRecipes()


    suspend fun insertRecipes(recipesEntity: RecipesEntity) =
        recipesDao.insertRecipes(recipesEntity)

}