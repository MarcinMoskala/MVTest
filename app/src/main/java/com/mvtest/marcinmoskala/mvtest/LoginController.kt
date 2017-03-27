package com.mvtest.marcinmoskala.mvtest

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import rx.Subscription
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

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
        !isEmailValid(email) -> R.string.error_invalid_email
        else -> null
    }

    private fun getPasswordErrorId(email: String) = when {
        email.isEmpty() || isPasswordValid(email) -> R.string.error_invalid_password
        else -> null
    }

    private fun isEmailValid(email: String): Boolean {
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 4
    }
}