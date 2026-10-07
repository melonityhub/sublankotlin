
package com.androyal.subxplayer.network

import android.content.Context
import com.androyal.subxplayer.data.models.AppConfig
import com.androyal.subxplayer.data.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    @Provides @Singleton
    fun provideSubxApi(client: OkHttpClient, repo: SettingsRepository): SubxApiService {
        val base = runBlocking { try { repo.catalogBaseUrlFlow.first() } catch(_:Exception){ AppConfig().catalogBaseUrl } }
        // Retrofit needs host; we use dynamic urls via @Url in some calls, but provide base
        val normalized = if (base.endsWith("/")) base else "$base/"
        return Retrofit.Builder()
            .baseUrl(normalized)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(SubxApiService::class.java)
    }
}
