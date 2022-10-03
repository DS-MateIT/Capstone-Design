package com.example.myapplication

//최근 시청 영상 - firebase 임시 데이터
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




//youtube api
data class SearchListResponse(
    val items:ArrayList<SearchResult> = ArrayList<SearchResult>()

)
data class SearchResult(
    val id: SearchResultId?,
    val snippet:SearchResultSnippet?
)

data class SearchResultId (
    val kind:String?,
    val videoId:String?
)

data class SearchResultSnippet (
    val publishedAt:String?,
    val title:String?,
    val description:String?,
    val thumbnails:Thumbnails
)

data class Thumbnails(
    val default:ThumbnailsInfo?,
    val medium:ThumbnailsInfo?,
    val high:ThumbnailsInfo?
)

data class ThumbnailsInfo(
    val url:String?,
    val width:Int?,
    val height:Int?
)
