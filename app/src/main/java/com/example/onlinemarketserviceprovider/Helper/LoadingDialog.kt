package com.example.onlinemarketserviceprovider.Helper

import android.app.Activity
import android.app.AlertDialog
import com.example.onlinemarketserviceprovider.R

class LoadingDialog {
    private lateinit var isDialog:AlertDialog
    fun startLoad(mActivity:Activity){
        /**Set View**/
        val inflater=mActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_progress_dialog,null)
        /**Set Dialog**/
        var builder =AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialog=builder.create()
        isDialog=builder.show()
    }

    fun isDismiss(){
        isDialog.dismiss()
    }
}