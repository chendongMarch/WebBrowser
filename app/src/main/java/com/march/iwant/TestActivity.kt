package com.march.iwant

import android.os.Bundle
import android.util.Log
import android.view.View
import com.march.dev.app.activity.BaseActivity

/**
 *  CreateAt : 2017/5/23
 *  Describe :
 *  @author chendong
 */
class TestActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return 0
    }

    override fun onInitViews(view: View?, saveData: Bundle?) {
        super.onInitViews(view, saveData)

        val myView = View(mContext)

        // 在kotlin中，
        // 如果函数的最后一个参数是函数，那么这个参数可以直接写在圆括号外面（要用花括号），
        // 如果只有一个函数参数，可以直接省略圆括号！
        myView.setOnClickListener {
            v ->
            run {
                Log.e("chendong", "test" + v.id)
            }
        }

        myView.setOnClickListener {
            v ->
            if (v.id == 0) {
                Log.e("chendong", "test${v.id})")
            } else {
                Log.e("chendong", "test${v.id})")
            }
        }

        myView.setOnClickListener {
            Log.e("chendong", "test${it.id})")
        }

        testFun("") {
            Log.e("chendong", "test$it)")
        }
        testFun {
            Log.e("chendong", "test$it)")
        }
    }


    fun testFun(lis: (str: String) -> Unit) {

    }

    fun testFun(param: String, lis: (str: String) -> Unit): Int {
        return 0
    }


    // 单表达式函数
    fun testFun2() = 100

    // 单表达式函数结合when
    fun testFun3(param: Int) = when (param) {
        0 -> 0
        in 1..5 -> 1
        5, 6, 7 -> 2
        else -> 100
    }


    // 循环
    fun testFun4() {

        // 遍历临时数组
        for (i in (1..4)) {

        }
        // 遍历list
        val lists = listOf(1, 2, 4)
        for (i in lists) {

        }

        // 遍历map
        val maps = mapOf(Pair("", ""), Pair("", ""))
        for ((key,value) in maps){

        }
    }
}