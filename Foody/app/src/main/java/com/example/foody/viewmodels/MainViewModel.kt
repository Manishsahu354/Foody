package com.example.foody.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foody.data.Repository
import com.example.foody.data.database.RecipesEntity
import com.example.foody.model.FoodRecipe
import com.example.foody.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

// we need application reference viewModel so we use androidViewModel();

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
):AndroidViewModel(application) {



    /** ROOM DATABASE */

    val readRecipe:LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO){
            repository.local.insertRecipes(recipesEntity)
        }

    /** RETROFIT */

    var recipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchRecipesResponse:MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String,String>){
        viewModelScope.launch {
            getRecipesSafeCall(queries)
        }
    }
    fun searchRecipes(searchQuery:Map<String,String>) = viewModelScope.launch {
        searchRecipesSafeCall(searchQuery)
    }



    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){

            try {
                val response = repository.remote.getRecipes(queries)
                recipeResponse.value = handleFoodRecipesResponse(response)

                val foodRecipe = recipeResponse.value!!.data
                foodRecipe?.let {
                    offlineCacheRecipes(foodRecipe)
                }

            }catch (e:Exception){

                recipeResponse.value = NetworkResult.Error("Recipes Not Found")
            }
        }else{
            recipeResponse.value = NetworkResult.Error("No Internet Connection")

        }
    }

    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {

        searchRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){

            try {
                val response = repository.remote.searchRecipe(searchQuery)
                searchRecipesResponse.value = handleFoodRecipesResponse(response)

            }catch (e:Exception){

                searchRecipesResponse.value = NetworkResult.Error("Recipes Not Found")
            }
        }else{
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection")

        }
    }

    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
       val recipesEntity = RecipesEntity(foodRecipe)

        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 ->{
                return NetworkResult.Error("Api Key Limited")
            }
            response.body()!!.results.isNullOrEmpty()->{
                return NetworkResult.Error("Recipes Not foung")
            }
            response.isSuccessful ->{
                val foodRecipe = response.body()
                return NetworkResult.Success(foodRecipe!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false;
        }
    }
}