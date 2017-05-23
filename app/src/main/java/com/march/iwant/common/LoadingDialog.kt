package com.march.iwant.common

import android.content.Context
import android.view.Gravity

import com.march.dev.app.dialog.BaseDialog
import com.march.dev.widget.LeProgressView
import com.march.iwant.R


/**
 * Project  : Reaper
 * Package  : com.march.reaper.iview
 * CreateAt : 2016/11/22
 * Describe :

 * @author chendong
 */
class LoadingDialog(context: Context) : BaseDialog(context) {

    override fun initViews() {

    }

    private val mLpv: LeProgressView by lazy { getView<LeProgressView>(R.id.mLpv) }
    private var isFirstDismiss = true

    override fun getLayoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun setWindowParams() {
        buildDefaultParams(WRAP, WRAP,1f,0f, Gravity.CENTER)
    }

    override fun show() {
        super.show()
        mLpv.startLoading()
    }


    override fun dismiss() {
        if (isFirstDismiss) {
            mLpv.stopLoading { dismiss() }
            isFirstDismiss = false
        } else {
            super.dismiss()
            isFirstDismiss = true
        }
    }
}
