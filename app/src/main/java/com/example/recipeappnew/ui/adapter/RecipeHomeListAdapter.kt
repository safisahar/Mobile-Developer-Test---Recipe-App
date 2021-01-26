package com.example.recipeappnew.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.recipeappnew.databinding.RecipeItemBinding
import com.example.recipeappnew.model.Recipe

class RecipeHomeListAdapter(private val list: List<Recipe>, val adapterOnClick: (Int) -> Unit) :
    RecyclerView.Adapter<RecipeHomeListAdapter.RecipeListView>() {

    inner class RecipeListView(private val binding: RecipeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipeModel: Recipe) {
            binding.foodTitleTv.text = recipeModel.title
            binding.foodTimeTv.text = "Cooking Time: ${recipeModel.cookingTime}"
            binding.foodCoverIv.load(recipeModel.cover)
            binding.root.setOnClickListener { adapterOnClick(recipeModel.id) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListView {
        return RecipeListView(
            RecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecipeListView, position: Int) =
        holder.bind(list[position])
}