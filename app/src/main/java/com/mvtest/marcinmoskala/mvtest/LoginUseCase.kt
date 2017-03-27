package com.mvtest.marcinmoskala.mvtest

class LoginUseCase {

    val loginRepository by LoginRepository.lazyGet()

    fun sendLoginRequest(email: String, password: String) = loginRepository
            .attemptLogin(email, password)
}