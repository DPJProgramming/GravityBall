package com.example.gravityball

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment


class GamePlayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_play, container, false)

        //create an array of platforms from the drawable folder
        val platformList = arrayOf(R.drawable.platform1, R.drawable.platform2, R.drawable.platform3,
                                   R.drawable.platform4, R.drawable.platform5, R.drawable.platform6,
                                   R.drawable.platform7, )



        //set platform to imageView and initial position
        val platform = view.findViewById<ImageView>(R.id.platForm)
        platform.y = -40f

        //set which drawable the platform is
        val random = (0 until platformList.size).random()
        platform.setImageResource(platformList[random])

        //set the height of platform so they are all the same height
        platform.layoutParams.height = 40

        //move the platform
        platform.animate().translationYBy(-2100f).duration = 3500

        //remove platform from view


        return view
    }
}