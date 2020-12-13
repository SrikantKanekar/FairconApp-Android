package com.example.faircon.api.main

import com.example.faircon.api.GenericResponse
import com.example.faircon.models.AccountProperties
import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface MainService {

    @GET("account/properties")
    suspend fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): AccountProperties

    @PUT("account/properties/update")
    @FormUrlEncoded
    suspend fun saveAccountProperties(
        @Header("Authorization") authorization: String,
        @Field("email") email: String,
        @Field("username") username: String
    ): GenericResponse

    @PUT("account/change_password")
    @FormUrlEncoded
    suspend fun updatePassword(
        @Header("Authorization") authorization: String,
        @Field("old_password") currentPassword: String,
        @Field("new_password") newPassword: String,
        @Field("confirm_new_password") confirmNewPassword: String
    ): GenericResponse
}