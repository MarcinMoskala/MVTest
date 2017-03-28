package com.mvtest.marcinmoskala.mvtest

import rx.Observable

class ValidateLoginFieldsUseCase {

    fun validateLogin(email: String, password: String) = Observable.just(getLoginErrors(email, password))

    private fun getLoginErrors(email: String, password: String) = LoginErrors(getEmailErrorId(email), getPasswordErrorId(password))

    private fun getEmailErrorId(email: String) = when {
        email.isEmpty() -> R.string.error_field_required
        !isEmailValid(email) -> R.string.error_invalid_email
        else -> null
    }

    private fun getPasswordErrorId(password: String) = when {
        password.isEmpty() -> R.string.error_field_required
        isPasswordValid(password) -> R.string.error_invalid_password
        else -> null
    }

    private fun isEmailValid(email: String): Boolean = email.contains("@")

    private fun isPasswordValid(password: String): Boolean = password.length > 4

    data class LoginErrors(val emailErrorId: Int? = null, val passwordErrorId: Int? = null) {
        val correct get() = emailErrorId == null && passwordErrorId == null
    }
}