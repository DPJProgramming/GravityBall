package com.example.gravityball

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
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
        val screen: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.display)

        screen.setOnTouchListener(object: View.OnTouchListener{
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(view: View, move: MotionEvent):Boolean{

                var eventType: Int = move.getActionMasked()

                when(eventType){
                    MotionEvent.ACTION_DOWN -> {
                    Log.i("poop", "touck down is working")
                    }
                    MotionEvent.ACTION_UP -> {
                    Log.i("poop", "touck up is working")
                    }
                    MotionEvent.ACTION_MOVE -> {
                    Log.i("poop", "touck move is working")
                    }
                }
                return true;
            }
        })

        // Initialize platform ImageView
        var platform = view.findViewById<ImageView>(R.id.platform)



        animate(platform)

        return view
    }

    private fun animate(platform: ImageView){
        //make an array of directions to use later
        val position = arrayOf<String>("")

        //set platform to random platform in list
        val random = Random.nextInt(0, platformList.size)
        platform.setImageResource(platformList[random])

        //set the height of platform so they are all the same height
        platform.getLayoutParams().height = 50

        //get starting, desired end position, duration, and right of left positioning
        val start = (platform.height.toFloat() + (view?.height?.toFloat() ?: 0f))
        val end = -2100f
        var speed: Long = 2000

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