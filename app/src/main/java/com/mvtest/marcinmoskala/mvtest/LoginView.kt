package com.mvtest.marcinmoskala.mvtest

interface LoginView {
    fun requestEmailFocus()
    fun requestPasswordFocus()
    fun displayToast(text: String)
}