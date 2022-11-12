package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class PostDTO (
    @SerializedName("title")
    var title: String )

//mlkit dataclass

data class mlkitDTO (
    @SerializedName("mlkitText")
    var mlkitText: String
    )

data class srchDTO(
    @SerializedName("srchText")
    var srchText: String
)

data class videoIdDTO(
    @SerializedName("video_id")
    var videoId: String,
    @SerializedName("title")
    var title: String

)

//user 정보 테스트
data class UserDTO(
    @SerializedName("useremail")
    var useremail : String,
    @SerializedName("userpw")
    var userpw : String,
    @SerializedName("userbirth")
    var userbirth : Int
    )
