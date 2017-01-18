package com.march.iwant

import com.antfortune.freeline.FreelineCore
import com.march.dev.app.BaseApplication

/**
 *  CreateAt : 2017/1/18
 *  Describe :
 *  @author chendong
 */
class IWantApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        FreelineCore.init(this)
    }
}