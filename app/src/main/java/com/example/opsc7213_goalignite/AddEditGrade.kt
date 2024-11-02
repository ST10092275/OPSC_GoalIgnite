package com.example.opsc7213_goalignite

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.opsc7213_goalignite.model.GradeModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//This whole code was taken from YOUTUBE
//https://www.youtube.com/watch?v=4ZFde7We0H8&list=PLNSnaPe-iLVKpRUqg1yv5JqM_4DQN8Ofo
// Jowel Ahmed - CONTACT APPLICATION BUT CHANGED AS THE "ADD GRADE"

class AddEditGrade : AppCompatActivity() {

    // Declare an ImageView for displaying a subject-related image or icon
    private lateinit var subject: ImageView
    // Declare an EditText for inputting the module name
    private lateinit var moduleName: EditText
    // Declare an EditText for inputting the module mark
    private lateinit var moduleMark: EditText
    // Declare a Button for adding new grades
    private lateinit var newGradesButton: Button
    // Declare a String variable to hold the module name
    private lateinit var module: String
    private lateinit var mark: String
    // Declare an instance of DatabaseHP for interacting with the database
    private lateinit var databaseHP: DatabaseHP



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edit_grade)

        // Initialize the back arrow
        val backArrow: ImageView = findViewById(R.id.backArrow)
        backArrow.setOnClickListener {
            // Navigate back to the previous activity
            finish() // This will close the current activity and return to the previous one
        }

        databaseHP = DatabaseHP(this)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // This is where the UI components are initialized after setting the content view
        subject = findViewById(R.id.subject)
        moduleName = findViewById(R.id.moduleName)
        moduleMark = findViewById(R.id.moduleMark)
        newGradesButton = findViewById(R.id.newGradesButton)


        newGradesButton.setOnClickListener { // Create a new instance of GradeModel
            saveData() // N
            // Call the saveData() function when the button is clicked
        }

    }
    // Function to save the entered data
    private fun saveData() {
        // Get the text from the EditText fields and convert them to strings
        module = moduleName.text.toString()
        mark = moduleMark.text.toString()


        // Validate input
        if (module.isNotEmpty() && mark.isNotEmpty()) {
            // Use a coroutine for database operations
            CoroutineScope(Dispatchers.IO).launch {
                val id = databaseHP.insertGrade(module, mark) // Insert grade into the database
                runOnUiThread {
                    Toast.makeText(applicationContext, "Inserted: $id", Toast.LENGTH_SHORT).show()
                    clearInputFields() // Clear input fields after saving
                }
            }
        } else {
            Toast.makeText(applicationContext, "Please enter both module and mark.", Toast.LENGTH_SHORT).show()
        }

        // After successful save

    }
    // Function to clear input fields
    private fun clearInputFields() {
        moduleName.text.clear()
        moduleMark.text.clear()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}


