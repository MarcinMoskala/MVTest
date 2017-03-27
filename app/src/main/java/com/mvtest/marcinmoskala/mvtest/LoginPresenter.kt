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
        val email = view.email
        val password = view.password

        view.passwordErrorId = getPasswordErrorId(password)
        view.emailErrorId = getEmailErrorId(email)

        when {
            view.emailErrorId != null -> view.requestEmailFocus()
            view.passwordErrorId != null -> view.requestPasswordFocus()
            else -> sendLoginRequest(email, password)
        }
    }

    private fun sendLoginRequest(email: String, password: String) {
        subscriptions += loginRepository.attemptLogin(email, password)
                .applySchedulers()
                .smartSubscribe(
                        onStart = { view.progressVisible = true },
                        onSuccess = { (token) -> view.informAboutLoginSuccess(token) },
                        onError = { view.informAboutError(it) },
                        onFinish = { view.progressVisible = false }
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

    private fun isEmailValid(email: String): Boolean = email.contains("@")

    private fun isPasswordValid(password: String): Boolean = password.length > 4
}