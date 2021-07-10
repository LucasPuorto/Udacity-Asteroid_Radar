package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidClickListener
import com.udacity.asteroidradar.AsteroidsAdapter
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment(), AsteroidClickListener {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val adapter: AsteroidsAdapter by lazy { AsteroidsAdapter(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        setupRecyclerView(binding)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    override fun asteroidClick(asteroid: Asteroid) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    private fun setupRecyclerView(binding: FragmentMainBinding) {
        binding.asteroidRecycler.adapter = adapter
        val list = mutableListOf<Asteroid>()
        for (i in 0..40) {
            list.add(
                Asteroid(
                    id = i.toLong(),
                    codename = "name",
                    closeApproachDate = "10/07/2021",
                    absoluteMagnitude = 100000.0,
                    estimatedDiameter = 200000.0,
                    relativeVelocity = 300000.0,
                    distanceFromEarth = 400000.0,
                    isPotentiallyHazardous = (i % 2) == 0
                )
            )
        }
        adapter.addAsteroidsList(list)
    }
}
