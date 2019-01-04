package com.dimovsoft.photoframe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

class Watch(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paintFrame = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintHourLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintMinuteLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintSecondLine = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintInside = Paint(Paint.ANTI_ALIAS_FLAG)

    private var size = 150

    init {
        paintFrame.color = Color.YELLOW
        paintInside.color = Color.WHITE

        paintHourLine.color = Color.BLACK
        paintHourLine.strokeWidth = 5f

        paintMinuteLine.color = Color.GRAY
        paintMinuteLine.strokeWidth = 4f

        paintSecondLine.color = Color.GREEN
        paintSecondLine.strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWatch(canvas)

        val seconds = Calendar.getInstance().get(Calendar.SECOND)
        drawNeedle(canvas,seconds/60f,paintSecondLine)

        val minutes = Calendar.getInstance().get(Calendar.MINUTE)
        drawNeedle(canvas, (minutes+seconds/60f)/60f,paintMinuteLine)

        val hours = Calendar.getInstance().get(Calendar.HOUR)
        drawNeedle(canvas,( hours+(minutes+seconds/60f)/60f)/12f,paintHourLine)

    }

    private fun drawNeedle(canvas: Canvas, percent: Float, paint: Paint) {
        val radius = size/2f*0.8f
        val center = radius + size/2f*0.2f
        val angle = 2f*Math.PI * percent - 0.5f*Math.PI
        val x = center + radius * Math.cos(angle).toFloat()
        val y = center + radius * Math.sin(angle).toFloat()
        canvas.drawLine(center,center,x,y,paint)

    }

    private fun drawWatch(canvas: Canvas) {
        canvas.drawCircle(size/2.0f, size/2.0f, size/2.0f, paintFrame) // Frame
        canvas.drawCircle(size/2.0f, size/2.0f, size/2.0f*0.8f, paintInside) // Inside
        canvas.drawCircle(size/2.0f, size/2.0f, 20f, paintFrame) // Center
        val radius = size/2f*0.8f
        val center = radius + size/2f*0.2f

        for (i in 0..11) {
            val angle = (i/12f)*(2f*Math.PI)
            val x1 = center + radius * Math.cos(angle).toFloat()
            val y1 = center + radius * Math.sin(angle).toFloat()
            val x2 = center + (radius*0.8f) * Math.cos(angle).toFloat()
            val y2 = center + (radius*0.8f) * Math.sin(angle).toFloat()
            canvas.drawLine(x1,y1,x2,y2,paintHourLine)
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

}