package com.example.myapplication
import java.io.Serializable

class VideoItem: Serializable{
    var title: String? = null
    var sources: String? = null
    var thumb: String? = null
    var description: String? = null

    constructor(title: String?, sources: String?, thumb: String?, description: String?) {
        this.title = title
        this.sources = sources
        this.thumb = thumb
        this.description = description
    }

    constructor() {}
}