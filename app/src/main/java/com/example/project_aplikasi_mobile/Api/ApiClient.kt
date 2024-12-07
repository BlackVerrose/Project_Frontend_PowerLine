package com.example.project_aplikasi_mobile.Api

import android.content.Context
import com.example.project_aplikasi_mobile.utils.PreferenceHelper
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private const val BASE_URL = "https://my-reporting.com/public/api/"

    fun getInstance(context: Context): ApiService {
        val preferenceHelper = PreferenceHelper(context)
        val token = preferenceHelper.getToken()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC // Atur level sesuai kebutuhan Anda
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor) // Tambahkan logging interceptor ke client
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val requestBuilder: Request.Builder = original.newBuilder()
                    .method(original.method, original.body)
                token?.let {
                    requestBuilder.addHeader("Authorization", "Bearer $it")
                }
                val request: Request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
