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
        @Field("videoId") videoId : String
    ) : Call<videoIdDTO>

    //videoId post - 북마크
    @FormUrlEncoded
    @POST("BMvideoid")
    fun bmvideoData(
        @Field("videoId") videoId : String
    ) : Call<videoIdDTO>





    //검색어 get 테스트
    @GET("/srch")//서버에 GET 요청을 할 주소를 입력
    fun srchresult() : Call<List<srchRelatedDTO>>


    //user post 테스트
    @FormUrlEncoded
    @POST("user")
    fun userData(
        @Field("useremail") useremail : String,
        @Field("userpw") userpw : String,
        @Field("userbirth") userbirth : Int //int? string?
    ) : Call<UserDTO>


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
        @Query("part") returnData : String
    ): Call<SearchListResponse>
}