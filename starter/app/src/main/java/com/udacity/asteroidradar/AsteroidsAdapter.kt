package com.udacity.asteroidradar

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AsteroidsAdapter(private val listener: AsteroidClickListener) : ListAdapter<Asteroid, AsteroidsViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder =
        AsteroidsViewHolder.from(parent)

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        holder.bind(listener, getItem(position))
    }

    fun addAsteroidsList(asteroidsList: List<Asteroid>) {
        CoroutineScope(Dispatchers.Default).launch {
            withContext(Dispatchers.Main) {
                submitList(asteroidsList)
            }
        }
    }
}

interface AsteroidClickListener {
    fun asteroidClick(asteroid: Asteroid)
}

class AsteroidsViewHolder private constructor(private val binding: ItemAsteroidBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(listener: AsteroidClickListener, asteroid: Asteroid) {
        binding.asteroid = asteroid
        binding.clickListener = listener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AsteroidsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemAsteroidBinding.inflate(layoutInflater, parent, false)

            return AsteroidsViewHolder(binding)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean =
        oldItem.id == newItem.id
}