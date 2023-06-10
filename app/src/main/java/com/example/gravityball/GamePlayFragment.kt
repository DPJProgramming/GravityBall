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
import android.view.animation.AnimationSet
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
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
        val screenHeight: Int = resources.displayMetrics.widthPixels

        //set animation for ball falling
        //ball.animate().translationYBy(screenHeight.toFloat()).setDuration(2000)
        //val ballDrop.interpolator = AccelerateInterpolator()
        //animation.start()
        //val ballAnimationSet = AnimationSet(true)

        //set touch controls and animate ball
        screen.setOnTouchListener(object: View.OnTouchListener{
            @RequiresApi(Build.VERSION_CODES.R)
            @SuppressLint("ClickableViewAccessibility")

            override fun onTouch(view: View, move: MotionEvent):Boolean{
                val touchPoint: Float = move.getX()
                //val screenWidth: Int = resources.displayMetrics.widthPixels
                //val windowMetrics = activity!!.windowManager.currentWindowMetrics
                //val screenWidth = windowMetrics.bounds.width()
                val touch: Int = move.getActionMasked()

                when(touch){
                    MotionEvent.ACTION_DOWN -> {
                        Log.i("touchDown", "touck down is working")

                        //if right side of screen is pressed
                        if(touchPoint < screenWidth && touchPoint > screenWidth/2){
                            Log.i("right", "touched right side")

                            var moveBall = ball.animate().translationXBy(1500f).setDuration(1500)

                            //makes sure ball stays inbounds
                            moveBall.setUpdateListener {
                                val ballPosition = ball.x

                                if (ballPosition + ball.width >= screenWidth) {
                                    moveBall.cancel()
                                }
                            }
                        }

                        //if left is pressed
                        else{
                            Log.i("touchDown", "touched left side")

                            //animate ball left
                            var moveBall = ball.animate().translationXBy(-1500f).setDuration(1500)

                            //sets listener for ball's position so it doesn't go out of bounds
                            moveBall.setUpdateListener{
                                val ballPosition = ball.x
                                if (ballPosition <= 0) {
                                    moveBall.cancel()
                                }
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.i("touchUp", "touch up is working")
                        ball.animate().cancel()
                    }
//                    MotionEvent.ACTION_MOVE -> {
//                    Log.i("touchMove", "touch move is working")
//                    }
                }
                return true
            }
        })



        animatePlatforms(screen)

        return view
    }

    private fun animatePlatforms(view: View){
        // Initialize platform ImageView
        val platform = view.findViewById<ImageView>(R.id.platform)

        //make an array of directions to use for positioning the platform
        //val position = arrayOf<String>("")

        //set platform to random platform in list
        val random = Random.nextInt(0, platformList.size)
        platform.setImageResource(platformList[random])

        //set the height of platform so they are all the same height
        platform.getLayoutParams().height = 50

        //get starting, desired end position, duration, and right of left positioning
        val start = (platform.height.toFloat() + (view.height.toFloat() ?: 0f))
        val end = -2100f
        val speed: Long = 2000

        //move the platform
        val animation = platform.animate().translationYBy(end).setDuration(speed)
        //animation.interpolator = AccelerateInterpolator()
        //animation.start()

        //resets view to original position and calls animate again after delay
        animation.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                platform.y = start
                handler.postDelayed({ animatePlatforms(view) }, 0)
            }
        })
    }
}