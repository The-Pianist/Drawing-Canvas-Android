package com.example.drawingapp2

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.view.*

@SuppressLint("NewApi")
class drawingView (context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null

    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null

    private var mBrushSize: Float =
        0.toFloat()

    private var thecolor = Color.BLACK

    private var mpaths= ArrayList <CustomPath>()

    private var canvas: Canvas? = null

    init {
        setUpDrawing()
    }


    private fun setUpDrawing() {
        mDrawPaint = Paint()
        mDrawPath = CustomPath(thecolor, mBrushSize)

        mDrawPaint!!.color = thecolor

        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND

        mCanvasPaint = Paint(Paint.DITHER_FLAG)

        mBrushSize = 20.toFloat()
    }

    override fun onSizeChanged(w: Int, h: Int, wprev: Int, hprev: Int) {
        super.onSizeChanged(w, h, wprev, hprev)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        for(path in mpaths){
            mDrawPaint!!.strokeWidth = path.brushThickness
            mDrawPaint!!.color = path.color
            canvas.drawPath(path, mDrawPaint!!)
        }
        if (!mDrawPath!!.isEmpty) {
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    fun setSizeForBursh(newsize:Float){
        mBrushSize=TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newsize, resources.displayMetrics)
        mDrawPaint!!.strokeWidth=mBrushSize
    }

    fun setcolor(color: Int){ thecolor=color }

    fun clear(){
        if(mpaths.isNotEmpty()){
            mpaths.removeLast()
            invalidate()
        }else{
            mpaths=mpaths
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = thecolor
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                mDrawPath!!.moveTo(
                    touchX,
                    touchY
                )
            }

            MotionEvent.ACTION_MOVE -> {
                mDrawPath!!.lineTo(
                    touchX,
                    touchY
                )
            }

            MotionEvent.ACTION_UP -> {
                mpaths.add(mDrawPath!!)
                mDrawPath = CustomPath(thecolor, mBrushSize)

            }
            else -> return false
        }

        invalidate()
        return true
    }


    internal inner class CustomPath(var color: Int, var brushThickness: Float) : Path()}


