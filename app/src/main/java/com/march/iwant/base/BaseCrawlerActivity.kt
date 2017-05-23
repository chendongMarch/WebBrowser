package com.march.iwant.base

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import bolts.Continuation
import bolts.Task
import com.march.dev.helper.LinerDividerDecoration
import com.march.dev.helper.Logger
import com.march.dev.helper.Toaster
import com.march.dev.utils.ActivityAnimUtils
import com.march.dev.utils.NetUtils
import com.march.iwant.R
import com.march.lib.adapter.common.OnLoadMoreListener
import com.march.lib.adapter.common.SimpleItemListener
import com.march.lib.adapter.core.BaseViewHolder
import com.march.lib.adapter.core.SimpleRvAdapter
import com.march.lib.adapter.module.LoadMoreModule
import org.jsoup.select.Elements
import java.util.*
import java.util.concurrent.Callable

/**
 * CreateAt : 2017/1/16
 * Describe : 网页爬取数据的基类
 * @author chendong
 */
abstract class BaseCrawlerActivity<D> : IWantActivity() {

    lateinit var mDatas: MutableList<D>
    lateinit var mAdapter: SimpleRvAdapter<D>
    lateinit var mRv: RecyclerView
    var isLoadOver = false


    override fun onInitDatas() {
        super.onInitDatas()
        mDatas = ArrayList<D>()
        mRv = findViewById(R.id.clawler_rv) as RecyclerView
    }

    class MyLinearLayoutManager(context: Context, orientation: Int, reverse: Boolean)
        : LinearLayoutManager(context, orientation, reverse) {
        override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
            try {
                super.onLayoutChildren(recycler, state)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)
        if (!initRecyclerView()) {
            mRv.layoutManager = MyLinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
            LinerDividerDecoration.attachRecyclerView(mRv)
        }
        mRv.post {
            createAdapter()
        }
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

            override fun onBindHeader(header: BaseViewHolder<D>?) {

            }
        }
        mAdapter.addLoadMoreModule(LoadMoreModule(3, getLoadMoreListener()))
        mAdapter.setItemListener(getItemListener())
//        mAdapter.addHFModule(HFModule(mContext, HFModule.NO_RES, R.layout.common_load_more, mRv))
//        mAdapter.hfModule.setFooterEnable(false)
        setUpAdapter()
        mRv.adapter = mAdapter
    }

    open fun setUpAdapter() {

    }


    override fun onStartWorks() {
        super.onStartWorks()
        startCrawlerTask()
    }

    protected fun getTextIfExist(elements: Elements, defaultStr: String): String {
        if (elements.size > 0) {
            return elements[0].text()
        }
        return defaultStr
    }


    protected fun startCrawlerTask(): Boolean {
        if (isLoadOver) return false
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
                Logger.e("chendong", task.error)
                mAdapter.loadMoreModule.finishLoad()
                Toaster.get().show(mContext, "加载失败，请稍候")
            } else {
                updateAdapter()
            }
        }, Task.UI_THREAD_EXECUTOR)
        return true
    }

    protected fun updateAdapter() {
        mRv.post {
            mAdapter.loadMoreModule.finishLoad()
            mAdapter.appendTailRangeData(mDatas, true)
        }
    }


    protected abstract fun onBindDataShow(holder: BaseViewHolder<D>, data: D, pos: Int, type: Int)

    protected abstract fun getItemListener(): SimpleItemListener<D>

    protected abstract fun getLoadMoreListener(): OnLoadMoreListener

    @Throws(Exception::class)
    protected abstract fun crawler()

    protected abstract fun getAdapterLayoutId(): Int

    override fun finish() {
        super.finish()
        ActivityAnimUtils.translateFinish(mActivity)
    }


    // Kotlin同样支持嵌套的内部类，不过和Java不一样的是，
    // Kotlin的内部类不会默认包含一个指向外部类对象的引用，
    // 也就是说，Kotlin中所有的内部类默认就是静态的，这样可以减少很多内存泄露的问题。
    // 另外，如果需要在内部类中引用外部类对象，可以在Inner类的声明前加上inner关键字，
    // 然后在Inner类中使用标记的this：this@Outer来指向外部类对象
    inner class AdapterImpl(context: Context, datas: List<D>, layoutId: Int) : SimpleRvAdapter<D>(context, datas, layoutId) {

        override fun onBindView(holder: BaseViewHolder<D>, data: D, pos: Int, type: Int) {
            this@BaseCrawlerActivity.onBindDataShow(holder, data, pos, type)
        }
    }
}
