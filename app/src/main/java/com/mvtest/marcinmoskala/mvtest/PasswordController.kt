package com.mvtest.marcinmoskala.mvtest

import android.widget.EditText

class PasswordController(val passwordView: EditText) {

    fun value() = passwordView.text.toString()

    fun validate(): Boolean {
        val password = value()
        val passwordErrorId = getPasswordErrorId(password)
        passwordView.setErrorId(passwordErrorId)
        if(passwordErrorId != null) passwordView.requestFocus()
        return passwordErrorId == null
    }

    private fun getPasswordErrorId(password: String) = when {
        password.isEmpty() || isPasswordValid(password) -> R.string.error_invalid_password
        else -> null
    }

    private fun isPasswordValid(password: String): Boolean = password.length > 4
}


