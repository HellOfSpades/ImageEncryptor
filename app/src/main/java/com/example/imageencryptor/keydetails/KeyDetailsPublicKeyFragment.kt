package com.example.imageencryptor.keydetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentKeyDetailsPublicKeyBinding


class KeyDetailsPublicKeyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //get the key from arguments
        var args = KeyDetailsPublicKeyFragmentArgs.fromBundle(requireArguments())
        var key = args.key

        //inflate the layout of the fragment
        var binding = FragmentKeyDetailsPublicKeyBinding.inflate(inflater)

        //set the text view to have the key details
        binding.keyDetailsPublicKeyModulusTextView.text = key.modulus
        binding.keyDetailsPublicKeyPublicExponentTextView.text = key.publicExponent

        return binding.root
    }
}