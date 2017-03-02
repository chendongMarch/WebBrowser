package com.march.iwant.common

import org.jsoup.select.Elements

/**
 *  CreateAt : 2017/3/2
 *  Describe :
 *  @author chendong
 */
object JsoupHelper{

    fun getTextIfExist(elements: Elements, defaultStr: String): String {
        if (elements.size > 0) {
            return elements[0].text()
        }
        return defaultStr
    }
}