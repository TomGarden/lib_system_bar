package io.github.tomgarden.lib_system_bar

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**键盘自动收起动作*/
object AutoDismissKeyboard {

    /**
     * 点击非编辑区域收起键盘
     * 获取点击事件
     */
    fun dispatchTouchEvent(activity: Activity?, ev: MotionEvent?) {
        activity ?: return
        ev ?: return

        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view: View? = activity.currentFocus
            if (isShouldHideKeyBord(view, ev)) {
                hideSoftInput(activity, view?.windowToken);
            }
        }
    }

    /**
     * 判定当前是否需要隐藏
     */
    private fun isShouldHideKeyBord(view: View?, ev: MotionEvent): Boolean {

        if (view is EditText) {
            val locationAry = intArrayOf(0, 0)
            view.getLocationInWindow(locationAry)
            val left = locationAry[0]
            val top = locationAry[1]
            val right = left + view.width
            val bottom = top + view.height

            /*点击的坐标是否在 view 所在的范围*/
            val clickPositionOnView = ev.x > left && ev.x < right && ev.y > top && ev.y < bottom
            return !clickPositionOnView
        }

        return true
    }

    /**
     * 隐藏软键盘
     */
    private fun hideSoftInput(context: Context?, token: IBinder?) {
        token ?: return
        context ?: return

        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        manager?.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}