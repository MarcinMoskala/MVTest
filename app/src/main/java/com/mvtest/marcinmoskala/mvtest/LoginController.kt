package com.mvtest.marcinmoskala.mvtest

import android.widget.EditText
import rx.Subscription

class LoginController(val view: LoginActivity, val emailView: EditText, val passwordView: EditText) {

    val loginUseCase = LoginUseCase()
    val loginValidationUseCase = LoginValidationUseCase()
    var subscriptions: List<Subscription> = emptyList()

    fun onDestroy() {
        subscriptions.forEach { it.unsubscribe() }
    }

    fun attemptLogin() {
        val email = emailView.text.toString()
        val password = passwordView.text.toString()
        subscriptions += loginValidationUseCase.validateLogin(email, password)
                .smartSubscribe { (emailErrorId, passwordErrorId) ->
                    emailView.setErrorId(emailErrorId)
                    passwordView.setErrorId(passwordErrorId)
                    when {
                        emailErrorId != null -> emailView.requestFocus()
                        passwordErrorId != null -> passwordView.requestFocus()
                        else -> sendLoginRequest(email, password)
                    }
                }
    }

    private fun sendLoginRequest(email: String, password: String) {
        subscriptions += loginUseCase.sendLoginRequest(email, password)
                .applySchedulers()
                .smartSubscribe(
                        onStart = { view.showProgress(true) },
                        onSuccess = { (token) -> view.toast("Login succeed. Token: $token") },
                        onError = { view.toast("Error occurred") },
                        onFinish = { view.showProgress(false) }
                )
    }
}


