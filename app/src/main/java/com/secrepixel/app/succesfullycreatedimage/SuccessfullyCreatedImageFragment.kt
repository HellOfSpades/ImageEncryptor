package com.secrepixel.app.succesfullycreatedimage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.secrepixel.app.tutorialtools.TutorialPreferenceKeys
import com.secrepixel.app.R
import com.secrepixel.app.tutorialtools.TutorialFragmentIterator
import com.secrepixel.app.databinding.FragmentSuccessfullyCreatedImageBinding
import com.secrepixel.app.succesfullycreatedimage.tutorial.SuccessfullyCreatedImageTutorialCongratulations
import com.secrepixel.app.succesfullycreatedimage.tutorial.SuccessfullyCreatedImageTutorialShareImage

/**
 * this fragment is responsible for displaying to the user that their image was saved correctly
 * it also used to easily send the image to other apps
 */
class SuccessfullyCreatedImageFragment : Fragment() {

    private lateinit var viewModel:SuccessfullyCreatedImageViewModel
    private lateinit var tutorialView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //get the arguments
        var args = SuccessfullyCreatedImageFragmentArgs.fromBundle(requireArguments())
        var uri = args.uri
        //inflate the binding
        var binding = FragmentSuccessfullyCreatedImageBinding.inflate(inflater)

        tutorialView = binding.tutorial
        //get the view model
        viewModel = ViewModelProvider(this).get(SuccessfullyCreatedImageViewModel::class.java)
        viewModel.setImage(uri)
        //set preview picture
        binding.displayImage.setImageURI(uri)
        //add listeners
        binding.goBackToMainMenuButton.setOnClickListener(){
            //navigate to the main menu
            Navigation.findNavController(it).navigate(SuccessfullyCreatedImageFragmentDirections.actionSuccessfullyCreatedImageFragmentToMainMenuFragment());
        }
        binding.shareImageButton.setOnClickListener(){
            //share image to another app
            startActivity(Intent.createChooser(viewModel.getShareImageIntent(), "Share file"))
        }

        //return the binding root
        return binding.root
    }

    override fun onResume() {
        //if its the first time the user opens this fragment run the tutorial that will guide the user
        super.onResume()
        val tutorialKey = TutorialPreferenceKeys.SUCCESSFULLY_CREATED_IMAGE_FRAGMENT_TUTORIAL_KEY.key
        val firstTime = requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE).getBoolean(tutorialKey, true)
        if (firstTime) {
            runTutorial()
            requireActivity().getPreferences(AppCompatActivity.MODE_PRIVATE).edit().putBoolean(tutorialKey, false).apply()
        }
    }

    /**
     * start tutorial for this fragment
     */
    private fun runTutorial() {
        val tutorial = TutorialFragmentIterator(
            arrayOf(
                SuccessfullyCreatedImageTutorialCongratulations(),
                SuccessfullyCreatedImageTutorialShareImage()
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