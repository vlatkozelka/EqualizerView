package com.example.equalizer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.util.*

/**
 * Created by Ali on 4/28/2020.
 */
class EqualizerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val gapPercent = 0.4f
        val speeds = arrayOf(4f,8f,12f)
    }

    var isStarted = false

    var colWidth: Int = 0
    var gap: Int = 0
    var totalGap: Int = 0

    var col1Rect = Rect()
    var col2Rect = Rect()
    var col3Rect = Rect()

    var col1CircleRect = RectF()
    var col2CircleRect = RectF()
    var col3CircleRect = RectF()

    var y1 : Int = 0
    var y2 : Int = 0
    var y3 : Int = 0

    var speed1: Float = speeds[1]
    var speed2: Float = speeds[0]
    var speed3: Float = speeds[2]

    val random = Random()
    val animationHandler = Handler()

    val colPaint = Paint()

    var barColor: Int = ContextCompat.getColor(context, R.color.white)
        private set
    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.EqualizerView,
            0, 0).apply {

            try {
                barColor = getColor(R.styleable.EqualizerView_barColor, ContextCompat.getColor(context, R.color.white))
            } finally {
                recycle()
            }
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }



    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        computeRects()

        colPaint.color = barColor

        canvas?.drawRect(col1Rect, colPaint)
        canvas?.drawRect(col2Rect, colPaint)
        canvas?.drawRect(col3Rect, colPaint)

        canvas?.drawOval(col1CircleRect, colPaint)
        canvas?.drawOval(col2CircleRect, colPaint)
        canvas?.drawOval(col3CircleRect, colPaint)

    }

    private fun computeRects(){

        totalGap = ((gapPercent * width).toInt())
        gap = ((gapPercent * width) / 2f).toInt()
        colWidth = ((width - totalGap) / 3f).toInt()

        colPaint.color = ContextCompat.getColor(context, R.color.white)
        //col1
        col1Rect.apply {
            left = 0
            right = left + colWidth
            bottom = height
            top = height - y1
        }

        col1CircleRect.apply {
            left = col1Rect.left.toFloat()
            right = col1Rect.right.toFloat()
            bottom = col1Rect.top + colWidth / 2f
            top = col1Rect.top - colWidth / 2f
        }

        //col2
        col2Rect.apply {
            left = col1Rect.right + gap
            right = left + colWidth
            bottom = height
            top = height - y2
        }

        col2CircleRect.apply {
            left = col2Rect.left.toFloat()
            right = col2Rect.right.toFloat()
            bottom = col2Rect.top + colWidth / 2f
            top = col2Rect.top - colWidth / 2f
        }

        //col3
        col3Rect.apply {
            left = col2Rect.right + gap
            right = left + colWidth
            bottom = height
            top = height - y3
        }

        col3CircleRect.apply {
            left = col3Rect.left.toFloat()
            right = col3Rect.right.toFloat()
            bottom = col3Rect.top + colWidth / 2f
            top = col3Rect.top - colWidth / 2f
        }

    }

    private fun getNextY(currentY: Float, speed: Float): Float {
        return currentY - speed
    }

    val animationRunnable = Runnable {
        generateNewPositions()
        invalidate()
        start()
    }

    fun start(){
        isStarted = true
        animationHandler.postDelayed(animationRunnable, 33) //roughly 30fps
    }

    fun stop(){
        isStarted = false
        animationHandler.removeCallbacks(animationRunnable)
        reset()
        invalidate()
    }

    fun setColorRes(@ColorRes colorRes: Int){
        val color = ContextCompat.getColor(context, colorRes)
        setColor(color)
    }

    fun setColor(color: Int){
        barColor = color
        invalidate()
    }

    private fun reset(){
        y1 = 0
        y2 = 0
        y3 = 0
    }

    private fun generateNewPositions(){
        y1 = getNextY(y1.toFloat(), speed1).toInt()
        y2 = getNextY(y2.toFloat(), speed2).toInt()
        y3 = getNextY(y3.toFloat(), speed3).toInt()
        if(y1 >= height - colWidth){
            y1 = height - colWidth
            speed1 = speeds[random.nextInt(3)]
        }else if(y1 <= 0){
            y1 = 0
            speed1 = -speeds[random.nextInt(3)]
        }

        if(y2 >= height - colWidth){
            y2 = height - colWidth
            speed2 = speeds[random.nextInt(3)]
        }else if(y2 <= 0){
            y2 = 0
            speed2 = -speeds[random.nextInt(3)]
        }

        if(y3 >= height - colWidth){
            y3 = height - colWidth
            speed3 = speeds[random.nextInt(3)]
        }else if(y3 <= 0){
            y3 = 0
            speed3 = -speeds[random.nextInt(3)]
        }

    }

    override fun onDetachedFromWindow() {
        animationHandler.removeCallbacks(animationRunnable)
        super.onDetachedFromWindow()
    }

}