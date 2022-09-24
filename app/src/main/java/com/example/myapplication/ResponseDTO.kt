package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class ResponseDTO (
    var result: String)

data class SrchRateDTO (
    //@SerializedName("srch_word")
    //var srch_word: String,

    //@SerializedName("video_id")
    //var video_id: String,

    @SerializedName("rate")
    var rate: Float
)


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

// user_id get
data class useridDTO(
    @SerializedName("user_id")
    val user_id : Int
)
// 북마크
data class bookmarkDTO(
    @SerializedName("useremail")
    var useremail: String,
    @SerializedName("videoid")
    var videoid: String
)