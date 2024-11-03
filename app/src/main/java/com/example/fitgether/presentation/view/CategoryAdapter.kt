package com.example.fitgether.presentation.view

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.fitgether.data.model.Category
import com.example.fitgether.databinding.CategoryCardItemBinding

class CategoryAdapter(val categoryList : List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    class CategoryHolder(val binding : CategoryCardItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val binding = CategoryCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.binding.imageView3.setImageResource(categoryList[position].categoryImage)
        holder.binding.textView3.text = categoryList[position].categoryTitle
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}