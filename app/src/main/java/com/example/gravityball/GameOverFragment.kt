package com.example.gravityball

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import kotlin.system.exitProcess

class GameOverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_over, container, false)
        val playAgainButton = view.findViewById<Button>(R.id.play_again)
        val quitButton = view.findViewById<Button>(R.id.quit)

        playAgainButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_gameOverFragment_to_startScreenFragment)
        }

        quitButton.setOnClickListener{
            val activity = MainActivity()
            activity.finish()
            exitProcess(0)
        }


        return view
    }
}
