package com.example.project_aplikasi_mobile

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.project_aplikasi_mobile.Api.ApiClient
import com.example.project_aplikasi_mobile.Model.JadwalItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class JadwalFragment : Fragment() {

    private lateinit var tableLayout: TableLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_jadwal, container, false)
        tableLayout = view.findViewById(R.id.tableLayout)
        fetchJadwalData()
        return view
    }

    private fun fetchJadwalData() {
        context?.let { ctx ->
            ApiClient.getInstance(ctx).getdataJadwal().enqueue(object : Callback<List<JadwalItem>> {
                override fun onResponse(call: Call<List<JadwalItem>>, response: Response<List<JadwalItem>>) {
                    if (response.isSuccessful) {
                        response.body()?.let { jadwalItems ->
                            populateTable(jadwalItems)
                        }
                    } else {
                        Log.e("JadwalFragment", "Failed to fetch jadwal data: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<JadwalItem>>, t: Throwable) {
                    Log.e("JadwalFragment", "Error fetching jadwal data", t)
                }
            })
        }
    }

    private fun populateTable(jadwalItems: List<JadwalItem>) {
        // Clear any existing rows (excluding the header row)
        tableLayout.removeViews(1, tableLayout.childCount - 1)

        for (item in jadwalItems) {
            val tableRow = TableRow(requireContext())

            val awalPemeriksaan = TextView(requireContext()).apply {
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                text = item.awal_pemeriksaan
                setPadding(8, 8, 8, 8)
                gravity = Gravity.CENTER
            }

            val batasPemeriksaan = TextView(requireContext()).apply {
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                text = item.batas_pemeriksaan
                setPadding(8, 8, 8, 8)
                gravity = Gravity.CENTER
            }

            val lokasiFeeder = TextView(requireContext()).apply {
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                text = item.lokasi_feeder
                setPadding(8, 8, 8, 8)
                gravity = Gravity.CENTER
            }

            val status = TextView(requireContext()).apply {
                layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                text = item.status
                setPadding(8, 8, 8, 8)
                gravity = Gravity.CENTER

                setBackgroundColor(
                    when (item.status.toLowerCase()) {
                        "open" -> ContextCompat.getColor(requireContext(), R.color.status_open)
                        else -> ContextCompat.getColor(requireContext(), R.color.status_close)
                    }
                )
            }

            tableRow.addView(awalPemeriksaan)
            tableRow.addView(batasPemeriksaan)
            tableRow.addView(lokasiFeeder)
            tableRow.addView(status)

            tableLayout.addView(tableRow)
        }
    }
}
