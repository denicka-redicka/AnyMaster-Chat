package com.aymaster.test.core.remote.impl.di

import com.aymaster.test.core.remote.impl.retrofit.ChatRestApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.TimeUnit

@Module(includes = [ChatRemoteDataSourceModule::class])
internal class ChatNetworkModule {

    private val contentType = "application/json".toMediaType()

    @Provides
    fun provideJson(): Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    @Provides
    fun provideMockserver(): MockWebServer = MockWebServer()

    @Provides
    fun provideWebSocketRequest(): Request = Request.Builder()
        .url("wss://free.blr2.piesocket.com/v3/1?api_key=T6rowBSOY33E4NR8IlyPdjZcucQVgcrdCPlneep6&notify_self=1")
        .build()

    @Provides
    fun provideRetrofitService(client: OkHttpClient, json: Json): ChatRestApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://mockserver.com/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
        return retrofit.create()
    }
}