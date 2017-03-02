package com.march.iwant.base

import com.march.dev.app.fragment.BaseFragment
import com.march.iwant.common.LoadingDialog


/**
 *  CreateAt : 2017/1/19
 *  Describe :
 *  @author chendong
 */
abstract class IWantFragment : BaseFragment() {

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