package com.mobizion.xutils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

object AnimationUtil {

     fun createAlphaInAnimation(duration:Long = 300): Animation {
        val an  = AlphaAnimation(0f, 1f)
        an.duration = duration
        return an
    }

    fun createAlphaOutAnimation(duration:Long = 300): Animation {
        val an = AlphaAnimation(1f, 0f)
        an.duration = duration
        an.fillAfter = true
        return an
    }

    fun createTranslationOutAnimation(duration:Long = 200, from:Float = 1f, to:Float = 0f): Animation {
        val type = TranslateAnimation.RELATIVE_TO_PARENT
        val an = TranslateAnimation(
            type, 0f, type, 0f, type,
            0f, type, -1f
        )
        an.duration = duration
        an.fillAfter = false

        return an
    }

    fun createTranslationOutBottomAnimation(duration:Long = 200, from:Float = 1f, to:Float = 0f,  completion: ((type: Int, animation: Animation) -> Unit)? = null): Animation {
        val type = TranslateAnimation.RELATIVE_TO_SELF
        val an = TranslateAnimation(
            type, 0f, type, 0f, type,
            0f, type, 2f
        )
        an.duration = duration
        an.fillAfter = false
        an.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation) {
//                completion?.let { it(1, animation) }
            }

            override fun onAnimationEnd(animation: Animation) {
                completion?.let { it(2, animation) }
            }

            override fun onAnimationRepeat(animation: Animation) {
//                completion?.let { it(3, animation) }
            }
        })
        return an
    }

    fun createTranslationInAnimation(duration:Long = 200, from:Float = 1f, to:Float = 0f, completion: ((type: Int, animation: Animation) -> Unit)? = null): Animation {
        val type = TranslateAnimation.RELATIVE_TO_SELF
        val an = TranslateAnimation(
            type, 0f, type, 0f, type,
            from, type, to
        )
        an.duration = duration
        an.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation) {
//                completion?.let { it(1, animation) }
            }

            override fun onAnimationEnd(animation: Animation) {
                completion?.let { it(2, animation) }
            }

            override fun onAnimationRepeat(animation: Animation) {
//                completion?.let { it(3, animation) }
            }
        })
        return an
    }

    fun scaleView(v: View, startScale: Float, endScale: Float, completion: (type: Int) -> Unit) {
        val anim = ScaleAnimation(
            startScale, endScale, // Start and end values for the X axis scaling
            startScale, endScale, // Start and end values for the Y axis scaling
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
            Animation.RELATIVE_TO_SELF, 0.5f
        ) // Pivot point of Y scaling
        anim.fillAfter = true // Needed to keep the result of the animation
        anim.duration = 250
        v.startAnimation(anim)
        anim.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                completion(0)
            }

            override fun onAnimationEnd(animation: Animation?) {
                completion(1)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                completion(2)
            }
        })
    }
}