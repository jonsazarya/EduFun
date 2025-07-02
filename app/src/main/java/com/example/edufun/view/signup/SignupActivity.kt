package com.example.edufun.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.edufun.database.EdufunDatabaseHelper
import com.example.edufun.databinding.ActivitySignupBinding
import com.example.edufun.view.welcome.WelcomeActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var edufunDatabaseHelper: EdufunDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        edufunDatabaseHelper = EdufunDatabaseHelper(this)

        setupView()
        playAnimation()

        binding.signupButton.setOnClickListener {
            val signupName = binding.nameEditText.text.toString()
            val signupEmail = binding.emailEditText.text.toString()
            val signupPassword = binding.passwordEditText.text.toString()
            if (signupName.isEmpty()){
                binding.nameEditText.error = "Masukkan nama anda"
            } else if (signupEmail.isEmpty()) {
                binding.emailEditText.error = "Masukkan email anda"
            } else if (!signupEmail.contains("@")) {
                binding.emailEditText.error = "Email harus mengandung '@"
            } else if (signupPassword.isEmpty()) {
                binding.passwordEditText.error = "Masukkan password anda"
            } else if (signupPassword.length < 6) {
                binding.passwordEditText.error = "Password minimal 6 karakter"
            } else if (edufunDatabaseHelper.isEmailExist(signupEmail)) {
                Toast.makeText(this, "Email sudah terdaftar", Toast.LENGTH_SHORT).show()
            } else {
                signupDatabase(signupName, signupEmail, signupPassword)
            }
        }
    }

    private fun signupDatabase(name: String, email: String, password: String){
        val insertRowId = edufunDatabaseHelper.insertUser(name, email, password)
        if (insertRowId != -1L){
            Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Registrasi gagal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }
}