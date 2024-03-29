package com.secrepixel.app.writemessage

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.secrepixel.app.databinding.FragmentWriteMessageBinding
import com.example.imageencryptorlibrary.encryption.PPKeyImageEncryptor
import com.secrepixel.app.*
import com.secrepixel.app.tutorialtools.TutorialFragmentIterator
import com.secrepixel.app.tutorialtools.TutorialPreferenceKeys
import com.secrepixel.app.writemessage.tutorial.WriteMessageTutorialWritingAMessageFragment
import timber.log.Timber
import java.math.BigInteger


/**
 * This fragment is where the user will make their encrypted image
 */
class WriteMessageFragment : Fragment() {

    //fragments view model
    private lateinit var viewModel: WriteMessageViewModel

    //fragments binding
    private lateinit var binding: FragmentWriteMessageBinding

    private lateinit var tutorialView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //=======Extract the key==================
        var args = WriteMessageFragmentArgs.fromBundle(requireArguments())
        var key = args.key
        //=====Initializing Fragment Properties======
        //initializing the binding
        binding = FragmentWriteMessageBinding.inflate(layoutInflater)
        //initializing the view model
        viewModel = ViewModelProvider(this).get(WriteMessageViewModel::class.java)
        viewModel.fragment = this
        viewModel.binding = binding
        viewModel.activity = activity
        viewModel.imageEncryptor = PPKeyImageEncryptor()
        //viewModel.imageEncryptor.setPublicKey(BigInteger(key.modulus), BigInteger(key.publicExponent))
        viewModel.imageEncryptor.setPublicKey(
            BigInteger(key.modulus),
            BigInteger(key.publicExponent)
        )
        //getting the tutorial view
        tutorialView = binding.tutorial
        //initialing listeners
        binding.chooseImageButton.setOnClickListener { this.onClickChooseImage() }
        binding.makeImageButton.setOnClickListener {
            this.onClickMakeImage()
        }
        binding.inputMessageTextView.addTextChangedListener(object: TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                binding.symbolsLeftTextView.text = getString(R.string.symbols_left)+" "+(viewModel.symbolCapacity-s.length)
            }
        })

        //other initializations
        binding.keyUsedTextView.text = getString(R.string.key_used)+" "+key.name

        //=====restoring previous state====
        if (savedInstanceState != null) {
            restoreInstanceState(savedInstanceState)
        }

        return binding.root
    }

    /**
     * Launcher to retrieve image from library, and update the necessary views
     */
    private var retrieveImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data?.data != null) {
                val data: Uri? = result.data!!.data
                setPicture(data)
            }
        }

    /**
     * activates when the choose image button is pressed
     * is opens the users gallery for him to find the image they want to encrypt
     */
    fun onClickChooseImage() {
        Timber.i("choose image bottom clicked")
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        retrieveImageResultLauncher.launch(intent)
    }

    /**
     * encrypts the message into the image and saves it
     */
    fun onClickMakeImage() {

        var usersMessage = binding.inputMessageTextView.text.toString()
        val progress = ProgressDialog.show(requireContext(), "Please Wait",
            "encrypting image", true);
        try {
            var fileName = viewModel.encrypt(usersMessage, progress)
        }catch (e: java.lang.RuntimeException){
            progress.dismiss()
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * saves the current state of the fragment
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("output_image", viewModel.getPicture())
        outState.putString("incomplete_user_message", binding.inputMessageTextView.text.toString())
    }

    /**
     * restores the previous state of the fragment
     */
    fun restoreInstanceState(savedInstanceState: Bundle) {
        setPicture(savedInstanceState.get("output_image") as Uri?)
        var incompleteUserMessage = savedInstanceState.getString("incomplete_user_message")
        if(incompleteUserMessage!=null){
            binding.inputMessageTextView.setText(incompleteUserMessage)
        }
    }

    /**
     * set picture to be changed as well as all the visuals to display image information to the user
     */
    fun setPicture(data: Uri?){
        if (data != null) {
            //how many decimal places to show
            val decimalPlaces = 2.0

            //setting the picture in the view model
            viewModel.setPicture(data)
            //updating the text views to match the selected image
            binding.previewImageView.setImageURI(viewModel.getPicture())
            //creating uri cursor
            val cursor = requireActivity().contentResolver.query(viewModel.getPicture()!!, null, null, null, null)!!
            cursor.moveToFirst()
            //cursor indexes
            val nameColumnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeColumnIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            //setting the text Views
            binding.fragmentImageSelectTextViewSelectImage.visibility = View.GONE
            //binding.fragmentImageSelectTextViewSelectImage.setTextColor(Color.TRANSPARENT)
            //binding.writeMessageFileNameTextView.text = getString(R.string.file_name)+" "+cursor.getString(nameColumnIndex)
            //there are 1048576 bytes in a mega byte
            binding.writeMessageFileSizeTextView.text = getString(R.string.file_size)+" "+
                    (Math.round(cursor.getDouble(sizeColumnIndex)/1048576*Math.pow(10.0,decimalPlaces))/Math.pow(10.0,decimalPlaces))+
                    getString(R.string.mb)
            binding.writeMessageFileTotalSymbolsTextView.text = getString(R.string.total_symbols)+" "+viewModel.symbolCapacity.toString()
            binding.symbolsLeftTextView.text = getString(R.string.symbols_left)+" "+(viewModel.symbolCapacity-binding.inputMessageTextView.text.length)
        }
    }

    override fun onResume() {
        //if its the first time the user opens this fragment run the tutorial that will guide the user
        super.onResume()
        val tutorialKey = TutorialPreferenceKeys.WRITE_MESSAGE_FRAGMENT_TUTORIAL_KEY.key
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
                WriteMessageTutorialWritingAMessageFragment()
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