package com.example.project_aplikasi_mobile.Model

data class JadwalItem(
    val awal_pemeriksaan: String,
    val batas_pemeriksaan: String,
    val created_at: String,
    val jadwal_id: Int,
    val lokasi_feeder: String,
    val lokasi_feeder_id: Int,
    val status: String,
    val updated_at: String,
    val user_id: Int
)