package com.example.myapplication

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService{

    @GET("/cal")//서버에 GET 요청을 할 주소를 입력
    fun getresult() : Call<ResponseDTO> //MainActivity에서 불러와서 이 함수에 큐를 만들고 대기열에 콜백을 넣어주면 그거갖고 요청하는거임.

    //mlkit post로 보내기
    @FormUrlEncoded
    @POST("mlkit")
    fun postData(
        @Field("mlkitText") mlkitText : String
    ) : Call<mlkitDTO>


    //srchword post
    @FormUrlEncoded
    @POST("srch")
    fun srchData(
        @Field("srchText") srchText : String
    ) : Call<srchDTO>

    //videoId post - 최근 시청한 영상
    @FormUrlEncoded
    @POST("videoid")
    fun videoData(
        @Field("email") email: String,
        @Field("videoId") videoId : String,
        @Field("title") title : String
    ) : Call<videoIdDTO>

    //북마크 - 유저 이메일과  videoid post
    @FormUrlEncoded
    @POST("BMvideoid")
    fun bmvideoData(
        @Field("useremail") useremail : String,
        @Field("videoid") videoid : String,
        @Field("title") title : String
    ) : Call<bookmarkDTO>

    //검색어 get 테스트
    @GET("/srch")//서버에 GET 요청을 할 주소를 입력
    fun srchresult() : Call<List<srchRelatedDTO>>

    //userid get
    @GET("/user")
    fun useridget() : Call<List<useridDTO>>

    //userid get 이메일값 같이 전달
    @GET("/user")
    fun useridget2(@Query("email") email:String) : Call<List<useridDTO>>

    // ! 일치율 !
    @GET("/srch-rate")
    fun rateResult(@Query("srch_word") srch_word:String, @Query("video_id") video_id:String): Call<List<SrchRateDTO>>

    //북마크 get 이메일값 같이 전달
    @GET("/BMvideoid")
    fun BMvideoidget(@Query("email") email:String) : Call<List<userBMDTO>>

    // 파이차트 get 이메일값 같이 전달
    @GET("/PieChart")
    fun PieChartget(@Query("email") email:String) : Call<List<PieChartDTO>>


    //최근 시청한 영상 get 이메일값 같이 전달
    @GET("/Recentvideoid")
    fun Recentvideoid(@Query("email") email:String) : Call<List<userRecentDTO>>

    //선호카테고리 get 이메일값 같이 전달
    @GET("/Favoritevideoid")
    fun Favoritevideoid(@Query("email") email:String) : Call<List<userFavoriteDTO>>

    //user post 테스트
    @FormUrlEncoded
    @POST("user")
    fun userData(
        @Field("useremail") useremail : String,
        @Field("userpw") userpw : String,
        @Field("userbirth") userbirth : Int //int? string?
    ) : Call<UserDTO>


    //userid get
    @GET("/videoid")
    fun videoIdget(
        @Query("email") email:String
    ) : Call<List<videoIdDTO>>


}




/*
interface RetrofitPost{
    @POST("/cal")
    //fun sendReq(@Body postdto: PostDTO) : Call<PostDTO>
    fun post(@FieldMap param: HashMap<String, String>): Call<PostDTO>
}
*/



interface NetworkService {
    @GET("youtube/v3/search")
    fun getList(
        @Query("key") key:String,
        @Query("q") search_query : String,
        @Query("type") returnType: String,
        @Query("part") returnData : String,
        @Query("order") order : String,
        @Query("videoDuration") videoDuration: String
    ): Call<SearchListResponse>
}

//videoID로 불러오기 시도
interface VideoIDlist {
    @GET("youtube/v3/videos")
    fun getvideoList(
        @Query("key") key:String,
        @Query("id") id:String,
        @Query("part") returnData : String
    ): Call<SearchListResponse>
}