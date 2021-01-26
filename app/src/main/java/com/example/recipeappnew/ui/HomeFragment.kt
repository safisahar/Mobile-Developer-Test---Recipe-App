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
import com.example.recipeappnew.databinding.FragmentHomeBinding
import com.example.recipeappnew.model.Recipe
import com.example.recipeappnew.model.State
import com.example.recipeappnew.network.ApiHelper
import com.example.recipeappnew.network.RetrofitBuilder
import com.example.recipeappnew.ui.adapter.RecipeHomeListAdapter
import com.example.recipeappnew.utils.ViewModelFactory
import com.example.recipeappnew.viewmodel.MainViewModel
import com.example.recipeappnew.viewmodel.NavDrawerState

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var navDrawerState: NavDrawerState

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        activity?.run {
            navDrawerState = ViewModelProvider(this).get(NavDrawerState::class.java)
        } ?: throw Throwable("invalid activity")
        binding.menuIv.setOnClickListener { navDrawerState.updateNavDrawer(true) }

        binding.seeAllBf.setOnClickListener { goToListPage("Breakfast") }
        binding.seeAllLunch.setOnClickListener { goToListPage("Lunch") }
        binding.seeAllDinner.setOnClickListener { goToListPage("Dinner") }
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
        binding.breakFastRv.adapter = RecipeHomeListAdapter(recipes) { goToDetailPage(it) }
        binding.lunchRv.adapter = RecipeHomeListAdapter(recipes) { goToDetailPage(it) }
        binding.dinnerRv.adapter = RecipeHomeListAdapter(recipes) { goToDetailPage(it) }
    }

    private fun goToDetailPage(id: Int) {
        val bundle = bundleOf("recipeId" to id)
        findNavController().navigate(R.id.action_homeFragment_to_detailFragment, bundle)
    }

    private fun goToListPage(category: String) {
        val bundle = bundleOf("category" to category)
        findNavController().navigate(R.id.action_homeFragment_to_listFragment, bundle)
    }

}