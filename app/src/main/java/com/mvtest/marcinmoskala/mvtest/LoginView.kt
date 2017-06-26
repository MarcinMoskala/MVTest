package com.mvtest.marcinmoskala.mvtest

interface LoginView {
    var progressVisible: Boolean
    var email: String
    var password: String
    var emailErrorId: Int?
    var passwordErrorId: Int?
    val emailRequestFocus: () -> Unit
    val passwordRequestFocus: () -> Unit
    fun informAboutLoginSuccess(token: String)
    fun informAboutError(error: Throwable)
}