package com.gruppe16.birdwatcher.fragments

import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.core.content.ContextCompat
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.databinding.FragmentCreateItemBinding
import com.gruppe16.birdwatcher.viewmodels.HomeCreateViewModel


class CreateItem : Fragment() {

    private val viewModel: HomeCreateViewModel by viewModels()
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
        binding.imageView2.setImageURI(viewModel.pictureUri.toUri())
        Log.d("CREATE", "HER ER RES: ${viewModel.pictureUri}" )
        binding.saveListing.setOnClickListener {
            findNavController().navigate(R.id.action_createItem_to_homeFragment)
        }
    }
}
