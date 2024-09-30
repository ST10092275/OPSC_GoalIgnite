package com.example.opsc7213_goalignite

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FlashcardApiService {
    @POST("api/flashcards")
    suspend fun createFlashcard(@Body flashcard: Flashcard): Response<Flashcard>

    @GET("api/flashcards")
    suspend fun getFlashcards(): Response<List<Flashcard>>

    @PUT("api/flashcards/{id}")
    suspend fun updateFlashcard(@Path("id") id: String, @Body flashcard: Flashcard): Response<Flashcard>

    @DELETE("api/flashcards/{id}")
    suspend fun deleteFlashcard(@Path("id") id: String): Response<Unit>
}


