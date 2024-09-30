package com.example.opsc7213_goalignite

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class FlashcardActivity : AppCompatActivity() {
    private lateinit var api: FlashcardApiService
    private lateinit var createButton: Button
    private lateinit var questionInput: EditText
    private lateinit var answerInput: EditText
    private lateinit var moduleInput: EditText
    private lateinit var flashcardContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcard)


        createButton = findViewById(R.id.createButton)
        questionInput = findViewById(R.id.questionInput)
        answerInput = findViewById(R.id.answerInput)
        moduleInput = findViewById(R.id.moduleInput)
        flashcardContainer = findViewById(R.id.flashcardContainer)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3004/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(FlashcardApiService::class.java)

        createButton.setOnClickListener {
            val module = moduleInput.text.toString()
            val question = questionInput.text.toString()
            val answer = answerInput.text.toString()

            if (module.isNotEmpty() && question.isNotEmpty() && answer.isNotEmpty()) {
                val flashcard = Flashcard(id = UUID.randomUUID().toString(), module, question, answer)
                saveFlashcard(flashcard)
            }
        }
        loadFlashcards()
    }
    private fun saveFlashcard(flashcard: Flashcard) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.createFlashcard(flashcard)
                if (response.isSuccessful) {
                    val createdFlashcard = response.body()
                    if (createdFlashcard != null) {
                        withContext(Dispatchers.Main) {
                            displayFlashcard(createdFlashcard)
                        }
                    } else {
                        Log.e("FlashcardActivity", "Response was successful but body is null")
                    }
                } else {
                    // Log the error from the server
                    Log.e("FlashcardActivity", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                Log.e("FlashcardActivity", "Exception: ${e.message}")
            }
        }
    }



    private fun loadFlashcards() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = api.getFlashcards()
                if (response.isSuccessful) {
                    val flashcards = response.body()
                    if (flashcards != null) {
                        withContext(Dispatchers.Main) {
                            flashcards.forEach { displayFlashcard(it) }
                        }
                    } else {
                        Log.e("FlashcardActivity", "No flashcards found")
                    }
                } else {
                    Log.e("FlashcardActivity", "Error loading flashcards: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("FlashcardActivity", "Exception: ${e.message}")
            }
        }
    }


    private fun displayFlashcard(flashcard: Flashcard) {
        val textView = TextView(this).apply {
            text = "${flashcard.module}\nQuestion: ${flashcard.question}\nAnswer: ${flashcard.answer}"
            setPadding(16, 16, 16, 16)
            setBackgroundColor(Color.LTGRAY)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        flashcardContainer.addView(textView)
    }
    // Find the button by its ID

}








