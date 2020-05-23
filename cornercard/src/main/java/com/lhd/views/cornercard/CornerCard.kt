package com.lhd.views.cornercard

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlin.math.abs

class CornerCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val rectView = RectF()
    private val pathView = Path()

    var cornerTopLeft = 0f
        private set
    var cornerTopRight = 0f
        private set
    var cornerBottomLeft = 0f
        private set
    var cornerBottomRight = 0f
        private set

    private var shadowRadius = 0f
    private var shadowDx = 0f
    private var shadowDy = 0f

    private val paintCard = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    init {
        if (parent is ViewGroup?) {
            (parent as ViewGroup?)?.clipChildren = false
        }
        setWillNotDraw(false)
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.CornerCard)
            cornerTopLeft =
                ta.getDimensionPixelSize(R.styleable.CornerCard_cc_corner_top_left, 0).toFloat()
            cornerTopRight =
                ta.getDimensionPixelSize(R.styleable.CornerCard_cc_corner_top_right, 0).toFloat()
            cornerBottomLeft =
                ta.getDimensionPixelSize(R.styleable.CornerCard_cc_corner_bottom_left, 0)
                    .toFloat()
            cornerBottomRight =
                ta.getDimensionPixelSize(R.styleable.CornerCard_cc_corner_bottom_right, 0)
                    .toFloat()

            shadowRadius =
                abs(ta.getDimensionPixelSize(R.styleable.CornerCard_cc_shadow_radius, 0).toFloat())
            shadowDx = ta.getDimensionPixelSize(R.styleable.CornerCard_cc_shadow_dx, 0).toFloat()
            shadowDy = ta.getDimensionPixelSize(R.styleable.CornerCard_cc_shadow_dy, 0).toFloat()
            paintCard.color = ta.getColor(R.styleable.CornerCard_cc_background_color,Color.BLACK)
            if (shadowRadius > 0f) {
                paintCard.setShadowLayer(
                    shadowRadius,
                    shadowDx,
                    shadowDy,
                    Color.parseColor("#000000")
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
        if (topOfView<rectView.top)
            topOfView = rectView.top
        if (leftOfView<rectView.left)
            leftOfView = rectView.left
        pathView.moveTo(leftOfView, cornerTopLeft)
        pathView.quadTo(leftOfView, topOfView, leftOfView + cornerTopLeft, topOfView)
        pathView.lineTo(rightOfView - cornerTopRight, topOfView)
        pathView.quadTo(rightOfView, topOfView, rightOfView, topOfView + cornerTopRight)
        pathView.lineTo(rightOfView, bottomOfView - cornerBottomRight)
        pathView.quadTo(
            rightOfView, bottomOfView,
            rightOfView - cornerBottomRight, bottomOfView
        )
        pathView.lineTo(leftOfView + cornerBottomLeft, bottomOfView)
        pathView.quadTo(leftOfView, bottomOfView, leftOfView, bottomOfView - cornerBottomLeft)
        pathView.lineTo(leftOfView, cornerTopLeft)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawPath(pathView, paintCard)
            canvas.clipPath(pathView)
        }
    }
}