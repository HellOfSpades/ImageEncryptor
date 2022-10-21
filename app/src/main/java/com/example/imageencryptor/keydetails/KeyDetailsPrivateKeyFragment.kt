package com.example.imageencryptor.keydetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.imageencryptor.R
import com.example.imageencryptor.databinding.FragmentKeyDetailsPrivateKeyBinding

/**
 * Fragment used to display the full information of the key
 */
class KeyDetailsPrivateKeyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get the key from arguments
        var args = KeyDetailsPrivateKeyFragmentArgs.fromBundle(requireArguments())
        var key = args.key

        //inflate the layout
        val binding = FragmentKeyDetailsPrivateKeyBinding.inflate(inflater)

        //set text views to have the key details
        binding.keyDetailsPrivateKeyModulusTextView.text = key.modulus
        binding.keyDetailsPrivateKeyPrivateExponentTextView.text = key.privateExponent
        binding.keyDetailsPrivateKeyPublicExponentTextView.text = key.publicExponent

        return binding.root
    }
}