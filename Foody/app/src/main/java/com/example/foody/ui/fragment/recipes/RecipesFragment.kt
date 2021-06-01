package com.example.foody.ui.fragment.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.viewmodels.MainViewModel
import com.example.foody.R
import com.example.foody.adapters.RecipesAdapter
import com.example.foody.util.Constants.Companion.API_KEY
import com.example.foody.util.NetworkResult
import com.example.foody.util.observeOnce
import com.example.foody.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {


    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel:RecipesViewModel

    val name = RecipesAdapter()




    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         mView = inflater.inflate(R.layout.fragment_recipes, container, false)

         setUpRecyclerView()
        readDataBase()

        return mView
    }

    private fun setUpRecyclerView(){
        mView.recyclerView.adapter = mAdapter
        mView.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }


    private fun readDataBase() {
        lifecycleScope.launch {
            Log.d("RecipesFragment","requestApiData Called")
            mainViewModel.readRecipe.observeOnce(viewLifecycleOwner, Observer {database ->
                if(database.isNotEmpty()){
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                }else{
                    requestApiData()
                }
            })
        }

    }

    private fun requestApiData(){

        Log.d("RecipesFragment","requestApiData Called")

        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipeResponse.observe(viewLifecycleOwner, Observer {response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }

                is NetworkResult.Error ->{
                    loadDataFromCache()
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Loading ->{
                    showShimmerEffect()
                }
            }

        })

    }

   private fun loadDataFromCache(){
       lifecycleScope.launch {
           mainViewModel.readRecipe.observe(viewLifecycleOwner, Observer {database ->
               if (database.isNotEmpty()){
                   mAdapter.setData(database[0].foodRecipe)
               }

           })
       }

   }

    private fun showShimmerEffect(){
        mView.recyclerView.showShimmer()
    }

    private fun hideShimmerEffect(){
        mView.recyclerView.hideShimmer()
    }

}