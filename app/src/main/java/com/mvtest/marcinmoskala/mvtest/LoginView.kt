package com.mvtest.marcinmoskala.mvtest

interface LoginView {
    fun showProgress(show: Boolean)
    fun getEmail(): String
    fun getPassword(): String
    fun setEmailError(id: Int?)
    fun setPasswordError(id: Int?)
    fun requestEmailFocus()
    fun requestPasswordFocus()
    fun informAboutLoginSuccess(token: String)
    fun informAboutError(error: Throwable)
}