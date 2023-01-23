package com.secrepixel.app.keydetails

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.secrepixel.app.databinding.FragmentKeyDetailsPublicKeyBinding

/**
 * Fragment that displays the key information that can be shared with others
 */
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

        //set text views to have the key details
        binding.keyDetailsPublicKey.text = key.modulus+":"+key.publicExponent
        binding.keyDetailsPublicKey.setOnClickListener(){
            setTextToClipboard((it as TextView).text)
        }
        return binding.root
    }

    private fun setTextToClipboard(text: CharSequence){
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        if(clipboard!=null) {
            val clip = ClipData.newPlainText("", text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(requireContext(), "copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }
}