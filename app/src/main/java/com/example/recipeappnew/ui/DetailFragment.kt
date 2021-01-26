package com.example.recipeappnew.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.example.recipeappnew.R
import com.example.recipeappnew.databinding.FragmentDetailBinding
import com.example.recipeappnew.model.Recipe
import com.example.recipeappnew.model.State
import com.example.recipeappnew.network.ApiHelper
import com.example.recipeappnew.network.RetrofitBuilder
import com.example.recipeappnew.utils.ViewModelFactory
import com.example.recipeappnew.viewmodel.MainViewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private lateinit var viewModel: MainViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupObservers()
        setupOnClick()
    }

    private fun setupOnClick() {
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

    private fun setupViewModel() {

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
        viewModel.getRecipes()
    }

    private fun setupObservers() {
        viewModel.recipeLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { state ->
                when (state) {
                    is State.Loading -> {
                        binding.loader.loadingView.visibility = View.VISIBLE
                    }
                    is State.Success -> {
                        binding.loader.loadingView.visibility = View.GONE
                        state.data.recipes?.let { list ->
                            val data =
                                list.filter { recipe -> recipe.id == arguments?.getInt("recipeId") }[0]
                            setData(data)
                        }
                    }
                    is State.Error -> {
                        binding.loader.loadingView.visibility = View.GONE
                        Toast.makeText(activity, state.message, Toast.LENGTH_SHORT).show()

                    }
                }
            }
        })
    }

    fun setData(recipe: Recipe) {
        binding.titleTv.text = recipe.title
        binding.cookTimeTv.text = getString(R.string.cooking_time, recipe.cookingTime)
        binding.prepTimeTv.text = getString(R.string.preparation_time, recipe.prepTime)
        binding.ingredientsTv.text = recipe.ingredients?.replace("\\n", "\n\n")
        binding.stepsTv.text = recipe.stepsGuide?.replace("\\n", "\n\n")
        binding.foodCoverIv.load(recipe.cover)

    }
}