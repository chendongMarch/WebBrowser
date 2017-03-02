package com.march.iwant.base

import com.march.dev.app.activity.BaseActivity
import com.march.iwant.common.LoadingDialog


/**
 *  CreateAt : 2017/1/19
 *  Describe :
 *  @author chendong
 */
abstract class IWantActivity : BaseActivity() {

    val mLoadingDialog: LoadingDialog by lazy {
        LoadingDialog(mContext)
    }

    protected fun showLoading(isShow: Boolean) {
        if (isShow && !mLoadingDialog.isShowing) {
            mLoadingDialog.show()
        }
        if (!isShow && mLoadingDialog.isShowing) {
            mLoadingDialog.dismiss()
        }
    }
}