package com.secrepixel.app.keydetails

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.secrepixel.app.databinding.FragmentKeyDetailsPrivateKeyBinding


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
        binding.keyDetailsPrivateKeyModulusTextView.setOnClickListener(){
            setTextToClipboard((it as TextView).text)
        }
        binding.keyDetailsPrivateKeyPrivateExponentTextView.text = key.privateExponent
        binding.keyDetailsPrivateKeyPrivateExponentTextView.setOnClickListener(){
            setTextToClipboard((it as TextView).text)
        }
        binding.keyDetailsPrivateKeyPublicExponentTextView.text = key.publicExponent
        binding.keyDetailsPrivateKeyPublicExponentTextView.setOnClickListener(){
            setTextToClipboard((it as TextView).text)
        }

        return binding.root
    }

    private fun setTextToClipboard(text: CharSequence){
        val clipboard: ClipboardManager? =
            getSystemService(requireContext(), ClipboardManager::class.java)
        if(clipboard!=null) {
            val clip = ClipData.newPlainText("", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }
}