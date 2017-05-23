package com.march.iwant.xxxiao

import android.os.Bundle
import android.view.View
import com.march.iwant.R
import com.march.iwant.base.IWantActivity
import com.march.iwant.pz.adapter.XiaoPagerAdapter
import com.march.iwant.xxxiao.model.XiaoTabModel
import kotlinx.android.synthetic.main.xiao_home_activity.*

/**
 *  CreateAt : 2017/3/2
 *  Describe :
 *  @author chendong
 */
class XiaoHomeActivity : IWantActivity() {

    lateinit var mTabDatas: MutableList<XiaoTabModel>

    override fun onInitDatas() {
        super.onInitDatas()
        mTabDatas = mutableListOf()
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_INEW))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_XGMN))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_SNLL))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_MRXT))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_SWMT))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_XGTX))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_OMNS))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_RHDY))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_NSHJ))
        mTabDatas.add(XiaoTabModel(XiaoTabModel.TYPE_MNBZ))
    }

    override fun onInitViews(view: View?, saveData: Bundle?) {
        mXiaoVp.adapter = XiaoPagerAdapter(supportFragmentManager, mTabDatas)
        mXiaoTabLy.setupWithViewPager(mXiaoVp)
    }

    override fun getLayoutId(): Int {
        return R.layout.xiao_home_activity
    }

    override fun isInitTitle(): Boolean {
        return false
    }
}