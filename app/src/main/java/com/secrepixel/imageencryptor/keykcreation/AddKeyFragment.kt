package com.secrepixel.imageencryptor.keykcreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.secrepixel.imageencryptor.R
import com.secrepixel.imageencryptor.databinding.FragmentAddKeyBinding
import com.secrepixel.imageencryptor.mainmenu.MainMenuViewModel

/**
 * Fragment responsible for users to generate, or add existing, keys
 */
class AddKeyFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //inflating the binding
        val binding: FragmentAddKeyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_key, container, false)
        //getting the view model
        val viewModel = ViewModelProvider(this).get(AddKeyViewModel::class.java)

        //setting on click listeners
        binding.generateNewKeyButton.setOnClickListener(){
            //check that the required field is not blank
            if(binding.addedKeyNameEditText.text.toString()==""){
                Toast.makeText(requireContext(), "Please provide a name for the key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.generateAndInsertNewKeyToDatabase(binding.addedKeyNameEditText.text.toString())
            Navigation.findNavController(it).navigate(R.id.action_addKeyFragment_to_mainMenuFragment)
        }
        binding.useExistingKeyButton.setOnClickListener(){

            //check that the required fields is not blank
            if(binding.addedKeyNameEditText.text.toString().equals("")){
                Toast.makeText(requireContext(), "Please provide a name for the key", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(binding.editTextModulus.text.toString().equals("") || binding.editTextPublicExponent.text.toString().equals("")){
                Toast.makeText(requireContext(), "Please provide a modulus and a public key", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var privateExp: String? = binding.editTextPrivateExponent.text.toString()
            if(privateExp.equals(""))privateExp = null;

            viewModel.constructAndInsertKeyIntoDatabase(
                binding.addedKeyNameEditText.text.toString(),
                binding.editTextModulus.text.toString(),
                binding.editTextPublicExponent.text.toString(),
                privateExp
            )
            Navigation.findNavController(it).navigate(R.id.action_addKeyFragment_to_mainMenuFragment)
        }
        //return the binding root
        return binding.root
    }
}