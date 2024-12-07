package com.example.project_aplikasi_mobile.Model


import com.google.gson.annotations.SerializedName

data class LokasiFeeder(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("lokasi_feeder")
    val lokasiFeeder: String,
    @SerializedName("updated_at")
    val updatedAt: String
)