
## usate
```
//ModuleName/build.gradle
dependencies {
    implementation 'io.github.tomgarden:lib_system_bar:+'
}

//ModuleName/build.gradle   OR    ProjectName/build.gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/TomGarden/")

        credentials {
            //不限的 github 账户名
            username = System.getenv("TOMGARADEN_USERNAME")
            //与 github 账户名成对的 具有 read:packages 权限的 token
            password = System.getenv("TOMGARADEN_READ_PACKAGES_TOKEN")
        }
    }
}
```

### 占位符

SystemBar 隐藏或者 SystemBar 覆盖 layout 的时候可能会需要一个和 SystemBar 尺寸相同的 View

```xml
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <!--layout_height 字段的设定无意义 , onMeasure 逻辑已被改写-->
        <io.github.tomgarden.lib_system_bar.view.PlaceHolderStatusBar
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/black" />

        <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="0dp"
            android:layout_weight="1"
            android:gravity="center_vertical"
            android:orientation="vertical"
            tools:context=".MainActivity">
        <FrameLayout/>

        <!--layout_height 字段的设定无意义 , onMeasure 逻辑已被改写-->
        <io.github.tomgarden.lib_system_bar.view.PlaceHolderNavigationBar
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@color/black" />
        
    </LinearLayout>
```

### 其他能力 , 代码里有注释

`SystemBar.xXxxxXxxx()` 


----

## 0x00. [控制系统界面可见度](https://developer.android.com/training/system-ui?hl=zh-cn)

1. 控制状态栏模糊程度
2. 控制状态来可见性
3. 控制导航栏可见性
4. 系统栏目变化回调

1. 其他相关内容
    - StatusBar 颜色变更


### 0.1. 解题思路

最开始尝试较清晰的能力代码封装 , 但是感受上 API 过于杂乱 , 转换思路为根据实际需求做应对各种效果的能力的封装
- 在写能力的时候发现 各个 flag 之间还会有相互影响 , 所以只能按照需求做逐个封装的思路是合理的 , 不应该考虑各个设置间的叠加效果 ;
    最好每次设置一个效果的时候就对之前的效果做全部的清空然后重新应用新效果

### 0.2. 连锁反应 ☞ 软键盘弹出效果

关于这种情况 , 最完美的表现就是 系统默认的 , 键盘弹出时候, 整个布局的高度都变小 , 键盘把内容顶起来


```kotlin
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
        //activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
```

调用上述函数后如果界面上有 EditText , 点击它软键盘弹出的时候 , 光标是覆盖在软键盘下方的
- [**不完美的解决方法** , 监听布局并改变布局尺寸 , 试过 , 监听并不是所有情况都会触发 , 有造成 , 键盘已经隐藏布局却没有复原的 case](https://stackoverflow.com/a/19494006/7707781)
- activity 新增了键盘隐藏控制能力 , 不知道能否避免这种异常再次发生 , 有待实践检验



## 0x01. [刘海屏支持](https://developer.android.com/guide/topics/display-cutout?hl=zh-cn)

1. 如有可能，请使用 `WindowInsetsCompat` 检索状态栏高度
2. java代码设置 `layoutInDisplayCutoutMode`
3. xml 配置 `android:windowLayoutInDisplayCutoutMode`





## 0x02. [键盘"恰当"隐藏的方法](https://blog.csdn.net/u013651026/article/details/78903398)
```java
    //XxxxActivity.java

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
```


## 0x03. []