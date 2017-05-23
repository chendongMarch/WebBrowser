package com.march.iwant.pz.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import com.march.iwant.pz.PzFragment
import com.march.iwant.pz.model.PzTabModel

/**
 *  CreateAt : 2017/3/2
 *  Describe :
 *  @author chendong
 */
class PzPagerAdapter(fm: FragmentManager, var mDatas: List<PzTabModel>) : FragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence {
        return mDatas[position].name
    }
    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return mDatas.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Fragment {
        val model = mDatas[position]
        return PzFragment.newInst(model.type, model.url)
    }
}