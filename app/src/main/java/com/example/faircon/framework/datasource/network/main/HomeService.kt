package com.example.faircon.framework.datasource.network.main

import com.example.faircon.business.domain.model.Parameter
import com.example.faircon.framework.datasource.network.GenericResponse
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import javax.inject.Singleton

@Singleton
interface HomeService {

    @GET("home/parameters")
    suspend fun getParameters(): Parameter

    @PUT("home/mode")
    suspend fun setMode(
        @Body mode: ModeRequest,
    ): GenericResponse

    @GET("home/test")
    suspend fun testConnection(): GenericResponse
}

class ModeRequest(
    @SerializedName("mode")
    val mode: Int
)