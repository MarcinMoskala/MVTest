package com.mvtest.marcinmoskala.mvtest

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import rx.Subscription
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

class LoginPresenter(val view: LoginView) {

    val loginRepository by LoginRepository.lazyGet()
    var subscriptions: List<Subscription> = emptyList()

    fun onDestroy() {
        subscriptions.forEach { it.unsubscribe() }
    }

    fun attemptLogin() {
        val email = view.getEmail()
        val password = view.getPassword()

        val passwordErrorId = getPasswordErrorId(password)
        view.setPasswordError(passwordErrorId)

        val emailErrorId = getEmailErrorId(email)
        view.setEmailError(emailErrorId)

        when {
            emailErrorId != null -> view.requestEmailFocus()
            passwordErrorId != null -> view.requestPasswordFocus()
            else -> sendLoginRequest(email, password)
        }
    }

    private fun sendLoginRequest(email: String, password: String) {
        subscriptions += loginRepository.attemptLogin(email, password)
                .applySchedulers()
                .smartSubscribe(
                        onStart = { view.showProgress(true) },
                        onSuccess = { (token) -> view.informAboutLoginSuccess(token) },
                        onError = { view.informAboutError(it) },
                        onFinish = { view.showProgress(false) }
                )
    }

    private fun getEmailErrorId(email: String) = when {
        email.isEmpty() -> R.string.error_field_required
        emailInvalid(email) -> R.string.error_invalid_email
        else -> null
    }

    private fun getPasswordErrorId(password: String) = when {
        password.isEmpty() -> R.string.error_field_required
        passwordInvalid(password) -> R.string.error_invalid_password
        else -> null
    }

    private fun emailInvalid(email: String): Boolean = !email.contains("@")

    private fun passwordInvalid(password: String): Boolean = password.length <= 4
}