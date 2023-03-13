package com.secrepixel.app.keykcreation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.secrepixel.app.tutorialtools.TutorialPreferenceKeys
import com.secrepixel.app.R
import com.secrepixel.app.tutorialtools.TutorialFragmentIterator
import com.secrepixel.app.databinding.FragmentAddKeyBinding
import com.secrepixel.app.keykcreation.tutorial.AddKeyTutorialCreatingKeys
import timber.log.Timber
import java.lang.NumberFormatException
import java.security.spec.InvalidKeySpecException

/**
 * Fragment responsible for users to generate, or add existing, keys
 */
class AddKeyFragment : Fragment() {

    lateinit var tutorialView: View;

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

        tutorialView = binding.tutorial


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
            if(binding.editTextKey.text.toString().equals("")){
                Toast.makeText(requireContext(), "Please provide a modulus and a public key", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            try {

                var keyProperties = binding.editTextKey.text.toString().split(":")

                for(s in keyProperties){
                    Timber.i(s)
                }

                if(keyProperties.size==2){
                    viewModel.constructAndInsertKeyIntoDatabase(
                        binding.addedKeyNameEditText.text.toString(),
                        keyProperties[0],
                        keyProperties[1],
                        null
                    )
                }else{
                    viewModel.constructAndInsertKeyIntoDatabase(
                        binding.addedKeyNameEditText.text.toString(),
                        keyProperties[0],
                        keyProperties[1],
                        keyProperties[2]
                    )
                }

                Navigation.findNavController(it)
                    .navigate(R.id.action_addKeyFragment_to_mainMenuFragment)
            }catch (e: InvalidKeySpecException){
                Toast.makeText(requireContext(), "Incorrect key specs", Toast.LENGTH_SHORT).show()
            }catch (e: NumberFormatException){
                Toast.makeText(requireContext(), "Modulus and keys should only contain numbers", Toast.LENGTH_SHORT).show()
            }
        }
        //return the binding root
        return binding.root
    }

    override fun onResume() {
        //if its the first time the user opens this fragment run the tutorial that will guide the user
        super.onResume()
        val tutorialKey = TutorialPreferenceKeys.ADD_KEY_FRAGMENT_TUTORIAL_KEY.key
        val firstTime = requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE).getBoolean(tutorialKey, true)
        if (firstTime) {
            runTutorial()
            requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE).edit()
                .putBoolean(tutorialKey, false).apply()
        }
    }

    /**
     * start tutorial for this fragment
     */
    private fun runTutorial() {
        val tutorial = TutorialFragmentIterator(
            arrayOf(
                AddKeyTutorialCreatingKeys()
            ))

        tutorialView.visibility = View.VISIBLE

        val nextFragment = tutorial.next();
        changeTutorialFragment(nextFragment)

        tutorialView.setOnClickListener(){
            if(!tutorial.hasNext()){
                tutorialView.visibility = View.GONE
                return@setOnClickListener;
            }
            val nextFragment = tutorial.next();
            changeTutorialFragment(nextFragment)
        }
    }

    /**
     * change tutorial fragment
     */
    private fun changeTutorialFragment(nextFragment: Fragment){
        val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        ft.replace(R.id.tutorial, nextFragment)

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        ft.addToBackStack(null)
        ft.commit()
    }
}