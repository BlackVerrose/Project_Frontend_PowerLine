package com.example.project_aplikasi_mobile.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project_aplikasi_mobile.LoginActivity
import com.example.project_aplikasi_mobile.R
import com.example.project_aplikasi_mobile.utils.PreferenceHelper

class ProfileFragment : Fragment() {

    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        preferenceHelper = PreferenceHelper(requireContext())

        val signOutButton: Button = view.findViewById(R.id.sign_out_button)
        signOutButton.setOnClickListener {
            clearToken()
            navigateToLogin()
        }

        return view
    }

    private fun clearToken() {
        preferenceHelper.clearToken()
    }

    private fun navigateToLogin() {
        val intent = Intent(activity, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        activity?.finish()
        showToast("Anda berhasil logout")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
