package com.example.imageencryptor.keykcreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentAddExistingKeyBinding


class AddExistingKeyFragment : Fragment() {

    lateinit var binding: FragmentAddExistingKeyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExistingKeyBinding.inflate(inflater)



        return binding.root
    }
}