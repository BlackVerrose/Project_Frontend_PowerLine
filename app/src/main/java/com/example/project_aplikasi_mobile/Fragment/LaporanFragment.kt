package com.example.project_aplikasi_mobile.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.project_aplikasi_mobile.PerbaikanActivity
import com.example.project_aplikasi_mobile.R

class LaporanFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_laporan, container, false)


        // onClickListener untuk CardViewPerbaikan
        val cardView = view.findViewById<CardView>(R.id.cardPerbaikan)
        cardView.setOnClickListener {
            val PerbaikanIntent = Intent(activity, PerbaikanActivity::class.java)
            startActivity(PerbaikanIntent)
        }

        return view
    }
}