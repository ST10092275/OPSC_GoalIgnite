package com.example.opsc7213_goalignite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import org.w3c.dom.DocumentFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentStudy.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentStudy : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_study, container, false)

        val button = view.findViewById<Button>(R.id.button)  // Assuming button2 is the one to click

        // Set an onClickListener to the button
        button.setOnClickListener {
            // Create an Intent to start a new activity
            val fragment = GalleryFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
        }
        val buttonFlashcard = view.findViewById<Button>(R.id.button2)  // Assuming button2 is the one to click

        // Set an onClickListener to the button
        buttonFlashcard.setOnClickListener {
            // Create an Intent to start a new activity
            val fragment = FlashcardFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
        }
        // Find the button by its ID
        val buttonDocument = view.findViewById<Button>(R.id.button3)

        // Set an onClickListener to the button
        buttonDocument.setOnClickListener {
            val fragment = DocumentFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
        }

        return view
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentStudy.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentStudy().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}