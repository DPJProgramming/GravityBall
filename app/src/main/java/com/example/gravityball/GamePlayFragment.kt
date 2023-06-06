package com.example.gravityball

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import kotlin.random.Random


class GamePlayFragment : Fragment() {

    val handler = Handler(Looper.getMainLooper())

    //create an array of platforms from the drawable folder
    var platformList = arrayOf(
        R.drawable.platform1, R.drawable.platform2, R.drawable.platform3,
        R.drawable.platform4, R.drawable.platform5, R.drawable.platform6,
        R.drawable.platform7,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_play, container, false)

        // Initialize platform ImageView
        var platform = view.findViewById<ImageView>(R.id.platform)

        animate(platform)

//                //set platform to imageView and initial position
//                val platform = view.findViewById<ImageView>(R.id.platForm)
//                platform.y = -40f
//
//                //set which drawable the platform is
//                val random = (0 until platformList.size).random()
//                platform.setImageResource(platformList[random])
//
//                //gets the starting postion of the platform
//               val start = (platform.height.toFloat() + (view?.height?.toFloat() ?: 0f))
//
//
//                //set the height of platform so they are all the same height
//                platform.setMaxHeight(20)
//
//                //move the platform
//                platform.animate().translationYBy(-2100f).duration = 3500
//
//                //remove platform from view
//
//                platform.setTranslationY(0f);
//
//                handler.postDelayed(this, 4000)
//
        return view
    }

    private fun animate(platform: ImageView){
        //make an array of directions to use later
        val position = arrayOf<String>("")

        //get view constraints to change position of platforms
//        val gamePlayScreen: ConstraintLayout = requireView().findViewById(R.id.gamePlayFragment)
//        val constraintSet = ConstraintSet()
//        constraintSet.clone(gamePlayScreen)

        //set platform to random platform in list
        val random = Random.nextInt(0, platformList.size)
        platform.setImageResource(platformList[random])

        //set the height of platform so they are all the same height
        platform.getLayoutParams().height = 50

        //get starting, desired end position, duration, and right of left positioning
        val start = (platform.height.toFloat() + (view?.height?.toFloat() ?: 0f))
        val end = -2100f
        var speed: Long = 2000
//        constraintSet.connect(
//            platform.id,
//            ConstraintSet.LEFT,
//            ConstraintSet.PARENT_ID,
//            ConstraintSet.LEFT,
//        )

        //move the platform
        val animation = platform.animate().translationYBy(end).setDuration(speed)
        animation.interpolator = AccelerateInterpolator()
        animation.start()

        //resets view to original position and calls animate again after delay
        animation.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                platform.y = start
                handler.postDelayed({ animate(platform) }, 0)
            }
        })
    }
}