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
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
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
        dropTheBall(ball, screenHeight, view)

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
                var platform = view.findViewById<ImageView>(R.id.platform)
                val touchPoint: Float = move.getX()
                val touch: Int = move.getActionMasked()

                when (touch) {
                    MotionEvent.ACTION_DOWN -> {
                        //Log.i("touchDown", "touch down is working")

                        //if right side of screen is pressed, move ball right
                        if (touchPoint < screenWidth && touchPoint > screenWidth / 2) {
                            //Log.i("right", "touched right side")

                            var moveBall = ball.animate().translationXBy(3000f).setDuration(1500).setInterpolator(LinearInterpolator())

                            //makes sure ball stays inbounds
                            moveBall.setUpdateListener {
                                val ballPosition = ball.x
                                //ball.y = platform.y -ball.height

                                if (ballPosition + ball.width >= screenWidth) {

                                    moveBall.cancel()
                                    dropTheBall(ball, screenHeight, view)
                                }
                                if((ball.x + ball.width >= platform.x) && (ball.x <= platform.x + platform.width) && (ball.y + ball.height >= platform.y + platform.height) && (ball.y <= platform.y + platform.height)){
                                    ball.y = platform.y -ball.height
                                    var raiseBall = ball.animate().translationY(-2100f).setDuration(4000)
                                }
                                else{
                                    dropTheBall(ball, screenHeight, view)
                                }
                            }
                        }

                        //if left is pressed move left unless ball is at edge of screen
                        else {
                            //Log.i("touchDown", "touched left side")

                            //animate ball left
                            var moveBall = ball.animate().translationXBy(-3000f).setDuration(1500).setInterpolator(LinearInterpolator())

                            //sets listener for ball's position so it doesn't go out of bounds
                            moveBall.setUpdateListener {
                                val ballPosition = ball.x
                                if (ballPosition <= 0) {
                                    moveBall.cancel()
                                    dropTheBall(ball, screenHeight, view)
                                }
                                if((ball.x + ball.width >= platform.x) && (ball.x <= platform.x + platform.width) && (ball.y + ball.height >= platform.y + platform.height) && (ball.y <= platform.y + platform.height)){
                                    ball.y = platform.y -ball.height
                                    var raiseBall = ball.animate().translationY(-2100f).setDuration(4000)
                                }
                                else{
                                    dropTheBall(ball, screenHeight, view)
                                }
                            }
                        }
                    }

                    //no longer touching the screen so the ball stops moving
                    MotionEvent.ACTION_UP -> {
                        //Log.i("touchUp", "touch up is working")
                        ball.animate().cancel()
                        dropTheBall(ball, screenHeight, view)
                    }
                }
                return true
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun dropTheBall(ball: ImageView, screenHeight: Int, view: View) {
        val screen: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.display)
        val set = ConstraintSet()
        set.clone(screen)
        val initialBallY = ball.y

        var ballDrop = ball.animate().translationYBy(screenHeight.toFloat()).setDuration(3000)
        val platform = view.findViewById<ImageView>(R.id.platform)
        ballDrop.setUpdateListener {
            var ballY = ball.y - 10
            var ballX = ball.x
            var platformX = platform.x
            var platformY = platform.y
            var ballStopped = false

            if (ballY + ball.height + 225 >= screenHeight) {
                //ballDrop.cancel()
                var ballAtBottom = ball.animate().translationYBy(-2100f).setDuration(20000)
                ball.setOnTouchListener { view, event ->
                    var platformX = platform.x
                    var platformY = platform.y

                    if((ballX + ball.width >= platformX) && (ballX <= platformX + platform.width) && (ballY + ball.height >= platformY + platform.height) && (ballY <= platformY + platform.height)){
                        var raiseBall = ball.animate().translationY(-2100f).setDuration(4000)
                        raiseBall.setUpdateListener{
                            if (ball.y<= 0) {
                                raiseBall.cancel()
                                view.findNavController().navigate(R.id.action_gamePlayFragment2_to_gameOverFragment)
                            }
                            if (ball.y<= 0) {
                                ballAtBottom.cancel()
                                view.findNavController().navigate(R.id.action_gamePlayFragment2_to_gameOverFragment)
                            }
                        }
                    }
                    false
                }
            }

            if((ballX >= platformX) && (ballX <= platformX + platform.width) && (ballY + ball.height >= platformY + platform.height) && (ballY <= platformY + platform.height)){

//                ball.layoutParams = params
//                ballDrop.cancel()
//                set.connect(ball.id, ConstraintSet.BOTTOM, platform.id, ConstraintSet.TOP, 0)
//                set.applyTo(screen)
                Log.i("Platform touched", "Ball touched a platform")
                var raiseBall = ball.animate().translationY(-2100f).setDuration(4000)
                raiseBall.setUpdateListener{
                    ball.y = platform.y -ball.height

                    if (ball.y<= 0) {
                        raiseBall.cancel()
                        view.findNavController().navigate(R.id.action_gamePlayFragment2_to_gameOverFragment)
                    }
                }
            }

            if (ballY<= 0) {
                ballDrop.cancel()
                view.findNavController().navigate(R.id.action_gamePlayFragment2_to_gameOverFragment)
            }
        }
    }

    private fun animatePlatforms(view: View, screenWidth: Int, platform: ImageView, screenHeight: Int){

        //make an array of directions to use for positioning the platform
        val position = arrayOf<Int>(0, screenWidth/2 - platform.width/2, screenWidth - platform.width)
        val randomPosition = position[Random.nextInt(0, position.size-1)]

        //set platform to random platform in list
        val random = Random.nextInt(0, platformList.size)
        platform.setImageResource(platformList[random])

        //get the width of drawable and set imageview width to match
        val platformWidth = platform.drawable.intrinsicWidth
        platform.maxWidth = platformWidth

        //set the height of platform so they are all the same height
        platform.layoutParams.height = 50

        //set starting, desired end position, duration, and right left positioning
        val end = -1920f
        val speed: Long = 2000
        platform.x = position[Random.nextInt(0, position.size)].toFloat()

        //move the platform
        val animation = platform.animate().translationYBy(end).setDuration(speed).setInterpolator(LinearInterpolator())

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