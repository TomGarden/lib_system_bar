package io.github.tomgarden.lib_system_bar

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import io.github.tomgarden.lib.log.Logger
import java.lang.ref.SoftReference

/**  用于键盘弹出时候控制布局尺寸 */
class AdjustVisibleFrameListener : ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var activityReference: SoftReference<Activity>

    constructor(activity: Activity?) {
        activityReference = SoftReference(activity)
    }

    override fun onGlobalLayout() {
        val activity = activityReference.get() ?: return
        val decorView = activity.window?.decorView ?: return

        val rect = Rect()
        decorView.getWindowVisibleDisplayFrame(rect)

        if (rect.left < 0 ||
            rect.top < 0 ||
            rect.right < 0 ||
            rect.bottom < 0) {
            Logger.e("无效参数 , 取消后续计算")
            return
        }

        val contentParent: View? = activity.findViewById(android.R.id.content)
        if (contentParent !is ViewGroup) {
            return
        }
        val contentView: View = contentParent.getChildAt(0)
        val layoutParams = contentView.layoutParams

        //val parentVisibleRect = Rect()
        //contentParent.getGlobalVisibleRect(parentVisibleRect)

        //val contentVisibleRect = Rect()
        //contentView.getWindowVisibleDisplayFrame(contentVisibleRect)

        val diff = rect.bottom
        if (layoutParams.height != diff) {
            layoutParams.height = diff
            contentView.layoutParams = layoutParams
        }
    }
}