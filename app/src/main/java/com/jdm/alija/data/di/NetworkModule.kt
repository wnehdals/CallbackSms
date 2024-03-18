package com.jdm.alija.data.di

import android.content.Context
import android.os.Build
import com.jdm.alija.BuildConfig
import com.jdm.alija.data.remote.MapService
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AUTH
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val defaultConnectTimeout = 10L
    private const val defaultReadTimeout = 30L
    private const val defaultWriteTimeout = 15L
    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.HEADERS
        }
    }
    private val timeoutInterceptor = Interceptor { chain ->
        val request = chain.request()

        val connectTimeout = request.header("CONNECT_TIMEOUT")?.toInt() ?: defaultConnectTimeout
        val readTimeout = request.header("READ_TIMEOUT")?.toInt() ?: defaultReadTimeout
        val writeTimeout = request.header("WRITE_TIMEOUT")?.toInt() ?: defaultWriteTimeout

        val builder = request.newBuilder()
        builder.removeHeader("CONNECT_TIMEOUT")
        builder.removeHeader("READ_TIMEOUT")
        builder.removeHeader("WRITE_TIMEOUT")

        chain.withConnectTimeout(connectTimeout.toInt(), TimeUnit.SECONDS)
            .withReadTimeout(readTimeout.toInt(), TimeUnit.SECONDS)
            .withWriteTimeout(writeTimeout.toInt(), TimeUnit.SECONDS)
            .proceed(builder.build())
    }
    @AUTH
    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain: Interceptor.Chain ->
            val request = chain.request().newBuilder()
                .headers(
                    Headers.headersOf(
                        "Authorization", "KakaoAK ${BuildConfig.kakao_rest_api_key}"
                    )
                )
                .build()
            chain.proceed(request)
        }
    }

    @AUTH
    @Provides
    @Singleton
    fun provideAuthOkHttpClient(
        @AUTH headerInterceptor: Interceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(defaultConnectTimeout, TimeUnit.SECONDS)
            readTimeout(defaultReadTimeout, TimeUnit.SECONDS)
            writeTimeout(defaultWriteTimeout, TimeUnit.SECONDS)
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(headerInterceptor)
        }.build()
    }

    @AUTH
    @Provides
    @Singleton
    fun provideAuthRetrofit(@AUTH okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://dapi.kakao.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @AUTH
    @Provides
    @Singleton
    fun provideAuthApi(@AUTH retrofit: Retrofit): MapService {
        return retrofit.create(MapService::class.java)
    }
}