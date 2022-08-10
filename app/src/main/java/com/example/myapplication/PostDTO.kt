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


