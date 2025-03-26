package com.example.edufun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edufun.R
import com.example.edufun.model.Category

class CategoryAdapter(
    private var categories: List<Category>,
    private val listener: OnCategoryClickListener // Menambahkan listener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface OnCategoryClickListener {
        fun onCategoryClick(category: Category)
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.tv_item_title)
        val categoryDesc: TextView = itemView.findViewById(R.id.tv_item_subtitle)
        val categoryImage: ImageView = itemView.findViewById(R.id.iv_item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name
        holder.categoryDesc.text = category.desc
        holder.categoryImage.setImageResource(category.imageResId)

        holder.itemView.setOnClickListener {
            listener.onCategoryClick(category) // Memanggil listener saat item diklik
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun updateList(newList: List<Category>) {
        categories = newList
        notifyDataSetChanged()
    }
}
