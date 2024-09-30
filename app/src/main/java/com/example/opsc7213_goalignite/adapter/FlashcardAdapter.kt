package com.example.opsc7213_goalignite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7213_goalignite.Flashcard
import com.example.opsc7213_goalignite.R


class FlashcardAdapter(private val flashcards: List<Flashcard>) : RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {

        class FlashcardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val questionTextView: TextView = view.findViewById(R.id.questionTextView)
            val answerTextView: TextView = view.findViewById(R.id.answerTextView)
            val moduleTextView: TextView = view.findViewById(R.id.moduleTextView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_flashcard, parent, false)
            return FlashcardViewHolder(view)
        }

        override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
            val flashcard = flashcards[position]
            holder.questionTextView.text = flashcard.question
            holder.answerTextView.text = flashcard.answer
            holder.moduleTextView.text = flashcard.module

            // Set click listener to toggle the answer visibility
            holder.itemView.setOnClickListener {
                if (holder.answerTextView.visibility == View.GONE) {
                    holder.answerTextView.visibility = View.VISIBLE
                } else {
                    holder.answerTextView.visibility = View.GONE
                }
            }
        }

        override fun getItemCount(): Int = flashcards.size

}
