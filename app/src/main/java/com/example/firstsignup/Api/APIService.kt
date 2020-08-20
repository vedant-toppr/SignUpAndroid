package com.example.firstsignup.Api

import com.example.firstsignup.Model.LogInResponse
import com.example.firstsignup.Model.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.PUT

interface APIService {
    @FormUrlEncoded
    @POST("core/users/")
    fun createUser(
            @Field("username") username: String?,
            @Field("first_name") first_name: String?,
            @Field("last_name") last_name: String?,
            @Field("email") email: String?,
            @Field("phone") phone: String?,
            @Field("password") password: String?): Call<User?>?

    @FormUrlEncoded
    @POST("token-auth/")
    fun userLogin(
            @Field("username") username: String?,
            @Field("password") password: String?): Call<LogInResponse?>?

    @FormUrlEncoded
    @PUT("/core/generate_otp/")
    fun getOTP(
            @Field("phone") phone: String?): Call<ResponseBody>?

    @FormUrlEncoded
    @PUT("/core/forget_pass/")
    fun setPass(
            @Field("phone") phone: String?,
            @Field("otp") otp: String?,
            @Field("new_password") new_password: String?): Call<ResponseBody>?
}