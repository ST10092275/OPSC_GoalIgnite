package com.example.opsc7213_goalignite




import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.opsc7213_goalignite.adapter.MusicAdapter

class FragmentTimer : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var musicListView: ListView

    // List of music file names and their corresponding raw resource IDs
    private val musicList = listOf("Sunsent", "Beauty", "Warm Memories", "White Petals", "Magical Moments")
    private val musicFiles = listOf(R.raw.sunset, R.raw.beauty, R.raw.warm_memories, R.raw.white__petals, R.raw.magical_moments)
    private val musicImages = listOf( R.drawable.sun, R.drawable.beauty, R.drawable.memories, R.drawable.pink, R.drawable.magics)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        // Initialize the ListView and set up the adapter
        musicListView = view.findViewById(R.id.music_list_view)
        val adapter = MusicAdapter(requireContext(), musicList, musicImages)
        musicListView.adapter = adapter

        // Set up item click listener to play the selected song
        musicListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Stop any currently playing music before starting new one
            mediaPlayer?.release()

            // Initialize MediaPlayer with the selected music file
            mediaPlayer = MediaPlayer.create(requireContext(), musicFiles[position])
            mediaPlayer?.start()
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        // Release MediaPlayer resources when the fragment is destroyed
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
