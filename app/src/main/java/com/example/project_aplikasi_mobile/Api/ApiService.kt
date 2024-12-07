package com.example.project_aplikasi_mobile.Api

import com.example.project_aplikasi_mobile.Model.LoginRequest
import com.example.project_aplikasi_mobile.Model.JadwalItem
import com.example.project_aplikasi_mobile.Model.LoginResponse
import com.example.project_aplikasi_mobile.Model.TemuanItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap


interface ApiService {
    @Multipart
    @POST("temuan")
    fun postTemuan(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>
    ): Call<TemuanItem>

    @GET("dataJadwal")
    fun getdataJadwal(): Call<List<JadwalItem>>

    @GET("dataJadwal")
    suspend fun getdataJadwals(): List<JadwalItem>

    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}



