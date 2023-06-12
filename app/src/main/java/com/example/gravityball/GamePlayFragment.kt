package com.example.gravityball

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlin.random.Random


class GamePlayFragment : Fragment() {

    val handler = Handler(Looper.getMainLooper())

    //create an array of platforms from the drawable folder
    var platformList = arrayOf(
        R.drawable.platform1, R.drawable.platform2, R.drawable.platform3,
        R.drawable.platform4, R.drawable.platform5, R.drawable.platform6,
        R.drawable.platform7,
    )

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game_play, container, false)
        val screen: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.display)
        val ball = view.findViewById<ImageView>(R.id.ball)
        val screenWidth: Int = resources.displayMetrics.widthPixels
        val screenHeight: Int = (resources.displayMetrics.heightPixels)
        val platform = view.findViewById<ImageView>(R.id.platform)

        //set animation for ball falling
        dropTheBall(ball, screenHeight, view, )

        //set touch controls and animate ball
        moveTheBall(screen, screenWidth, ball, screenHeight)

        //begin platform animations
        animatePlatforms(screen, screenWidth, platform, screenHeight)

        return view
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun moveTheBall(screen: ConstraintLayout, screenWidth: Int, ball: ImageView, screenHeight: Int) {
        screen.setOnTouchListener(object : View.OnTouchListener {
            @RequiresApi(Build.VERSION_CODES.R)
            @SuppressLint("ClickableViewAccessibility")

            override fun onTouch(view: View, move: MotionEvent): Boolean {
                val touchPoint: Float = move.getX()
                val touch: Int = move.getActionMasked()

                when (touch) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.i("touchDown", "touch down is working")

                        //if right side of screen is pressed, move ball right
                        if (touchPoint < screenWidth && touchPoint > screenWidth / 2) {
                            Log.i("right", "touched right side")

                            var moveBall = ball.animate().translationXBy(1500f).setDuration(1500)

                            //makes sure ball stays inbounds
                            moveBall.setUpdateListener {
                                val ballPosition = ball.x

                                if (ballPosition + ball.width >= screenWidth) {
                                    moveBall.cancel()
                                    dropTheBall(ball, screenHeight, view)
                                }
                            }
                        }

                        //if left is pressed move left unless ball is at edge of screen
                        else {
                            Log.i("touchDown", "touched left side")

                            //animate ball left
                            var moveBall = ball.animate().translationXBy(-1500f).setDuration(1500)

                            //sets listener for ball's position so it doesn't go out of bounds
                            moveBall.setUpdateListener {
                                val ballPosition = ball.x
                                if (ballPosition <= 0) {
                                    moveBall.cancel()
                                    dropTheBall(ball, screenHeight, view)
                                }
                            }
                        }
                    }

                    //no longer touching the screen so the ball stops moving
                    MotionEvent.ACTION_UP -> {
                        Log.i("touchUp", "touch up is working")
                        ball.animate().cancel()
                        dropTheBall(ball, screenHeight, view)
                    }
                }
                return true
            }
        })
    }

    private fun dropTheBall(ball: ImageView, screenHeight: Int, view: View) {
        val ballDrop = ball.animate().translationYBy(screenHeight.toFloat()).setDuration(3000)
        val platform = view.findViewById<ImageView>(R.id.platform)
        ballDrop.setUpdateListener {
            var ballPosition = ball.y

//            if(ball.y - ball.height >= platform.y){
//                ballDrop.cancel()
//                val ballUp = ball.animate().translationYBy(-2100f).setDuration(3000)
//            }

            if (ballPosition + ball.height + 225 >= screenHeight) {
                ballDrop.cancel()
            }

            if (ballPosition <= 0) {
                ballDrop.cancel()
                view.findNavController().navigate(R.id.action_gamePlayFragment2_to_gameOverFragment)
            }
        }
    }

    private fun animatePlatforms(view: View, screenWidth: Int, platform: ImageView, screenHeight: Int){
        // Initialize platform ImageView
        //val platform = view.findViewById<ImageView>(R.id.platform)

        //make an array of directions to use for positioning the platform
        val position = arrayOf<Int>(0, screenWidth/2 - platform.width/2, screenWidth - platform.width)
        val randomPosition = position[Random.nextInt(0, position.size-1)]

        //set platform to random platform in list
        val random = Random.nextInt(0, platformList.size)
        platform.setImageResource(platformList[random])

        //set the height of platform so they are all the same height and the width to match drawable
        platform.layoutParams.height = 50

        //get starting, desired end position, duration, and right left positioning
        val end = -2100f
        val speed: Long = 3000
        platform.x = position[Random.nextInt(0, position.size)].toFloat()

        //move the platform
        val animation = platform.animate().translationYBy(end).setDuration(speed)

        //resets view to original position and calls animate again after delay
        animation.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                platform.y = screenHeight + 40.toFloat()
                platform.x = (screenWidth/2 - platform.width/2).toFloat()
                handler.postDelayed({ animatePlatforms(view, screenWidth, platform, screenHeight) }, 0)
            }
        })
    }
}