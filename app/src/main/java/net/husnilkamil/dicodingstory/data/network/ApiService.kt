package net.husnilkamil.dicodingstory.data.network

import net.husnilkamil.dicodingstory.data.response.InsertResponse
import net.husnilkamil.dicodingstory.data.response.ListStoryResponse
import net.husnilkamil.dicodingstory.data.response.LoginResponse
import net.husnilkamil.dicodingstory.data.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/v1/register")
    fun registerUser(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("/v1/login")
    fun loginUser(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>

    @GET("/v1/stories")
    fun getStoriesAll(
        @Header("Authorization") token : String,
        @Query("location") location: Int
    ): Call<ListStoryResponse>

    @Multipart
    @POST("/v1/stories")
    suspend fun addStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part?,
        @Part("description") description: RequestBody?
    ): InsertResponse


    //Paging
    @GET("/v1/stories")
    suspend fun getStories(
        @Header("Authorization") token : String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): ListStoryResponse

}