package com.march.iwant

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.march.dev.app.activity.BaseActivity
import com.march.dev.helper.LinerDividerDecoration
import com.march.dev.utils.ActivityAnimUtils
import com.march.dev.widget.TitleBarView
import com.march.iwant.model.HomeDataModel
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import com.march.lib.adapter.core.SimpleRvAdapter
import kotlinx.android.synthetic.main.home_activity.*

/**
 *  CreateAt : 2017/1/18
 *  Describe :
 *  @author chendong
 */
class HomeActivity : BaseActivity() {

    private lateinit var mHomeAdapter: SimpleRvAdapter<HomeDataModel>
    private lateinit var mDataList: MutableList<HomeDataModel>

    override fun getLayoutId(): Int {
        return R.layout.home_activity
    }

    override fun onInitDatas() {
        super.onInitDatas()
        mDataList = arrayListOf()
        mDataList.add(HomeDataModel(0, "泡在网上的日子", "做最好的移动开发社区", "http://www.jcodecraeer.com/templets/jcodecraeer/images/logo.png?v=1.0", 0))
        mDataList.add(HomeDataModel(1, "GitHub", "快捷搜索", "https://github.com/fluidicon.png", 0))
    }


    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        mTitleBarView.setText(TitleBarView.CENTER, "主页")
        mHomeAdapter = object : SimpleRvAdapter<HomeDataModel>(mContext, mDataList, R.layout.home_item) {
            override fun onBindView(holder: BaseViewHolder<HomeDataModel>, data: HomeDataModel, pos: Int, type: Int) {
                holder.setText(R.id.tv_title, data.title)
                        .setText(R.id.tv_desc, data.desc)
                val imageView: ImageView = holder.getView(R.id.iv_icon)
                Glide.with(mContext).load(data.imagePath).into(imageView)
            }
        }
        mHomeAdapter.setItemListener(object : SimpleItemListener<HomeDataModel>() {
            override fun onClick(pos: Int, holder: BaseViewHolder<*>?, data: HomeDataModel?) {
                dispatchItemClick(data)
            }
        })

        mHomeRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        LinerDividerDecoration.attachRecyclerView(mHomeRv)
        mHomeRv.adapter = mHomeAdapter
    }


    private fun dispatchItemClick(data: HomeDataModel?) {
        var intent = Intent(HomeActivity@ this, PzActivity::class.java)
        when (data?.id) {
            0 -> {
                intent = Intent(HomeActivity@ this, PzActivity::class.java)
            }
            1 -> {
                intent = Intent(HomeActivity@ this, GitHubSearchActivity::class.java)
            }
        }
        startActivity(intent)
        ActivityAnimUtils.translateStart(mActivity)
    }
}