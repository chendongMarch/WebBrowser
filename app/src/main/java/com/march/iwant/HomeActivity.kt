package com.march.iwant

import android.os.Bundle
import android.view.View
import com.march.dev.app.activity.BaseActivity
import kotlinx.android.synthetic.main.home_activity.*

/**
 *  CreateAt : 2017/1/18
 *  Describe :
 *  @author chendong
 */
class HomeActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.home_activity
    }

    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        mHomeTv.text = "测试"
    }

}