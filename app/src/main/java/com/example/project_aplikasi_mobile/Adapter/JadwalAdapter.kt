package com.example.project_aplikasi_mobile.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_aplikasi_mobile.Model.JadwalItem
import com.example.project_aplikasi_mobile.R

class JadwalAdapter(private val jadwalList: List<JadwalItem>) :
    RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder>() {

    class JadwalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val jadwalawalPeriksa: TextView = view.findViewById(R.id.tvAwalPemeriksaan)
        val jadwalbatasPeriksa: TextView = view.findViewById(R.id.tvBatasPemeriksaan)
        val jadwallokasiFeeder: TextView = view.findViewById(R.id.tvLokasiFeeder)
        val jadwalketerangan: TextView = view.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.table_row, parent, false)
        return JadwalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalViewHolder, position: Int) {
        val jadwal = jadwalList[position]
        holder.jadwalawalPeriksa.text = jadwal.awal_pemeriksaan
        holder.jadwalbatasPeriksa.text = jadwal.batas_pemeriksaan
        holder.jadwallokasiFeeder.text = jadwal.lokasi_feeder
        holder.jadwalketerangan.text = jadwal.status

        // Logging for debugging
        Log.d("JadwalAdapter", "Awal Pemerikasaan: ${jadwal.awal_pemeriksaan}")
        Log.d("JadwalAdapter", "Batas Pemeriksaan: ${jadwal.batas_pemeriksaan}")
        Log.d("JadwalAdapter", "Lokasi Feeder: ${jadwal.lokasi_feeder}")
        Log.d("JadwalAdapter", "Status: ${jadwal.status}")
    }

    override fun getItemCount(): Int {
        return jadwalList.size
    }
}
