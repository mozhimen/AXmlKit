package com.mozhimen.xmlk.basic.bases

/**
 * @ClassName Req
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/11
 * @Version 1.0
 */
data class aa(
    val code: Int,
    val `data`: List<Data>,
    val msg: String
)

data class Data(
    val id: Int,
    val name: String
)