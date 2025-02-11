package com.example.edufun.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edufun.UserRepository
import com.example.edufun.model.User
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun saveSession(user: User) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}