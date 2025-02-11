package com.example.edufun.response

import com.example.edufun.model.User

data class RegisterResponse(
    val success: Boolean,        // Menunjukkan apakah pendaftaran berhasil
    val message: String,        // Pesan yang menjelaskan hasil pendaftaran
    val user: User?             // Data pengguna yang terdaftar, bisa null jika gagal
)

