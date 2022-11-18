package com.gruppe16.birdwatcher.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.gruppe16.birdwatcher.R
import com.gruppe16.birdwatcher.components.FirebaseDatabase
import com.gruppe16.birdwatcher.databinding.FragmentSelecteditemBinding
import com.gruppe16.birdwatcher.viewmodels.SharedViewModel

class SelectedItemFragment : Fragment() {

    private val viewModel: SharedViewModel by activityViewModels()
    private var _binding: FragmentSelecteditemBinding? = null
    private val binding get() = _binding!!
    private lateinit var db : FirebaseDatabase

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
        db = FirebaseDatabase()

        binding.editNameBtn.setOnClickListener{
            editSelectedItem("bird name", "birdName")
        }

        binding.editLocationBtn.setOnClickListener{
            editSelectedItem("location", "location")
        }

        binding.editDateBtn.setOnClickListener{
            editSelectedItem("date", "date")
        }

        binding.editDescriptionBtn.setOnClickListener{
            editSelectedItem("description", "description")
        }

        binding.editSelectedItemButton.setOnClickListener{
            findNavController().navigate(R.id.action_selectedItemFragment_to_editSelectedItemFragment)
        }

        binding.backButton.setOnClickListener{
            findNavController().navigate(R.id.action_selectedItemFragment_to_galleryFragment)
        }
    }

    //TODO: add logic for edit activity
    private fun editSelectedItem(enterText: String, toUpdate: String){
        val builder = AlertDialog.Builder(this.requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_listing_layout, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.etBox)

        with(builder) {
            setTitle("Enter new ${enterText}:")
            setPositiveButton("OK") {
                    dialog, which ->
                db.updateListing(viewModel.listId.value.toString(), toUpdate, editText.text.toString())
                when(enterText) {
                    "bird name" -> binding.birdName.text = editText.text.toString()
                    "location" -> binding.place.text = editText.text.toString()
                    "date" -> binding.date.text = editText.text.toString()
                    else -> binding.description.text = editText.text.toString()
                }
            }
            setNegativeButton("Cancel") {
                    dialog, which ->
                Log.d("Dialog box", "Edit canceled")
            }
            setView(dialogLayout)
            show()
        }
    }
}