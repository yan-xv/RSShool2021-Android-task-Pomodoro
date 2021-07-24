package com.yanxv.pomodoro.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.AttrRes
import com.yanxv.pomodoro.R

class ProgressBarCircle @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {

    private var periodMs = 0L
    private var currentMs = 0L
    private var colorFront = Color.RED
    private var colorBack = Color.GRAY
    private var colorCenter = Color.WHITE
    private var bFinished = false

    private val paint = Paint()

    init {
        if (attrs != null) {
            val styledAttrs = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.ProgressBarCircle,
                defStyleAttr,
                0
            )
            colorFront = styledAttrs.getColor(R.styleable.ProgressBarCircle_custom_color_front, colorFront)
            colorBack = styledAttrs.getColor(R.styleable.ProgressBarCircle_custom_color_back, colorBack)
            colorCenter = styledAttrs.getColor(R.styleable.ProgressBarCircle_custom_color_center, colorCenter)
            styledAttrs.recycle()
        }

        paint.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (periodMs == 0L || currentMs == 0L) return
        val startAngel = if (currentMs < periodMs || bFinished) (((currentMs % periodMs).toFloat() / periodMs) * 360) else 360f
        paint.color = colorBack
        canvas.drawArc(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            0f,
            360f,
            true,
            paint
        )
        paint.color = colorFront
        canvas.drawArc(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            -90f,
            - startAngel,
            true,
            paint
        )
        paint.color = colorCenter
        canvas.drawArc(
            width.toFloat()*.05f,
            height.toFloat()*.05f,
            width.toFloat()*.95f,
            height.toFloat()*.95f,
            0f,
            360f,
            true,
            paint
        )
    }

    /**
     * Set lasted milliseconds
     */
    fun setCurrent(current: Long) {
        currentMs = current
        invalidate()
    }

    /**
     * Set colors
     */
    fun setColorFront(color: Int) {
        colorFront = color
        invalidate()
    }

    fun setColorBack(color: Int) {
        colorBack = color
        invalidate()
    }

    fun setColorCenter(color: Int) {
        colorCenter = color
        invalidate()
    }

    /**
     * Set time period
     */
    fun setPeriod(period: Long) {
        periodMs = period
    }
    /**
     * Set state
     */
    fun setFinished(state: Boolean) {
        bFinished = state
    }
}
