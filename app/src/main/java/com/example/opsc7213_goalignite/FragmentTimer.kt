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
import android.os.Handler
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Locale
import com.example.opsc7213_goalignite.adapter.MusicAdapter

class FragmentTimer : Fragment() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var musicListView: ListView

    // List of music file names and their corresponding raw resource IDs
    private val musicList = listOf("Sunsent", "Beauty", "Warm Memories", "White Petals", "Magical Moments")
    private val musicFiles = listOf(R.raw.sunset, R.raw.beauty, R.raw.warm_memories, R.raw.white__petals, R.raw.magical_moments)
    private val musicImages = listOf( R.drawable.sun, R.drawable.beauty, R.drawable.memories, R.drawable.pink, R.drawable.magics)








// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentTimer.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentTimer : Fragment() {
    private var param1: String? = null
    private var param2: String? = null


    private var duration: Int = 120 // duration in seconds
    private var running = false
    private var wasRunning = false
    private var seconds = 0
    private lateinit var textView10: TextView
    private lateinit var handler: Handler


   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        savedInstanceState?.let {
            seconds = it.getInt("seconds")
            running = it.getBoolean("running")
            wasRunning = it.getBoolean("wasRunning")
        }
    }


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

        textView10 = view.findViewById(R.id.textView10)
        val startButton: FloatingActionButton = view.findViewById(R.id.start)
        val stopButton: FloatingActionButton = view.findViewById(R.id.stop)
        val resetButton: FloatingActionButton = view.findViewById(R.id.reset)

        handler = Handler()

        startButton.setOnClickListener { running = true }
        stopButton.setOnClickListener { running = false }
        resetButton.setOnClickListener {
            running = false
            seconds = 0
        }

        runTimer()
        return view
    }






    private fun runTimer() {
        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60

                val time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs)
                textView10.text = time

                if (running) {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running)
        outState.putBoolean("wasRunning", wasRunning)
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning) {
            running = true
        }
    }


    companion object {


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentTimer().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }




