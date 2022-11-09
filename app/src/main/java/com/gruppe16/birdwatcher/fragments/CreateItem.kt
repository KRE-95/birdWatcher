package com.gruppe16.birdwatcher.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.databinding.FragmentCreateItemBinding


class CreateItem : Fragment() {

    private var _binding: FragmentCreateItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreateItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveListing.setOnClickListener {
            findNavController().navigate(R.id.action_createItem_to_homeFragment)
        }
    }
}
