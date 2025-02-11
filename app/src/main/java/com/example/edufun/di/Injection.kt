package com.example.edufun.di

import android.content.Context
import com.example.edufun.UserRepository
import com.example.edufun.pref.UserPreference
import com.example.edufun.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}