package com.example.fitgether.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import com.example.fitgether.R
import com.example.fitgether.data.model.categoryList
import com.example.fitgether.data.model.eventList
import com.example.fitgether.databinding.FragmentCreateEventBinding
import com.example.fitgether.databinding.FragmentHomeScreenBinding


class HomeScreenFragment : Fragment() {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater,container,false)

        binding.homePageCategoryRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL,false)
        val categoryAdapter = CategoryAdapter(categoryList = categoryList)
        binding.homePageCategoryRecycler.adapter = categoryAdapter

        binding.homepagePopularRecycler.layoutManager = LinearLayoutManager(requireContext())
        val popularAdapter = HomepageEventsAdapter(eventList = eventList) //buradaki liste sadece önizleme amaçlı oluşturuldu sil yarın. firebaseden çekmedim bunu yani.
        binding.homepagePopularRecycler.adapter = popularAdapter

        return binding.root
    }

}