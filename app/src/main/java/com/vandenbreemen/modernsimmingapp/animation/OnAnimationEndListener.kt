package com.vandenbreemen.modernsimmingapp.animation

import android.animation.Animator

/**
 * Listener for attaching logic to be performed when the animation has completed
 */
abstract class OnAnimationEndListener: Animator.AnimatorListener {

    override fun onAnimationStart(animation: Animator?) {

    }

    override fun onAnimationCancel(animation: Animator?) {

    }

    override fun onAnimationRepeat(animation: Animator?) {

    }
}