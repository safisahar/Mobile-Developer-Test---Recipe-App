package com.example.recipeappnew.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeappnew.model.RecipeData
import com.example.recipeappnew.model.State
import com.example.recipeappnew.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {

    private val _mainLiveData = MutableLiveData<State<RecipeData>>()
    val recipeLiveData: LiveData<State<RecipeData>> get() = _mainLiveData



    fun getRecipes() {
        _mainLiveData.value = State.loading()
        viewModelScope.launch(Dispatchers.IO) {
            val mainResponse = mainRepository.getRecipes()
            withContext(Dispatchers.Main) {

                mainResponse.let { mainData ->
                    mainData.recipes?.let {
                        if (it.isNotEmpty()) {
                            _mainLiveData.value = State.success(mainResponse)
                        } else {
                            _mainLiveData.value = State.error("No Data Available")
                        }
                    }

                } ?: run {
                    _mainLiveData.value = State.error("Something went wrong")
                }


            }
        }
    }

}