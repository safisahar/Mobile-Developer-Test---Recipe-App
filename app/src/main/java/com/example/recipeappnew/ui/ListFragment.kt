package com.example.recipeappnew.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipeappnew.R
import com.example.recipeappnew.databinding.FragmentListBinding
import com.example.recipeappnew.model.Recipe
import com.example.recipeappnew.model.State
import com.example.recipeappnew.network.ApiHelper
import com.example.recipeappnew.network.RetrofitBuilder
import com.example.recipeappnew.ui.adapter.RecipeListAdapter
import com.example.recipeappnew.utils.ViewModelFactory
import com.example.recipeappnew.viewmodel.MainViewModel

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private lateinit var viewModel: MainViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
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
                        state.data.recipes?.let { it1 -> setAdapter(it1) }
                    }
                    is State.Error -> {
                        binding.loader.loadingView.visibility = View.GONE
                        Toast.makeText(activity, state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun setAdapter(recipes: List<Recipe>) {
        binding.recipeListRv.adapter = RecipeListAdapter(recipes) { goToDetailPage(it) }

    }

    private fun goToDetailPage(id: Int) {
        val bundle = bundleOf("recipeId" to id)
        findNavController().navigate(R.id.action_listFragment_to_detailFragment, bundle)
    }
}