package com.example.edufun.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.edufun.R

class MyButton: AppCompatButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var txtColor: Int = 0
    private var enabledBg: Drawable
    private var disabledBg: Drawable

    init {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBg = ContextCompat.getDrawable(context, R.drawable.button) as Drawable
        disabledBg = ContextCompat.getDrawable(context, R.drawable.button_disable) as Drawable
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if(isEnabled) enabledBg else disabledBg
        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
        text = if(isEnabled) "Submit" else "Submit"
    }
}