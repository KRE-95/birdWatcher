package com.gruppe16.birdwatcher.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import com.gruppe16.birdwatcher.databinding.FragmentSelecteditemBinding
import com.gruppe16.birdwatcher.viewmodels.HomeCreateViewModel

class SelectedItemFragment : Fragment() {

    private val viewModel: HomeCreateViewModel by activityViewModels()
    private var _binding: FragmentSelecteditemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelecteditemBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editSelectedItemButton.setOnClickListener{
            // TODO: go to editSelectedItem Activity
        }
    }

    //TODO: add logic for edit activity
    private fun editSelectedItem(){}
}