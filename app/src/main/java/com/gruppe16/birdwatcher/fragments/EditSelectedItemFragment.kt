package com.gruppe16.birdwatcher.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.databinding.FragmentEditselecteditemBinding
import com.gruppe16.birdwatcher.viewmodels.SharedViewModel


class EditSelectedItemFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentEditselecteditemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditselecteditemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveEditButton.setOnClickListener{
            // TODO: call saveEdits
        findNavController().navigate(R.id.action_editSelectedItemFragment_to_selectedItemFragment)
        }
    }


    //TODO: add logic for saving edits to firebase
    private fun saveEdits(){}
}