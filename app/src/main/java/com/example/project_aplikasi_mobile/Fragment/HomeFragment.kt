package com.example.project_aplikasi_mobile

import SliderAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.project_aplikasi_mobile.Api.ApiClient
import com.example.project_aplikasi_mobile.Model.JadwalItem
import com.example.project_aplikasi_mobile.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var barChart: BarChart
    private val handler = Handler(Looper.getMainLooper())
    private val imageList = listOf(
        R.drawable.listrik_1,
        R.drawable.listrik_2,
        R.drawable.listrik_3
    )
    private var currentPage = 0

    private val sliderRunnable = object : Runnable {
        override fun run() {
            if (currentPage == sliderAdapter.itemCount) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(this, 3000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        viewPager = view.findViewById(R.id.viewPager)
        sliderAdapter = SliderAdapter(imageList)
        viewPager.adapter = sliderAdapter
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
            }
        })

        barChart = view.findViewById(R.id.barChart)
        fetchAndDisplayData()

        return view
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(sliderRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(sliderRunnable)
    }

    private fun fetchAndDisplayData() {
        lifecycleScope.launch {
            val apiService = ApiClient.getInstance(requireContext())
            val jadwalItems = apiService.getdataJadwals()
            setupBarChart(jadwalItems)
        }
    }

    private fun setupBarChart(jadwalItems: List<JadwalItem>) {
        val entries = jadwalItems.mapIndexed { index, jadwalItem ->
            BarEntry(index.toFloat(), jadwalItem.jadwal_id.toFloat())
        }
        val dataSet = BarDataSet(entries, "Jadwal ID")
        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.invalidate()
    }
}
