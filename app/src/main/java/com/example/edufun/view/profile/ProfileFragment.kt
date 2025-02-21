package com.example.edufun.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.edufun.R
import com.example.edufun.view.main.MainViewModel
import com.example.edufun.model.User

class ProfileFragment : Fragment() {
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var profileTextView: TextView
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profileTextView = view.findViewById(R.id.text_profile)
        logoutButton = view.findViewById(R.id.btn_logout)

        mainViewModel.getSession().observe(viewLifecycleOwner, Observer { user ->
            updateUI(user)
        })

        logoutButton.setOnClickListener {
            mainViewModel.logout()
            Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun updateUI(user: User) {
        profileTextView.text = user.email.ifEmpty { "Profile Information" }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
