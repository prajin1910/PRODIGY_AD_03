package com.example.stopwatch

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var timerTextView: TextView
    private lateinit var startPauseButton: Button
    private lateinit var resetButton: Button

    private var isRunning = false
    private var elapsedTime = 0L
    private var startTime = 0L

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            val currentTime = System.currentTimeMillis()
            val totalElapsedTime = elapsedTime + (if (isRunning) currentTime - startTime else 0)

            val hours = (totalElapsedTime / (1000 * 60 * 60)) % 24
            val minutes = (totalElapsedTime / (1000 * 60)) % 60
            val seconds = (totalElapsedTime / 1000) % 60
            val milliseconds = (totalElapsedTime % 1000) / 10

            val timeText = String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
            timerTextView.text = timeText

            if (isRunning) {
                handler.postDelayed(this, 10) // Update every 10 ms for smooth display
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        timerTextView = findViewById(R.id.timerTextView)
        startPauseButton = findViewById(R.id.startPauseButton)
        resetButton = findViewById(R.id.resetButton)

        startPauseButton.setOnClickListener {
            if (isRunning) {
                // Pause the timer
                isRunning = false
                elapsedTime += System.currentTimeMillis() - startTime
                handler.removeCallbacks(updateTimeRunnable)
                startPauseButton.text = "Start"
            } else {
                // Start or resume the timer
                isRunning = true
                startTime = System.currentTimeMillis()
                handler.post(updateTimeRunnable)
                startPauseButton.text = "Pause"
            }
        }

        resetButton.setOnClickListener {
            // Reset the timer
            isRunning = false
            elapsedTime = 0L
            handler.removeCallbacks(updateTimeRunnable)
            timerTextView.text = "00:00:00.00"
            startPauseButton.text = "Start"
        }
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateTimeRunnable)
    }
}
