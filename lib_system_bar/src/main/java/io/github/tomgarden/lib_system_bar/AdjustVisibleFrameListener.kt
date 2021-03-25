package io.github.tomgarden.lib_system_bar

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import io.github.tomgarden.lib.log.Logger
import java.lang.ref.SoftReference

/**  用于键盘弹出时候控制布局尺寸
 * 不建议使用 , 在有输入款的页面不建议使用导致键盘弹起覆盖布局 systemBar 设置 ;
 * 因为我没有找到稳定的合理的无异常实现方案 .
 *
 * 我没有找到方案不代表没有方案 , 查看了微信和飞书 , 他们也没有做这种效果 , 这也不能说明没有好的解决方案
 *
 * 但是我个人更倾向于这个问题存疑吧 , 用较为稳妥的解决方案吧
 * */
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