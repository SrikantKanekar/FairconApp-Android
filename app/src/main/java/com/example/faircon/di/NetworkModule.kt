package com.example.faircon.di

import com.example.faircon.business.domain.util.Urls
import com.example.faircon.framework.datasource.network.services.ConnectService
import com.example.faircon.framework.datasource.network.webSocket.WebSocketListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideWebSocketOkhttp(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideWebSocketListener(): WebSocketListener {
        return WebSocketListener()
    }

    @Singleton
    @Provides
    fun provideESP8266APRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Urls.ESP8266_AP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideConnectService(
        retrofit: Retrofit
    ): ConnectService {
        return retrofit
            .create(ConnectService::class.java)
    }
}