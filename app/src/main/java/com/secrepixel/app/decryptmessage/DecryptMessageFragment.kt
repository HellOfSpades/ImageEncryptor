package com.secrepixel.app.decryptmessage

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.secrepixel.app.writemessage.WriteMessageFragmentArgs
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import com.secrepixel.app.tutorialtools.TutorialPreferenceKeys
import com.secrepixel.app.R
import com.secrepixel.app.tutorialtools.TutorialFragmentIterator
import com.secrepixel.app.databinding.FragmentDecryptMessageBinding
import com.secrepixel.app.decryptmessage.tutorial.DecryptMessageTutorialDecryptingImage
import java.math.BigInteger

/**
 * The purpose of this fragment is to decrypt the message from the chosen image
 */
class DecryptMessageFragment : Fragment() {

    //fragments binding
    lateinit var binding: FragmentDecryptMessageBinding
    //fragments view model
    lateinit var viewModel: DecryptMessageViewModel
    lateinit var tutorialView: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //=======Extract the key==================
        var args = WriteMessageFragmentArgs.fromBundle(requireArguments())
        var key = args.key

        //initializing the view model
        viewModel = ViewModelProvider(this).get(DecryptMessageViewModel::class.java)
        viewModel.key = key
        viewModel.activity = activity
        viewModel.imageEncryptor = PPKeyImageEncryptor()
        viewModel.imageEncryptor.setPublicPrivateKey(BigInteger(key.modulus),BigInteger(key.publicExponent),BigInteger(key.privateExponent))

        //initializing the binding
        binding = FragmentDecryptMessageBinding.inflate(layoutInflater)

        tutorialView = binding.tutorial
        //add click listeners
        binding.chooseImageButtonDecryptMessageView.setOnClickListener(){
            onClickChooseImage()
        }
        binding.decryptButtonDecryptMessageView.setOnClickListener(){
            val decrypted = viewModel.decrypt();
            var message: String
            if(decrypted==null){
                message = "can't extract message from this image"
            }else{
                message = decrypted
            }
            //currently the output message is toasted, but it will later be changed to something else
            Toast.makeText(this.context,  message, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }




    //Launcher to retrieve image from library
    @RequiresApi(Build.VERSION_CODES.Q)
    private var retrieveImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data?.data != null) {
                val data: Uri? = result.data!!.data
                if (data != null) {
                    viewModel.setPicture(data)
                    binding.decryptedImagePreview.setImageURI(viewModel.getPicture())
                }
            }
        }

    /**
     * opens the gallery for the user to pick the image they want to retrieve
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun onClickChooseImage() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        retrieveImageResultLauncher.launch(intent)
    }


    override fun onResume() {
        //if its the first time the user opens this fragment run the tutorial that will guide the user
        super.onResume()
        val tutorialKey = TutorialPreferenceKeys.DECRYPT_MESSAGE_FRAGMENT_TUTORIAL_KEY.key
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
                DecryptMessageTutorialDecryptingImage()
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