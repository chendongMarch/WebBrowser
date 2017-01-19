package com.march.iwant.model

/**
 *  CreateAt : 2017/1/19
 *  Describe : 泡在网上的日子数据类型
 *  @author chendong
 */
data class PzDataModel(var link: String, var title: String, var desc: String, var imgUrl: String) {
    init {
        this.title = title.replace("【", "[").replace("】", "]").trim { it <= ' ' }
        this.desc = desc.replace("【", "[").replace("】", "]").trim { it <= ' ' }
    }
}