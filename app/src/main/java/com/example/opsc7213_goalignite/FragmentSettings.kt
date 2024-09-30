package com.example.opsc7213_goalignite

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout

import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore



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

    private lateinit var profileArrow : ImageView
    private lateinit var profilelayout: LinearLayout
    private lateinit var profileFieldsLayout: LinearLayout
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var saveProfileButton: Button



    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
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

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

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

        profileArrow = view.findViewById(R.id.profilearrow)
        profilelayout = view.findViewById(R.id.profilelayout)
        profileFieldsLayout = view.findViewById(R.id.profileFieldsLayout)
        nameEditText = view.findViewById(R.id.nameEditText)
        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        saveProfileButton = view.findViewById(R.id.saveProfileButton)

        saveProfileButton.setOnClickListener {
            saveProfileData()
            try {
                sendMessage()
            } catch (e: Exception) {
                // Handle the error gracefully, logging it and notifying the user
                handleError(e)
            }
        }
        loadUserData()

        switchMode = view.findViewById(R.id.switchMode)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()




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
        profilelayout.setOnClickListener {
            if (profileFieldsLayout.visibility == View.GONE) {
                profileFieldsLayout.visibility = View.VISIBLE
                profileArrow.rotation = 90f // Rotate arrow to indicate expansion
            } else {
                profileFieldsLayout.visibility = View.GONE
                profileArrow.rotation = 0f // Reset arrow rotation
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
    private fun sendMessage() {
        try {
            // Attempt to display a Toast message
            Toast.makeText(context, "Your profile update request has been sent!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            // Handle any errors when trying to display the message
            throw Exception("Failed to send message: ${e.localizedMessage}")
        }
    }


    private fun handleError(e: Exception) {
        // Log the error for debugging purposes
        Log.e("ProfileManagement", "Error: ${e.localizedMessage}")

        // Show an error message to the user
        Toast.makeText(context, "Error sending message: ${e.localizedMessage}", Toast.LENGTH_LONG)
            .show()
    }
    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            // Load the email from FirebaseAuth
            emailEditText.setText(user.email)

            // Load the name from FirebaseAuth or Firestore
            firestore.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")
                        nameEditText.setText(name)
                    }
                }
        }
    }
    private fun saveProfileData() {
        val updatedName = nameEditText.text.toString()
        val updatedEmail = emailEditText.text.toString()
        val updatedPassword = passwordEditText.text.toString()

        val user = auth.currentUser

        user?.let {
            // Update email
            if (updatedEmail.isNotEmpty() && updatedEmail != user.email) {
                user.updateEmail(updatedEmail).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(context, "Failed to update email", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Update password
            if (updatedPassword.isNotEmpty()) {
                user.updatePassword(updatedPassword).addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(context, "Failed to update password", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Update name in Firestore
            val userProfileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(updatedName)
                .build()

            user.updateProfile(userProfileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save name to Firestore
                    val userData = mapOf(
                        "name" to updatedName,
                        "email" to updatedEmail
                    )
                    firestore.collection("users").document(user.uid).set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Update theme based on the user preference
    private fun updateTheme(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


    // Update theme based on the user preference


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