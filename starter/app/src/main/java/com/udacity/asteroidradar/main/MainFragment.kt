package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModelFactory = MainViewModelFactory(
            application = requireNotNull(this.activity).application
        )
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        val adapter = AsteroidAdapter(AsteroidListener { asteroid ->
            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
        })
        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroid.observe(requireActivity(), Observer { asteroid ->
            adapter.data = asteroid
        })
        viewModel.imageProperty.observe(requireActivity(), Observer { property ->
            if(property.mediaType == Constants.IMAGE_TYPE) {
                binding.activityMainImageOfTheDay.let { imageView ->
                    Picasso.with(context).load(property.imageUrl).into(imageView)
                    imageView.contentDescription = property.title
                }
                binding.activityMainImageOfTheDayLayout.visibility = View.VISIBLE
            } else {
                binding.activityMainImageOfTheDayLayout.visibility = View.GONE
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.week -> viewModel.fetchAsteroids(Constants.RangeEndDate.WEEK_END_DATE_DAYS)
            R.id.day -> viewModel.fetchAsteroids(Constants.RangeEndDate.TODAY_END_DATE_DAYS)
            R.id.database -> viewModel.fetchAsFromDatabase()
        }
        return true
    }
}
