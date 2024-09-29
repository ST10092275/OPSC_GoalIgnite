package com.example.opsc7213_goalignite

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSettings.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSettings : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var themeLayout: LinearLayout
    private lateinit var switchLayout: LinearLayout
    private lateinit var themeArrow: ImageView
    private lateinit var switchMode: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

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
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize views
        themeLayout = view.findViewById(R.id.themeLayout)
        switchLayout = view.findViewById(R.id.switchLayout)
        themeArrow = view.findViewById(R.id.themearrow)
        switchMode = view.findViewById(R.id.switchMode)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        // Load saved theme preference
        val nightMode = sharedPreferences.getBoolean("nightMode", false)
        switchMode.isChecked = nightMode
        updateTheme(nightMode)

        // Set click listener for the theme layout
        themeLayout.setOnClickListener {
            if (switchLayout.visibility == View.GONE) {
                switchLayout.visibility = View.VISIBLE
                themeArrow.rotation = 90f // Rotate arrow to indicate expansion
            } else {
                switchLayout.visibility = View.GONE
                themeArrow.rotation = 0f // Reset arrow rotation
            }
        }

        // Set listener for the switch to toggle theme
        switchMode.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("nightMode", isChecked)
            editor.apply()
            updateTheme(isChecked)
        }

        return view
    }

    // Update theme based on the user preference
    private fun updateTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSettings.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSettings().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}