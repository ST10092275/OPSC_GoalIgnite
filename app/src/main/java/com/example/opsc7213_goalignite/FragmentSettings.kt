package com.example.opsc7213_goalignite

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import java.util.Locale


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

    private lateinit var themeLayouts: LinearLayout
    private lateinit var themeLayout: LinearLayout
    private lateinit var switchLayout: LinearLayout
    private lateinit var themeArrow: ImageView
    private lateinit var switchMode: SwitchCompat
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var changeMyLanguage: Button
    private lateinit var languageSwitch: LinearLayout
    private lateinit var layoutSwitch: LinearLayout
    private lateinit var supportLayout:LinearLayout
    private lateinit var dropdownLayout: LinearLayout
    private lateinit var supportFormLayout:LinearLayout

    private var nightMode: Boolean = false




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
        changeMyLanguage = view.findViewById(R.id.changeMyLanguage)
        layoutSwitch = view.findViewById(R.id.LayoutSwitch)
        dropdownLayout = view.findViewById(R.id.dropdownLayout)
        supportLayout = view.findViewById(R.id.supportLayout)
        languageSwitch = view.findViewById(R.id.LanguageSwitch)
        themeLayouts = view.findViewById(R.id.themeLayouts)
        supportFormLayout = view.findViewById(R.id.supportFormLayout)

        // Initialize SharedPreferences
        sharedPreferences =
            requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        // Load saved theme preference
        val nightMode = sharedPreferences.getBoolean("nightMode", false)
        switchMode.isChecked = nightMode
        updateTheme(nightMode)

        themeLayouts.setOnClickListener {
            // Replace the current fragment with FAQFragment
            replaceWithFAQFragment()
        }

        supportFormLayout.setOnClickListener {
            //replace with current fragment with supportForm
            replaceWithSupportForm()
        }



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

        supportLayout.setOnClickListener {
            toggleDropdown()
        }

        // Set the click listener for change language button
        languageSwitch.setOnClickListener {
            toggleLayoutSwitch()
        }

        changeMyLanguage.setOnClickListener {
            // Code to show language options
            showChangeLanguageDialog()
        }
        return view
    }

    private fun replaceWithFAQFragment() {
        val fragment = FragmentFaq()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment) // Replace 'fragment_container' with your actual container ID.
            .addToBackStack(null)
            .commit()
    }

    private fun replaceWithSupportForm() {
        val supportFormFragment = SupportForm()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, supportFormFragment) // Use the correct container ID here
            .addToBackStack(null)
            .commit()
    }
    private fun toggleLayoutSwitch() {
        val layoutSwitch = view?.findViewById<LinearLayout>(R.id.LayoutSwitch)
        if (layoutSwitch?.visibility == View.GONE) {
            layoutSwitch.visibility = View.VISIBLE
        } else {
            layoutSwitch?.visibility = View.GONE
        }
    }
    private fun toggleDropdown() {
        if (dropdownLayout.visibility == View.GONE) {
            dropdownLayout.visibility = View.VISIBLE
        } else {
            dropdownLayout.visibility = View.GONE
        }
    }
    private fun showChangeLanguageDialog() {
        // Languages to display
        val listItems = arrayOf("isiZulu", "English")

        // Create AlertDialog
        val mBuilder = AlertDialog.Builder(requireContext())
        mBuilder.setTitle("Choose language...")
        mBuilder.setSingleChoiceItems(listItems, -1) { dialogInterface, i ->
            when (i) {
                0 -> {
                    // Zulu
                    setLocale("zu")
                    saveLanguageToPreferences("zu")  // Save the language selection
                    activity?.recreate()  // Recreate the activity to apply language changes
                }
                1 -> {
                    // English
                    setLocale("en")
                    saveLanguageToPreferences("en")  // Save the language selection
                    activity?.recreate()
                }
            }

            // Dismiss the alert dialog once a language is selected
            dialogInterface.dismiss()
        }

        // Create and show the dialog
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = requireContext().resources.configuration
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }

    private fun saveLanguageToPreferences(languageCode: String) {
        // Save selected language to SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("selected_language", languageCode)
            apply()  // Save the changes asynchronously
        }
    }

    private fun loadLanguageFromPreferences() {
        // Load the saved language from SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("Settings", android.content.Context.MODE_PRIVATE)
        val languageCode = sharedPref.getString("selected_language", "en")  // Default to English if not set
        setLocale(languageCode!!)
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