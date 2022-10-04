package com.example.imageencryptor.mainmenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imageencryptor.databinding.FragmentSelectedKeyBinding


class SelectedKeyFragment : Fragment() {

    private lateinit var binding: FragmentSelectedKeyBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSelectedKeyBinding.inflate(inflater)


        return binding.root
    }
}