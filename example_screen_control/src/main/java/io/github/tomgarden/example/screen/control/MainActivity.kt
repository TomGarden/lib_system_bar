package io.github.tomgarden.example.screen.control

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.databinding.DataBindingUtil
import io.github.tomgarden.example.screen.control.databinding.ActivityMainBinding
import io.github.tomgarden.lib_system_bar.AutoDismissKeyboard
import io.github.tomgarden.lib_system_bar.SystemBar

class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        //SystemBar.systemBarCoverLayout(this@MainActivity)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {

            btnSystemBarDim.setOnClickListener {
                SystemBar.systemBarDim(this@MainActivity)
            }
            btnHideStatusBar.setOnClickListener {
                //SystemBar.hideStatusBar(this@MainActivity)
                SystemBar.hideStatusBar(this@MainActivity)
            }
            btnFullScreenImmersive.setOnClickListener {
                SystemBar.fullScreenImmersive(this@MainActivity)
            }
            btnFullScreenStickyImmersive.setOnClickListener {
                SystemBar.fullScreenStickyImmersive(this@MainActivity)
                SystemBar.supportBangsVerHor(this@MainActivity.window)
            }
            btnDisplayCustom.setOnClickListener {
                //SystemBar30.displayCutouts(this@MainActivity)
//                SystemBar.fullScreenStickyImmersive(this@MainActivity)
//                SystemBar.supportBangsVerHor(this@MainActivity.window)
            }
            btnDisplayCustom29.setOnClickListener {
                SystemBar.fullScreenStickyImmersive(this@MainActivity)
                SystemBar.supportBangsVerHor(this@MainActivity.window)
            }

            btnSysBarCover.setOnClickListener {
                SystemBar.systemBarCoverLayout(this@MainActivity)
                SystemBar.setStatusBarColor(this@MainActivity, Color.TRANSPARENT)
                SystemBar.setNavigationBarColor(this@MainActivity, Color.TRANSPARENT)
                //SystemBar.setRootView(this@MainActivity)
                SystemBar.adjustVisibleFrame(this@MainActivity)
            }

            btnStatusBarCover.setOnClickListener {
                SystemBar.statusBarCoverLayout(this@MainActivity)
                SystemBar.setStatusBarColor(this@MainActivity, Color.GREEN)
                SystemBar.setNavigationBarColor(this@MainActivity, Color.GREEN)
                SystemBar.adjustVisibleFrame(this@MainActivity)
            }

            btnStatusBarLightModel.setOnClickListener {
                SystemBar.statusBarLightModel(this@MainActivity)
            }

            btnStatusBarDarkModel.setOnClickListener {
                SystemBar.statusBarDarkModel(this@MainActivity)
            }

            btnClearWindowFlag.setOnClickListener { SystemBar.clearWindowFlag(this@MainActivity) }
            btnClearViewFlag.setOnClickListener { SystemBar.clearViewFlag(this@MainActivity) }

        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        AutoDismissKeyboard.dispatchTouchEvent(this, ev)
        return super.dispatchTouchEvent(ev)
    }
}