package com.march.iwant.pz

import android.os.Bundle
import android.view.View
import com.march.iwant.R
import com.march.iwant.base.IWantActivity
import com.march.iwant.pz.adapter.PzPagerAdapter
import com.march.iwant.pz.model.PzTabModel
import kotlinx.android.synthetic.main.pz_home_activity.*

/**
 *  CreateAt : 2017/3/2
 *  Describe :
 *  @author chendong
 */
class PzHomeActivity : IWantActivity() {

    lateinit var mTabDatas: MutableList<PzTabModel>

    override fun onInitDatas() {
        super.onInitDatas()
        mTabDatas = mutableListOf()
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_ANDROID))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_JS))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_CSS))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_HTML5CSS3))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_DB))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_MSG_IT))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_MSG_MOBILE))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_MSG_SOCIAL))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_MSG_BUSINESS))
        mTabDatas.add(PzTabModel(PzTabModel.TYPE_MSG_ANDROID))
    }

    override fun onInitViews(view: View?, saveData: Bundle?) {
        mPzVp.adapter = PzPagerAdapter(supportFragmentManager, mTabDatas)
        mPzTabLy.setupWithViewPager(mPzVp)
    }

    override fun getLayoutId(): Int {
        return R.layout.pz_home_activity
    }

    override fun isInitTitle(): Boolean {
        return false
    }
}