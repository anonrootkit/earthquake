package com.example.earthquake

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.earthquake.databinding.PhotoListItemBinding

class NewAdapter : ListAdapter<Photo, NewAdapter.ViewHolder>(DiffUtilCallback) {

    object DiffUtilCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return true
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(private val binding : PhotoListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bindPhotoToViewHolder(photo : Photo) {
            binding.photo = photo
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PhotoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val photo = getItem(position)
        holder.bindPhotoToViewHolder(photo)
    }
}