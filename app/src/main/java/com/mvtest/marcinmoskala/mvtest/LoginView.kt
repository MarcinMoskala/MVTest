package com.mvtest.marcinmoskala.mvtest

interface LoginView {
    fun informAboutLoginSuccess(token: String)
    fun informAboutError(error: Throwable)
}