package com.gruppe16.birdwatcher.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.databinding.FragmentSelecteditemBinding
import com.gruppe16.birdwatcher.viewmodels.SharedViewModel

class SelectedItemFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentSelecteditemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelecteditemBinding.inflate(inflater, container, false)
        Glide.with(
            this.requireContext())
            .load(viewModel.picture.toString())
            .into(binding.selectedItem
            )
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this.requireContext()).load(viewModel.picture.value).into(binding.selectedItem)
        binding.birdName.text = viewModel.birdName.value
        binding.description.text = viewModel.description.value
        binding.date.setText(viewModel.date.value)

        binding.editSelectedItemButton.setOnClickListener{
            findNavController().navigate(R.id.action_selectedItemFragment_to_editSelectedItemFragment)
        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_selectedItemFragment_to_galleryFragment)
        }
    }

    //TODO: add logic for edit activity
    private fun editSelectedItem(){}
}