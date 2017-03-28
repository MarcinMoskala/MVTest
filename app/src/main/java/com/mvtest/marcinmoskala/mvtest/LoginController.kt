package com.mvtest.marcinmoskala.mvtest

import android.widget.EditText
import rx.Subscription

class LoginController(val view: LoginActivity, val emailView: EditText, val passwordView: EditText) {

    val loginRepository by LoginRepository.lazyGet()
    var subscriptions: List<Subscription> = emptyList()

    fun onDestroy() {
        subscriptions.forEach { it.unsubscribe() }
    }

    fun attemptLogin() {
        val email = emailView.text.toString()
        val password = passwordView.text.toString()

        val passwordErrorId = getPasswordErrorId(password)
        passwordView.setErrorId(passwordErrorId)

        val emailErrorId = getEmailErrorId(email)
        emailView.setErrorId(emailErrorId)

        when {
            emailErrorId != null -> emailView.requestFocus()
            passwordErrorId != null -> passwordView.requestFocus()
            else -> sendLoginRequest(email, password)
        }
    }

    private fun sendLoginRequest(email: String, password: String) {
        subscriptions += loginRepository.attemptLogin(email, password)
                .applySchedulers()
                .smartSubscribe(
                        onStart = { view.showProgress(true) },
                        onSuccess = { (token) -> view.toast("Login succeed. Token: $token") },
                        onError = { view.toast("Error occurred") },
                        onFinish = { view.showProgress(false) }
                )
    }

    private fun getEmailErrorId(email: String) = when {
        email.isEmpty() -> R.string.error_field_required
        emailInvalid(email) -> R.string.error_invalid_email
        else -> null
    }

    private fun getPasswordErrorId(email: String) = when {
        email.isEmpty() -> R.string.error_field_required
        passwordInvalid(email) -> R.string.error_invalid_password
        else -> null
    }

    private fun emailInvalid(email: String): Boolean = !email.contains("@")

    private fun passwordInvalid(password: String): Boolean = password.length <= 4
}


