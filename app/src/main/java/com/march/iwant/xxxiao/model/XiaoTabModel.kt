package com.march.iwant.xxxiao.model

/**
 *  CreateAt : 2017/3/2
 *  Describe :
 *  @author chendong
 */
data class XiaoTabModel(var type: Int) {

    var name = ""
    var linkUrl = ""

    companion object {
        val TYPE_INEW = 0 //最新美女
        val TYPE_XGMN = 1// 性感美女
        val TYPE_SNLL = 2// 少女萝莉
        val TYPE_MRXT = 3// 美乳香臀
        val TYPE_SWMT = 4// 丝袜美腿
        val TYPE_XGTX = 5//  性感特写
        val TYPE_OMNS = 6//  欧美女神
        val TYPE_RHDY = 7//  日韩东亚
        val TYPE_NSHJ = 8//  女神合集
        val TYPE_MNBZ = 9//  美女壁纸
    }

    private val URL_INEW = "http://m.xxxiao.com/"
    private val URL_XGMN = "http://m.xxxiao.com/cat/xinggan"
    private val URL_SNLL = "http://m.xxxiao.com/cat/shaonv"
    private val URL_MRXT = "http://m.xxxiao.com/cat/mrxt"
    private val URL_SWMT = "http://m.xxxiao.com/cat/swmt"
    private val URL_XGTX = "http://m.xxxiao.com/cat/xgtx"
    private val URL_OMNS = "http://m.xxxiao.com/cat/oumei"
    private val URL_RHDY = "http://m.xxxiao.com/cat/rihandongya"
    private val URL_NSHJ = "http://m.xxxiao.com/cat/collection"
    private val URL_MNBZ = "http://m.xxxiao.com/cat/wallpaper"

    init {
        when (type) {
            TYPE_INEW -> {
                name = "最新"
                linkUrl = URL_INEW
            }
            TYPE_XGMN -> {
                name = "性感美女"
                linkUrl = URL_XGMN
            }
            TYPE_SNLL -> {
                name = "少女萝莉"
                linkUrl = URL_SNLL
            }
            TYPE_MRXT -> {
                name = "美乳香臀"
                linkUrl = URL_MRXT

            }
            TYPE_SWMT -> {
                name = "丝袜美腿"
                linkUrl = URL_SWMT
            }
            TYPE_XGTX -> {
                name = "性感特写"
                linkUrl = URL_XGTX
            }
            TYPE_OMNS -> {
                name = "欧美女神"
                linkUrl = URL_OMNS
            }
            TYPE_RHDY -> {
                name = "日韩东亚"
                linkUrl = URL_RHDY
            }
            TYPE_NSHJ -> {
                name = "女神合集"
                linkUrl = URL_NSHJ
            }
            TYPE_MNBZ -> {
                name = "美女壁纸"
                linkUrl = URL_MNBZ
            }
        }
    }
}