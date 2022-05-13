package com.mobizion.base.dialog

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.mobizion.base.databinding.DialogProgressBinding
import com.mobizion.base.extension.visible
import com.mobizion.base.utils.getDrawable


class LoadingDialog(activity: Activity) {

    private val loadingDialogBuilder:AlertDialog.Builder = AlertDialog.Builder(activity)
    private val binding:DialogProgressBinding = DialogProgressBinding.inflate(LayoutInflater.from(activity),null,false)
    private var loadingDialog: AlertDialog

    init {
        loadingDialogBuilder.setView(binding.root)
        loadingDialogBuilder.setCancelable(false)
        loadingDialogBuilder.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return@setOnKeyListener false
            }
            return@setOnKeyListener true
        }
        binding.llLoading.background = getDrawable(Color.WHITE,30f)
        loadingDialog = loadingDialogBuilder.create()
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun setProgressColor(color:Int){
        binding.progressBar.indeterminateTintList = ColorStateList.valueOf(color)
    }

    fun setProgressText(text:String){
        binding.txtProgress.text = text
    }

    fun setProgressTextColor(color:Int){
        binding.txtProgress.setTextColor(color)
    }

    fun setDialogBackgroundColor(color: Int,radius:Float){
        binding.root.background = getDrawable(color,radius)
    }

    fun show(msg:String){
        binding.txtProgress.visible(msg.isNotEmpty())
        setProgressText(msg)
        loadingDialog.show()
    }

    fun dismiss(){
        loadingDialog.dismiss()
    }


}