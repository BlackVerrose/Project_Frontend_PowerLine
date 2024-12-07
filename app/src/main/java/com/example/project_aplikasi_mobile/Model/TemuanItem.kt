package com.example.project_aplikasi_mobile.Model

data class TemuanItem(
    val created_at: String,
    val equipment_type: String,
    val finding: String,
    val gambar: Array<String>,
    val lokasi_feeder: String,
    val no_pole: String,
    val status: String,
    val temuan_id: Int,
    val updated_at: String
)
