package com.march.iwant.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.march.dev.helper.LinerDividerDecoration
import com.march.dev.utils.ActivityAnimUtils
import com.march.dev.widget.TitleBarView
import com.march.iwant.R
import com.march.iwant.base.IWantActivity
import com.march.iwant.github.GitHubSearchActivity
import com.march.iwant.home.model.HomeDataModel
import com.march.iwant.pz.PzHomeActivity
import com.march.iwant.runoob.RunoobActivity
import com.march.iwant.xxxiao.XiaoHomeActivity
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import com.march.lib.adapter.core.SimpleRvAdapter
import kotlinx.android.synthetic.main.home_activity.*

/**
 *  CreateAt : 2017/1/18
 *  Describe : 主页Activity
 *  @author chendong
 */
class HomePageActivity : IWantActivity() {

    private val mHomeAdapter: SimpleRvAdapter<HomeDataModel> by lazy {
        initAdapter()
    }
    private lateinit var mDataList: MutableList<HomeDataModel>

    // 初始化adapter
    private fun initAdapter(): SimpleRvAdapter<HomeDataModel> {
        val adapter = object : SimpleRvAdapter<HomeDataModel>(mContext, mDataList, R.layout.home_item_list) {
            override fun onBindView(holder: BaseViewHolder<HomeDataModel>, data: HomeDataModel, pos: Int, type: Int) {
                holder.setText(R.id.tv_title, data.title)
                        .setText(R.id.tv_desc, data.desc)
                val imageView: ImageView = holder.getView(R.id.iv_icon)
                Glide.with(mContext).load(data.imagePath).into(imageView)
            }
        }
        adapter.setItemListener(object : SimpleItemListener<HomeDataModel>() {
            override fun onClick(pos: Int, holder: BaseViewHolder<*>?, data: HomeDataModel?) {
                dispatchItemClick(data)
            }
        })
        return adapter
    }

    override fun getLayoutId(): Int {
        return R.layout.home_activity
    }

    override fun onInitDatas() {
        super.onInitDatas()
        mDataList = arrayListOf()
        mDataList.add(HomeDataModel(0, "泡在网上的日子", "做最好的移动开发社区",
                "http://www.jcodecraeer.com/templets/jcodecraeer/images/logo.png?v=1.0", 0))
        mDataList.add(HomeDataModel(1, "GitHub", "快捷搜索", "https://github.com/fluidicon.png", 0))
        mDataList.add(HomeDataModel(3, "菜鸟教程", "RUNOOB.COM", "https://github.com/fluidicon.png", 0))
        mDataList.add(HomeDataModel(2, "Xxxiao.com", "海纳百川，有容乃大", "", 0 ))
    }


    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        mTitleBarView.setText(TitleBarView.CENTER, "主页")
        mHomeRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        LinerDividerDecoration.attachRecyclerView(mHomeRv)
        mHomeRv.adapter = mHomeAdapter
    }


    private fun dispatchItemClick(data: HomeDataModel?) {
        var jumpIntent = Intent()
        when (data?.id) {
            0 -> {
                jumpIntent = Intent(HomeActivity@ this, PzHomeActivity::class.java)
            }
            1 -> {
                jumpIntent = Intent(HomeActivity@ this, GitHubSearchActivity::class.java)
            }
            2 -> {
                jumpIntent = Intent(HomeActivity@ this, XiaoHomeActivity::class.java)
            }
            3 -> {
                jumpIntent = Intent(HomeActivity@ this, RunoobActivity::class.java)
            }

        }
        startActivity(jumpIntent)
        ActivityAnimUtils.translateStart(mActivity)
    }
}