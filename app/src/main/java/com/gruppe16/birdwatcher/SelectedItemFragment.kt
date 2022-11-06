package com.gruppe16.birdwatcher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gruppe16.birdwatcher.databinding.FragmentSelecteditemBinding

// View binding fragments
private var _binding: FragmentSelecteditemBinding? = null
private val binding get() = _binding!!


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectedItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SelectedItemFragment : Fragment(R.layout.fragment_selecteditem) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelecteditemBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    //TODO: add logic for edit activity and link to button
    private fun editSelectedItem(){}
}