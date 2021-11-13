package io.github.tomgarden.lib_system_bar

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.view.*
import io.github.tomgarden.lib.log.Logger
import io.github.tomgarden.lib_system_bar.AdjustVisibleFrameListener


/** 在设置各种 标志的时候如果发现设置不生效或者由异常 , 可以尝试先将标志位置为初值 , 然后在做进一步的标志位的配置 */
object SystemBar {

    //******************************************************************************************
    // SystemBar 显示和状态控制
    //******************************************************************************************


    fun clearViewFlag(activity: Activity?) {
        activity?.window?.decorView?.systemUiVisibility = 0
    }

    fun clearWindowFlag(activity: Activity?) {
        activity?.window?.setFlags(0, 0)
        activity?.recreate()
    }

    /** [调暗系统栏(状态栏/导航栏)](https://developer.android.com/training/system-ui/dim#kotlin)
     *
     * 本课介绍如何在 Android 4.0（API 级别 14）及更高版本中调暗系统栏（即状态和导航栏）。在更早期的版本中，Android 不提供调暗系统栏的内置方式。
     * 使用此方法时，内容大小不会调整，但系统栏中的图标会消失。用户只要轻触屏幕的状态栏或导航栏区域，这两个栏就会全面显示出来。这种方法的优点在于，这些栏仍然存在，但是它们的详细信息被遮盖，不仅可以在打造沉浸式体验，而且不影响用户轻松访问这些栏。
     *
     *  已知:
     *  1. api 调用生效 : 16  31
     *  2. api 调用无效 : 19  21  29
     *  3. 未出现的版本系为测试
     * */
    fun systemBarDim(activity: Activity?) {
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
    }

