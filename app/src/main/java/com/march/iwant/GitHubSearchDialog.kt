package com.march.iwant

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import com.march.dev.app.dialog.BaseDialog
import com.march.dev.utils.CheckUtils


/**
 * CreateAt : 2017/1/16
 * Describe : 选择github搜索条件的dialog
 * @author chendong
 */
class GitHubSearchDialog(context: Context) : BaseDialog(context) {

    private lateinit var mSpinner: Spinner
    private lateinit var mEt: EditText
    private lateinit var datas: MutableList<SortType>
    private lateinit var listener: OnChooseSearchListener

    internal inner class SortType(var sortType: String, var display: String) {
        override fun toString(): String {
            return display
        }
    }

    interface OnChooseSearchListener {
        fun onChoose(sort: String, query: String)
    }

    fun setListener(listener: OnChooseSearchListener) {
        this.listener = listener
    }

    override fun initViews() {
        registerClickListener(R.id.tv_cancel, R.id.tv_confirm)

        mEt = getView<EditText>(R.id.et)
        mSpinner = getView(R.id.spinner)
        datas = arrayListOf()
        datas.add(SortType(GitHubSearchActivity.SORT_BY_FORK, "Fork数排序"))
        datas.add(SortType(GitHubSearchActivity.SORT_BY_STAR, "Star数排序"))
        datas.add(SortType(GitHubSearchActivity.SORT_BY_MACTH, "名称相近数排序"))
        datas.add(SortType(GitHubSearchActivity.SORT_BY_UPDATE, "最近更新数排序"))
        mSpinner.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, android.R.id.text1, datas)
    }


    override fun onClickView(view: View?) {
        when (view!!.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_confirm -> {
                val trim = mEt.text.toString().trim { it <= ' ' }
                if (!CheckUtils.isEmpty(trim)) {
                    listener.onChoose(datas[mSpinner.selectedItemPosition].sortType, trim)
                }
                dismiss()
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.dialog_github
    }

    override fun setWindowParams() {
        buildDefaultParams(MATCH, WRAP, Gravity.BOTTOM)
        setBottomToCenterAnimation()
    }
}
