package com.example.gravityball

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController

/**
 * A simple [Fragment] subclass.
 * Use the [GamePlayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GamePlayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_game_play, container, false)
        val gameOverButton = view.findViewById<Button>(R.id.game_over)

        gameOverButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_gamePlayFragment2_to_gameOverFragment)
        }
        return view
    }
}