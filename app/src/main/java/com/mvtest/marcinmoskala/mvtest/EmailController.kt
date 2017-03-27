package com.mvtest.marcinmoskala.mvtest

import android.widget.EditText

class EmailController(val emailView: EditText) {

    fun value() = emailView.text.toString()

    fun validate(): Boolean {
        val email = value()
        val emailErrorId = getEmailErrorId(email)
        emailView.setErrorId(emailErrorId)
        if(emailErrorId != null) emailView.requestFocus()
        return emailErrorId == null
    }

    private fun getEmailErrorId(email: String) = when {
        email.isEmpty() -> R.string.error_field_required
        !isEmailValid(email) -> R.string.error_invalid_email
        else -> null
    }

    private fun isEmailValid(email: String): Boolean = email.contains("@")
}

