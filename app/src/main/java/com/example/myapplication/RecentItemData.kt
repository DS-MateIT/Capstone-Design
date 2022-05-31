package com.example.myapplication

import android.graphics.drawable.Drawable


class RecentItemData {
    var Image: String? = null
    var rate: String? = null
    var keyword: String? = null
    var hashtag: String? = null

    constructor(Image: String, rate: String, keyword: String, hashtag: String) {
        this.Image = Image
        this.rate = rate
        this.keyword = keyword
        this.hashtag = hashtag
    }
    constructor() {}
}
