package com.lhd.views.cornercard

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlin.math.abs

class CornerCard @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val rectView = RectF()
    private val pathView = Path()

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
            val ta = context.obtainStyledAttributes(it, R.styleable.CornerCard)

            shadowRadius =
                abs(ta.getDimensionPixelSize(R.styleable.CornerCard_cc_shadow_radius, 0).toFloat())
            shadowDx = ta.getDimensionPixelSize(R.styleable.CornerCard_cc_shadow_dx, 0).toFloat()
            shadowDy = ta.getDimensionPixelSize(R.styleable.CornerCard_cc_shadow_dy, 0).toFloat()
            paintCard.color = ta.getColor(R.styleable.CornerCard_cc_background_color, Color.BLACK)

            shadowColor = ta.getColor(R.styleable.CornerCard_cc_shadow_color, Color.BLACK)
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
        val viewHeight = bottomOfView - topOfView
        val viewWidth = rightOfView - leftOfView
        if (topOfView < rectView.top)
            topOfView = rectView.top
        if (leftOfView < rectView.left)
            leftOfView = rectView.left
        val leftOval = RectF(leftOfView, topOfView, leftOfView + viewHeight, topOfView + viewHeight)
        val rightOval = RectF(rightOfView - viewHeight, leftOval.top, rightOfView, leftOval.bottom)
        val centerRect = RectF(leftOval.centerX(), leftOval.top, rightOval.centerX(), leftOval.bottom)
        pathView.addOval(leftOval, Path.Direction.CW)
        pathView.addRect(centerRect, Path.Direction.CW)
        pathView.addOval(rightOval, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            it.drawPath(pathView, paintCard)
            canvas.clipPath(pathView)
        }
    }
}