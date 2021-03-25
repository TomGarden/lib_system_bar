package io.github.tomgarden.lib_system_bar.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import io.github.tomgarden.lib_system_bar.SystemBar

/** 状态栏(Status Bar)占位符 , xml 高度定义无效 , 写死了*/
class PlaceHolderStatusBar : FrameLayout {

    constructor(context: Context) : super(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            this(context, attrs, defStyleAttr, 0)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        /* 用自己定义的 */
        val heightMS =
            SystemBar.getStatusBarHeightPix(context)?.or(MeasureSpec.EXACTLY)
                ?: heightMeasureSpec

        super.onMeasure(widthMeasureSpec, heightMS)
    }
}