package com.mobizion.xbase.activity

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding
import com.mobizion.xbase.view.model.PermissionViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 *@Author: Mirza Tahir Baig
 *@Email: tahir@mobizion.com
 *@Date: 29/08/2022
 */
abstract class XBaseActivity<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity()  {

    val permissionViewModel:PermissionViewModel by viewModel()

    val requestMultiplePermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { status ->
        permissionViewModel.setMultiplePermissionStatus(status)
    }

    val launchActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        permissionViewModel.setActivityResult(result)
    }

    val requestSinglePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        permissionViewModel.setSinglePermissionStatus(granted)
    }

    val binding: B by lazy { bindingFactory(layoutInflater) }

    /**
     * if @return true then keyboard will be hide if touch outside of edit text
     */
    abstract fun shouldHideKeyboard(): Boolean

    /**
     * call in onCreate Method used to initUI
     */

    abstract fun initViews()

    /**
     * used to hide status bar
     */

    open fun shouldHideStatusBar():Boolean{
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if(shouldHideStatusBar()){
                window.decorView.windowInsetsController!!.hide(
                    WindowInsets.Type.statusBars()
                )
            }
        }else{
            if(shouldHideStatusBar()){
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
        }
        setDecorView()
        setContentView(binding.root)
        initViews()
    }

    open fun enableFullScreen():Boolean = false

    open fun enableLightStatusBar():Boolean = false

    private fun setDecorView(){
        if (enableFullScreen()){
            window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            setLightStatusBar(window.decorView, enableLightStatusBar())
        }
    }

    private fun setLightStatusBar(view: View, isLight: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = view.systemUiVisibility
            flags = if (isLight) {
                flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            view.systemUiVisibility = flags
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (shouldHideKeyboard()) {
            if (ev?.action == MotionEvent.ACTION_DOWN) {
                val v: View? = currentFocus
                if (v is EditText) {
                    val outRect = Rect()
                    v.getGlobalVisibleRect(outRect)
                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                        v.clearFocus()
                        val imm =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * Extension to observe live data
     */

    fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) {
        observe(this@XBaseActivity, onChanged)
    }

}