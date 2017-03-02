package com.march.iwant.base

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import bolts.Continuation
import bolts.Task
import com.march.dev.helper.LinerDividerDecoration
import com.march.dev.helper.Toaster
import com.march.dev.utils.NetUtils
import com.march.iwant.R
import com.march.lib.adapter.common.OnLoadMoreListener
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import com.march.lib.adapter.core.SimpleRvAdapter
import com.march.lib.adapter.module.HFModule
import com.march.lib.adapter.module.LoadMoreModule
import java.util.*
import java.util.concurrent.Callable

/**
 * CreateAt : 2017/1/16
 * Describe : 网页爬取数据的基类
 * @author chendong
 */
abstract class BaseCrawlerFragment<D> : IWantFragment() {

    lateinit var mDatas: MutableList<D>
    lateinit var mAdapter: SimpleRvAdapter<D>
    lateinit var mRv: RecyclerView


    override fun onInitDatas() {
        super.onInitDatas()
        mDatas = ArrayList<D>()
    }


    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        mRv = view?.findViewById(R.id.clawler_rv) as RecyclerView
        if (!initRecyclerView()) {
            mRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            LinerDividerDecoration.attachRecyclerView(mRv)
        }
        createAdapter()
    }

    open fun initRecyclerView(): Boolean {
        return false
    }

    // 创建adapter
    private fun createAdapter() {
        // object: 声明内部类
        mAdapter = object : SimpleRvAdapter<D>(mContext, mDatas, getAdapterLayoutId()) {
            override fun onBindView(holder: BaseViewHolder<D>, data: D, pos: Int, type: Int) {
                onBindDataShow(holder, data, pos, type)
            }
        }
        mAdapter.addLoadMoreModule(LoadMoreModule(3, getLoadMoreListener()))
        mAdapter.setItemListener(getItemListener())
        mAdapter.addHFModule(HFModule(mContext, HFModule.NO_RES, R.layout.common_load_more, mRv))
        mAdapter.hfModule.setFooterEnable(false)
        mRv.adapter = mAdapter
    }


    override fun onStartWorks() {
        super.onStartWorks()
        startCrawlerTask()
    }


    protected fun startCrawlerTask(): Boolean {
        if (!NetUtils.isNetworkConnected(mContext)) {
            mAdapter.loadMoreModule.finishLoad()
            Toaster.get().show(mContext, "网络不给力哦")
            return false
        }

        Task.callInBackground(Callable <Any> {
            crawler()
        }).continueWith(Continuation <Any, Any> {
            task ->
            if (task.error != null) {
                mAdapter.loadMoreModule.finishLoad()
                Toaster.get().show(mContext, "加载失败，请稍候")
            } else {
                updateAdapter()
            }
        }, Task.UI_THREAD_EXECUTOR)
        return true
    }

    protected fun updateAdapter() {
        mAdapter.loadMoreModule.finishLoad()
        mAdapter.appendTailRangeData(mDatas, true)
    }

    protected abstract fun onBindDataShow(holder: BaseViewHolder<D>, data: D, pos: Int, type: Int)

    protected abstract fun getItemListener(): SimpleItemListener<D>

    protected abstract fun getLoadMoreListener(): OnLoadMoreListener

    @Throws(Exception::class)
    protected abstract fun crawler()

    protected abstract fun getAdapterLayoutId(): Int

}
