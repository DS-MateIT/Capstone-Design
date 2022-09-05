package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class ResponseDTO (
    var result: String)


// 검색어, 연관검색어 get 테스트
data class srchRelatedDTO(
    @SerializedName("srch_craw1")
    val srch_craw1 : String,
    @SerializedName("srch_craw2")
    val srch_craw2 : String,
    @SerializedName("srch_craw3")
    val srch_craw3 : String,
    @SerializedName("srch_keyword")
    val srch_keyword : String,
    @SerializedName("word_no")
    val word_no : Int

)