    /**
     * [隐藏状态栏](https://developer.android.com/training/system-ui/status?hl=zh-cn)
     *
     * 对于 4.0(14) 及更低版本
     *     <application
     *          ...
     *          android:theme="@android:style/Theme.Holo.NoActionBar.Fullscreen" >
     *          ...
     *     </application>
     *
     * @param activity
     */
    fun hideStatusBar(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            /*此种隐藏方式触摸不会导致状态来显现*/
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        } else {
            /*此种隐藏方式触摸会导致状态来显现*/
            activity?.window?.insetsController?.let { controller: WindowInsetsController ->
                controller.hide(WindowInsets.Type.statusBars())
            }
        }
    }

    /** 『Api level >= 19 生效』展示 statusBar 和 navigationBar 但是会覆盖在应用布局之上
     *
     *  『Api level 30』此动作完成后 ,
     *                  - 对于 statusBar 的着色 调用不生效 , statusBar 保持半透明 ,
     *                  - 对于 navigationBar 的着色动作生效
     *  『Api level 29』此动作完成后 , 对于 statusBar 的着色 调用不生效 , 是透明的
     *
     *  单纯设置 systemBar 覆盖 layout 会造成键盘弹出时候的 view 自动上移能力丧失
     *
     *  对 systemBar 着色不生效的情况可以通过 在 布局中 填充 View 来解决
     *
     *  [暂未实施的参考](https://stackoverflow.com/questions/29069070/completely-transparent-status-bar-and-navigation-bar-on-lollipop)
     * */
    fun statusBarCoverLayout(activity: Activity?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            /*『FLAG_TRANSLUCENT_STATUS』: When this flag is enabled for a window,
            it automatically sets the system UI visibility
            flags SYSTEM_UI_FLAG_LAYOUT_STABLE and SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.*/
            //activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        //activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    /** 『Api level >= 19 生效』展示 statusBar 和 navigationBar 但是会覆盖在应用布局之上
     *
     *  『Api level 30』此动作完成后 ,
     *                  - 对于 statusBar 的着色 调用生效 ,
     *                  - 对于 navigationBar 的着色动作不生效 , navigationBar 保持半透明 ,
     *  『Api level 29』此动作完成后 , 对于 systemBar 的着色 调用不生效 , 是透明的
     *
     *  对 systemBar 着色不生效的情况可以通过 在 布局中 填充 View 来解决
     *
     *  目前此函数无法和 adjustVisibleFrame 结合使用
     *
     *  [暂未实施的参考](https://stackoverflow.com/questions/29069070/completely-transparent-status-bar-and-navigation-bar-on-lollipop)
     * */
    fun systemBarCoverLayout(activity: Activity?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            /*『FLAG_TRANSLUCENT_STATUS』: When this flag is enabled for a window,
            it automatically sets the system UI visibility
            flags SYSTEM_UI_FLAG_LAYOUT_STABLE and SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.*/
            //activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    /**[使用沉浸式全屏模式](https://developer.android.com/training/system-ui/immersive)
     * 当用户需要调出系统栏时，他们可从隐藏系统栏的任一边滑动, 来调出系统界面
     * */
    fun fullScreenImmersive(activity: Activity?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            activity?.window?.let { window ->
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
        } else {
            activity?.window?.insetsController?.let { controller: WindowInsetsController ->
                controller.hide(
                    WindowInsets.Type.statusBars() or
                            WindowInsets.Type.navigationBars()
                )
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_BARS_BY_TOUCH

            }
        }
    }

    /**[使用粘性沉浸式全屏模式](https://developer.android.com/training/system-ui/immersive)
     *
     * 在粘性沉浸模式下，如果用户从隐藏了系统栏的边缘滑动，系统栏会显示出来，
     * 但它们是半透明的，并且轻触手势会传递给应用，因此应用也会响应该手势。
     *
     * 无互动几秒钟后，或者用户在系统栏之外的任何位置轻触或做手势时，系统栏会自动消失。
     * */
    fun fullScreenStickyImmersive(activity: Activity?) {

        var flag =
            /* 没有刘海屏的设备上没有这一行也是同样的效果 , 刘海屏设备兼容需要它存在 */
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                /*{@link WindowInsetsController#hide(int)} with {@link (WindowInsets.Type)#statusBars()}*/
                .or(View.SYSTEM_UI_FLAG_FULLSCREEN)
                /*{@link WindowInsetsController#hide(int)} with {@link Type#navigationBars()}*/
                .or(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        /*『apiLevel >= 19 生效』粘性效果*/
        /*{@link WindowInsetsController#BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE}*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            flag = flag.or(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

        activity?.window?.decorView?.systemUiVisibility = flag
    }


    /** 『Api level >= 23 生效』 设置 statusBar 为亮色主题的时候 statusBar 中文字颜色为暗色*/
    fun statusBarLightModel(activity: Activity?): Unit {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    /** 『Api level >= 23 生效』 设置 statusBar 为暗色主题的时候 statusBar 中文字颜色为亮色 */
    fun statusBarDarkModel(activity: Activity?): Unit {
        /*只是做了一个简单的和 statusBarLightModel 相反的位运算*/
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.window?.decorView?.systemUiVisibility?.let { systemUiVisibility ->
                activity.window?.decorView?.systemUiVisibility =
                    systemUiVisibility.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
            }
        }
    }

    /** 设置根布局参数 */
    fun setRootView(activity: Activity?) {
        val parent = activity?.findViewById<View>(R.id.content) as ViewGroup?

        var i = 0
        val count = parent?.childCount ?: 0
        while (i < count) {
            val childView = parent?.getChildAt(i)
            if (childView is ViewGroup) {
                childView.setFitsSystemWindows(true)
                childView.clipToPadding = true
            }
            i++
        }
    }

    /** 参考调试代码 : https://stackoverflow.com/a/19494006/7707781 , 如有必要 , 根据逻辑做出相关调整*/
    fun adjustVisibleFrame(activity: Activity?) {
        activity?.window?.decorView?.viewTreeObserver
            ?.addOnGlobalLayoutListener(AdjustVisibleFrameListener(activity))
    }

    //******************************************************************************************
    // 获取 SystemBar 高度
    //******************************************************************************************

    /**[推荐]获取 StatusBar 像素 高度*/
    fun getStatusBarHeightPix(activity: Activity): Int {
        val rectgle = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(rectgle)
        val statusBarHeight: Int = rectgle.top
        //val contentViewTop: Int = window.findViewById<View>(Window.ID_ANDROID_CONTENT).getTop()
        //val TitleBarHeight = contentViewTop - StatusBarHeight
        return statusBarHeight
    }

    /**获取 StatusBar 像素 高度*/
    fun getStatusBarHeightPix(context: Context): Int? {
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            null
        }
    }

    /** 获取 NavigationBar 高度*/
    fun getNavigationBarHeightPix(context: Context): Int? {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            null
        }
    }

    /* 『模拟器可用 , 兼容性差』 当前系统是否展示 NavigationBar */
    fun configIsShowNavigationBar(context: Context): Boolean? {
        val id = context.resources
            .getIdentifier("config_showNavigationBar", "bool", "android")
        return if (id > 0) {
            return context.resources.getBoolean(id);
        } else {
            null
        }
    }


    //******************************************************************************************
    // 修改 SystemBar 颜色
    //******************************************************************************************

    /** 『apiLevel >= 21 生效』设置 statusBar 颜色 */
    fun setStatusBarColor(activity: Activity?, color: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.statusBarColor = color
        }
    }

    /** 『apiLevel >= 21 生效』设置 navigationBar 颜色 */
    fun setNavigationBarColor(activity: Activity?, color: Int): Unit {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.navigationBarColor = color
        }
    }


    //******************************************************************************************
    // [支持刘海儿](https://developer.android.com/guide/topics/display-cutout?hl=zh-cn)
    //******************************************************************************************

    /**『apiLevel >= 28 生效』 这是默认行为，如上所述。在竖屏模式下，内容会呈现到刘海区域中；但在横屏模式下，内容会显示黑边。*/
    fun supportBangsVertical(window: Window?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            supportBangs(window, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT)
        }
    }

    /**『apiLevel >= 28 生效』 在竖屏模式和横屏模式下，内容都会呈现到刘海区域中。*/
    fun supportBangsVerHor(window: Window?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            supportBangs(
                window,
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            )
        }
    }

    /**『apiLevel >= 28 生效』 内容从不呈现到刘海区域中。*/
    fun unSupportBangs(window: Window?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            supportBangs(window, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER)
        }
    }

    /** 『apiLevel >= 30 生效』
    始终允许窗口延伸到屏幕所有边缘的显示剪切区域。

    窗口必须确保没有重要内容与DisplayCutout重叠。

    在此模式下，无论窗口是否隐藏系统栏，窗口都会在纵向和横向显示的所有边缘上的切口下延伸。
     */
    fun supportBangsExtendViewToAllCustom(window: Window?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            supportBangs(window, WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS)
        }
    }

    /**『apiLevel >= 28 生效』设置对刘海屏的支持 */
    fun supportBangs(window: Window?, mode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window?.apply {
                attributes =
                    attributes.apply {
                        layoutInDisplayCutoutMode = mode
                    }
            }
        }
    }


    //******************************************************************************************
    // 工具函数
    //******************************************************************************************

    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }

}