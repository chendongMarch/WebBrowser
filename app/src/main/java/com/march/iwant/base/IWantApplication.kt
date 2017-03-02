package com.march.iwant.base

import com.antfortune.freeline.FreelineCore
import com.march.dev.app.BaseApplication
import com.march.dev.utils.PathUtils
import com.march.iwant.db.DbHelper

/**
 *  CreateAt : 2017/1/18
 *  Describe :
 *  @author chendong
 */
class IWantApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        FreelineCore.init(this)
        PathUtils.initPath(this, "a.com.march.iwant")
        DbHelper.init(this)
    }
}