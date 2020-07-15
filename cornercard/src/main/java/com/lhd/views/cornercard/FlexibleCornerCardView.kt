package com.lhd.views.cornercard

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.FrameLayout
import kotlin.math.abs
import kotlin.math.min

class FlexibleCornerCardView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val rectView = RectF()
    private val rectContainer = RectF()
    private val pathView = Path()

    private var cornerRadius = 0f
    private var initCornerRadius = 0f
    private var shadowRadius = 0f
    private var shadowDx = 0f
    private var shadowDy = 0f
    private var shadowColor = Color.BLACK

    private val paintCard = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        if (parent is ViewGroup?) {
            (parent as ViewGroup?)?.clipChildren = false
        }
        setWillNotDraw(false)
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.FlexibleCornerCardView)

            cornerRadius = ta.getDimension(R.styleable.FlexibleCornerCardView_cc_corner_radius, 0f)
            initCornerRadius = cornerRadius
            shadowRadius =
                abs(ta.getDimension(R.styleable.FlexibleCornerCardView_cc_shadow_radius, 0f))
            shadowDx = ta.getDimension(R.styleable.FlexibleCornerCardView_cc_shadow_dx, 0f)
            shadowDy = ta.getDimension(R.styleable.FlexibleCornerCardView_cc_shadow_dy, 0f)
            paintCard.color = ta.getColor(R.styleable.FlexibleCornerCardView_cc_background_color, Color.BLACK)

            shadowColor = ta.getColor(R.styleable.FlexibleCornerCardView_cc_shadow_color, Color.BLACK)
            if (shadowRadius > 0f) {
                paintCard.setShadowLayer(
                    shadowRadius,
                    shadowDx,
                    shadowDy,
                    shadowColor
                )
            }
            ta.recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectView.set(0f, 0f, w.toFloat(), h.toFloat())
        pathView.reset()
        val rightOfView = rectView.right - shadowRadius - abs(shadowDx)
        var leftOfView = rectView.left + shadowRadius + abs(shadowDx)
        var topOfView = rectView.top + shadowRadius + abs(shadowDy)
        val bottomOfView = rectView.bottom - shadowRadius - abs(shadowDy)
        if (topOfView < rectView.top)
            topOfView = rectView.top
        if (leftOfView < rectView.left)
            leftOfView = rectView.left
        rectContainer.set(leftOfView, topOfView, rightOfView, bottomOfView)
        pathView.addRoundRect(rectContainer, cornerRadius, cornerRadius, Path.Direction.CCW)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawPath(pathView, paintCard)
            canvas.clipPath(pathView)
        }
    }

    fun setCornersRadius(radius: Float) {
        val maxRadius = min(width, height) / 2f
        val r = if (radius > maxRadius) maxRadius else radius
        cornerRadius = r
        pathView.reset()
        pathView.addRoundRect(rectContainer, cornerRadius, cornerRadius, Path.Direction.CCW)
        postInvalidate()
    }

    private var anim: CornerAnimation? = null
    var cornerExtend = false
        private set

    fun changeCornerState(duration: Long = 1000) {
        if (cornerExtend) {
            startCollapseCorner(duration)
        } else {
            startExtendCorner(duration)
        }
    }

    fun startExtendCorner(duration: Long) {
        cornerExtend = true
        anim?.cancel()
        anim = CornerAnimation(this, initCornerRadius, min(width, height) / 2f)
        anim?.startAnim(duration)
    }

    fun startCollapseCorner(duration: Long) {
        cornerExtend = false
        anim?.cancel()
        anim = CornerAnimation(this, min(width, height) / 2f, initCornerRadius)
        anim?.startAnim(duration)
    }

    class CornerAnimation(
        private val flexibleCornerCardView: FlexibleCornerCardView,
        private var oldProgress: Float,
        private var newProgress: Float
    ) : Animation() {
        private var currentProgress: Float = 0F

        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            val progress = oldProgress + (newProgress - oldProgress) * interpolatedTime
            currentProgress = progress
            flexibleCornerCardView.setCornersRadius(progress)
        }

        fun startAnim(duration: Long = 1000){
            setDuration(duration)
            flexibleCornerCardView.startAnimation(this)
        }

        override fun cancel() {
            flexibleCornerCardView.setCornersRadius(currentProgress)
            super.cancel()
        }

    }

}