package com.mobizion.gallary.imageCropper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.mobizion.gallary.imageCropper.enum.TouchMode
import com.mobizion.gallary.imageCropper.extensions.getMinimumSideLength
import com.mobizion.gallary.imageCropper.extensions.handleActionMove
import com.mobizion.gallary.imageCropper.extensions.handleActionUp
import com.mobizion.gallary.imageCropper.extensions.onCropMeasure
import com.mobizion.gallary.imageCropper.model.ImageAttributes
import com.mobizion.gallary.imageCropper.scale.ScaleListener

open class CropImageView(context: Context, attributeSet: AttributeSet) :
    androidx.appcompat.widget.AppCompatImageView(context, attributeSet) {
    var cropMatrix: Matrix = Matrix()
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).also { paint ->
        paint.color = Color.WHITE
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE
    }
    var matrixValues: FloatArray = FloatArray(9)
    var touchMode: TouchMode = TouchMode.NONE
    var lastPoint: PointF = PointF()
    var startPoint: PointF = PointF()
    var minScale = 1f
    var maxScale = 3f
    var viewWidth = 0
    var viewHeight = 0
    var saveScale = 1f
    var origWidth = 0f
    var origHeight = 0f
    var oldMeasuredWidth = 0
    var oldMeasuredHeight = 0
    private var mScaleDetector: ScaleGestureDetector
    private var path = Path()
    var centreXCircleArea = 0f
    var centreYCircleArea = 0f
    lateinit var cropImageBitmap: Bitmap
    private var isCircleDraw = false

    init {
        super.setClickable(true)
        mScaleDetector = ScaleGestureDetector(context, ScaleListener)
        scaleType = ScaleType.MATRIX
        setupTouchListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        setOnTouchListener { _, motionEvent ->
            mScaleDetector.onTouchEvent(motionEvent)
            val currentPoint = PointF(motionEvent.x, motionEvent.y)
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastPoint.set(currentPoint)
                    startPoint.set(lastPoint)
                    touchMode = TouchMode.DRAG
                }
                MotionEvent.ACTION_MOVE -> {
                    handleActionMove(currentPoint)
                }
                MotionEvent.ACTION_UP -> {
                    handleActionUp(currentPoint)
                }
                MotionEvent.ACTION_POINTER_UP -> {
                    touchMode = TouchMode.NONE
                }
            }
            imageMatrix = cropMatrix
            invalidate()
            true
        }
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isCircleDraw) {
            path.addCircle(
                (centreXCircleArea),
                (centreYCircleArea),
                getMinimumSideLength(viewWidth, viewWidth),
                Path.Direction.CW
            )
            canvas?.drawCircle(
                centreXCircleArea, centreYCircleArea,
                getMinimumSideLength(viewWidth, viewWidth), paint
            )
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        onCropMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun getImageAttributes(): ImageAttributes {
        return ImageAttributes(
            viewWidth,
            viewHeight,
            cropImageBitmap.config,
            cropImageBitmap,
            centreXCircleArea,
            centreYCircleArea,
            cropMatrix
        )
    }

    fun saveImage(): Bitmap {
        val paint = Paint()
        val path = Path()
        val bitmap = Bitmap.createBitmap(viewWidth, viewWidth, cropImageBitmap.config)
        val canvas = Canvas(bitmap)
        cropMatrix.let { canvas.drawBitmap(cropImageBitmap, it, null) }
        paint.style = Paint.Style.FILL
        paint.color = Color.TRANSPARENT
        paint.isAntiAlias = true
        path.fillType = Path.FillType.INVERSE_EVEN_ODD
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        path.addRect(
            0f,
            0f,
            viewWidth.toFloat(),
            viewHeight.toFloat(),
            Path.Direction.CCW
        )
//        path.addCircle(
//            centreXCircleArea,
//            centreYCircleArea,
//            getMinimumSideLength(viewWidth, viewWidth),
//            Path.Direction.CCW
//        )
        canvas.drawPath(path, paint)
        return bitmap
    }

}