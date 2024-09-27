package com.example.opsc7213_goalignite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val rName = findViewById<EditText>(R.id.rName)
        val rEmail = findViewById<EditText>(R.id.rEmail)
        val rPassword = findViewById<EditText>(R.id.rPassword)
        val rCPassword = findViewById<EditText>(R.id.rCPassword)

        btnRegister.setOnClickListener {
            val name = rName.text.toString().trim()
            val email = rEmail.text.toString().trim()
            val password = rPassword.text.toString().trim()
            val confirmPassword = rCPassword.text.toString().trim()

            // Validation checks
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Register user with Firebase Authentication
            registerUser(name, email, password)
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Save additional user data to Firestore
                    val user = hashMapOf(
                        "name" to name,
                        "email" to email
                    )

                    db.collection("users").document(auth.currentUser?.uid!!)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Registration successful. You can now log in.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, Login::class.java)
                            startActivity(intent)
                            finish()
                            // Redirect to login
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error saving user info: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
