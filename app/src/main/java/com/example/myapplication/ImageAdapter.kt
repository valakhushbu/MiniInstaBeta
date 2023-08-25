package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemLayoutBinding

class ImageAdapter(private var imageDatas: List<ImageData>) : ListAdapter<ImageData, ImageAdapter.ViewHolder>(ImageDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(imageDatas[position])
    }

    class ViewHolder(private val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageData: ImageData) {
            binding.imageData = imageData // Bind the ImageData object to the binding variable
            binding.executePendingBindings()
        }
    }

    class ImageDiffCallback : DiffUtil.ItemCallback<ImageData>() {
        override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem.id == newItem.id // Compare using ID for unique items
        }

        override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem == newItem
        }
    }

    // Update the data using DiffUtil
    fun updateData(newImageDatas: List<ImageData>) {
        imageDatas = newImageDatas
        notifyDataSetChanged() // Or use DiffUtil if needed
    }

}