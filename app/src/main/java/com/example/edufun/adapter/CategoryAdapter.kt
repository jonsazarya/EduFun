package com.example.edufun.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edufun.R
import com.example.edufun.model.Category

class CategoryAdapter(private val categories: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewholder>() {

    class CategoryViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.tv_item_title)
        val categoryDesc: TextView = itemView.findViewById(R.id.tv_item_subtitle)
        val categoryImage: ImageView = itemView.findViewById(R.id.iv_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.CategoryViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false)
        return CategoryViewholder(view)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.CategoryViewholder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name
        holder.categoryDesc.text = category.desc
        holder.categoryImage.setImageResource(category.imageResId)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}