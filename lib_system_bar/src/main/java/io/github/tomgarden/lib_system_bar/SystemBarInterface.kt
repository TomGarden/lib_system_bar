package io.github.tomgarden.lib_system_bar

import android.app.Activity

/**
 * 系统 UI 的控制比较细碎 , 我们尝试通过业务场景加以区分
 * 1. 粘性沉浸式全屏模式 : 进入全屏模式后 , 滑动边缘可以展示系统 UI , 过一小段时间后系统 UI 自动消失 , 应用对此无感知
 *      - SystemBar.fullScreenStickyImmersive(this@MainActivity)
 *      - SystemBar.supportBangsVerHor(this@MainActivity.window)
 * 2. 应用内容位于系统 UI 下方 , 系统 UI 颜色手动调控
 *      -
 *      -
 * 3. TODO: 其他,待续
 *
 * @constructor Create empty System bar interface
 */
interface SystemBarInterface {

    /** [调暗系统栏](https://developer.android.com/training/system-ui/dim#kotlin) */
    fun systemBarDim(activity: Activity?)

    /*[隐藏状态连](https://developer.android.com/training/system-ui)*/
    fun hideStatusBar(activity: Activity?)

    /** 展示 statusBar 但是会覆盖在 应用布局之上 */
    fun statusBarCoverLayout(activity: Activity?)

    /* 展示 statusBar 和 navigationBar 但是会覆盖在应用布局之上*/
    fun systemBarCoverLayout(activity: Activity?)

    /**[使用沉浸式全屏模式](https://developer.android.com/training/system-ui/immersive)
     * 当用户需要调出系统栏时，他们可从隐藏系统栏的任一边滑动, 来调出系统界面
     * */
    fun fullScreenImmersive(activity: Activity?)

    /**[使用粘性沉浸式全屏模式](https://developer.android.com/training/system-ui/immersive)
     *
     * 在粘性沉浸模式下，如果用户从隐藏了系统栏的边缘滑动，系统栏会显示出来，
     * 但它们是半透明的，并且轻触手势会传递给应用，因此应用也会响应该手势。
     *
     * 无互动几秒钟后，或者用户在系统栏之外的任何位置轻触或做手势时，系统栏会自动消失。
     * */
    fun fullScreenStickyImmersive(activity: Activity?)

    /**
     * [支持挖孔/刘海屏](https://developer.android.com/guide/topics/display-cutout)
     *
     * @param activity
     */
    fun displayCutouts(activity: Activity?)

    /** 校验 status 状态是否正在被应用 */
    fun bitJudgment(curVal: Int, checkStatus: Int): Boolean {
        return curVal and checkStatus == checkStatus
    }

    fun clear(activity: Activity?) {
        activity?.window?.decorView?.systemUiVisibility = 0
    }
}