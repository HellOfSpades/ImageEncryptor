package com.example.imageencryptor.keydetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentKeyDetailsPrivateKeyBinding

class KeyDetailsPrivateKeyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get the key from arguments
        var args = KeyDetailsPrivateKeyFragmentArgs.fromBundle(requireArguments())
        var key = args.key

        //inflate the lyout
        val binding = FragmentKeyDetailsPrivateKeyBinding.inflate(inflater)

        binding.keyDetailsPrivateKeyModulusTextView.text = key.modulus
        binding.keyDetailsPrivateKeyPrivateExponentTextView.text = key.privateExponent
        binding.keyDetailsPrivateKeyPublicExponentTextView.text = key.publicExponent

        return binding.root
    }
}