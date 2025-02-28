/*
 * Copyright 2018 Allan Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.allanwang.kau.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.StringRes
import kotlin.math.hypot
import kotlin.math.max

/**
 * Created by Allan Wang on 2017-06-01.
 *
 * Animation extension functions for Views
 */

@SuppressLint("NewApi")
@KauUtils
fun View.circularReveal(
    x: Int = 0,
    y: Int = 0,
    offset: Long = 0L,
    radius: Float = -1.0f,
    duration: Long = 500L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        visible()
        onFinish?.invoke()
        return
    }
    if (!buildIsLollipopAndUp) return fadeIn(offset, duration, onStart, onFinish)

    val r = if (radius >= 0) radius
    else max(hypot(x.toDouble(), y.toDouble()), hypot((width - x.toDouble()), (height - y.toDouble()))).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, 0f, r).setDuration(duration)
    anim.startDelay = offset
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            visible()
            onStart?.invoke()
        }

        override fun onAnimationEnd(animation: Animator?) = onFinish?.invoke() ?: Unit
        override fun onAnimationCancel(animation: Animator?) = onFinish?.invoke() ?: Unit
    })
    anim.start()
}

@SuppressLint("NewApi")
@KauUtils
fun View.circularHide(
    x: Int = 0,
    y: Int = 0,
    offset: Long = 0L,
    radius: Float = -1.0f,
    duration: Long = 500L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        invisible()
        onFinish?.invoke()
        return
    }
    if (!buildIsLollipopAndUp) return fadeOut(offset, duration, onStart, onFinish)

    val r = if (radius >= 0) radius
    else max(hypot(x.toDouble(), y.toDouble()), hypot((width - x.toDouble()), (height - y.toDouble()))).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, r, 0f).setDuration(duration)
    anim.startDelay = offset
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) = onStart?.invoke() ?: Unit

        override fun onAnimationEnd(animation: Animator?) {
            invisible()
            onFinish?.invoke() ?: Unit
        }

        override fun onAnimationCancel(animation: Animator?) = onFinish?.invoke() ?: Unit
    })
    anim.start()
}

@KauUtils
fun View.fadeIn(
    offset: Long = 0L,
    duration: Long = 200L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        visible()
        onFinish?.invoke()
        return
    }
    val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    anim.startOffset = offset
    anim.duration = duration
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) = onFinish?.invoke() ?: Unit
        override fun onAnimationStart(animation: Animation?) {
            visible()
            onStart?.invoke()
        }
    })
    startAnimation(anim)
}

@KauUtils
fun View.fadeOut(
    offset: Long = 0L,
    duration: Long = 200L,
    onStart: (() -> Unit)? = null,
    onFinish: (() -> Unit)? = null
) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        invisible()
        onFinish?.invoke()
        return
    }
    val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    anim.startOffset = offset
    anim.duration = duration
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            invisible()
            onFinish?.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
            onStart?.invoke()
        }
    })
    startAnimation(anim)
}

@KauUtils
fun TextView.setTextWithFade(text: String, duration: Long = 200, onFinish: (() -> Unit)? = null) {
    fadeOut(
        duration = duration,
        onFinish = {
            setText(text)
            fadeIn(duration = duration, onFinish = onFinish)
        }
    )
}

@KauUtils
fun TextView.setTextWithFade(@StringRes textId: Int, duration: Long = 200, onFinish: (() -> Unit)? = null) =
    setTextWithFade(context.getString(textId), duration, onFinish)

@KauUtils
fun ViewPropertyAnimator.scaleXY(value: Float) = scaleX(value).scaleY(value)
