package com.mvtest.marcinmoskala.mvtest

import android.databinding.BaseObservable
import android.databinding.ObservableField
import rx.Subscription

class LoginViewModel(val view: LoginView) {

    val loginUseCase by lazy { LoginUseCase() }
    val validateLoginFieldsUseCase by lazy { ValidateLoginFieldsUseCase() }
    var subscriptions: List<Subscription> = emptyList()

    var progressVisible = ObservableField(false)
    var email = ObservableField("")
    var password = ObservableField("")
    var visiblePasswordErrorId = ObservableField<Int?>(null)
    var visibleEmailErrorId = ObservableField<Int?>(null)

    fun onDestroy() {
        subscriptions.forEach { it.unsubscribe() }
    }

    fun attemptLogin() {
        val emailVal = email.get()
        val passwordVal = password.get()
        validateFieldsAndSendLoginRequest(emailVal, passwordVal)
    }

    private fun validateFieldsAndSendLoginRequest(emailVal: String, passwordVal: String) {
        subscriptions += validateLoginFieldsUseCase.validateLogin(emailVal, passwordVal)
                .smartSubscribe { (emailErrorId, passwordErrorId) ->
                    visiblePasswordErrorId.set(passwordErrorId)
                    visibleEmailErrorId.set(emailErrorId)
                    when {
                        emailErrorId != null -> view.requestEmailFocus()
                        passwordErrorId != null -> view.requestPasswordFocus()
                        else -> sendLoginRequest(emailVal, passwordVal)
                    }
                }
    }

    private fun sendLoginRequest(email: String, password: String) {
        loginUseCase.sendLoginRequest(email, password)
                .applySchedulers()
                .smartSubscribe(
                        onStart = { progressVisible.set(true) },
                        onSuccess = { (token) -> this.informAboutLoginSuccess(token) },
                        onError = this::informAboutError,
                        onFinish = { progressVisible.set(false) }
                )
    }

    private fun informAboutLoginSuccess(token: String) {
        view.displayToast("Login succeed. Token: $token")
    }

    private fun informAboutError(error: Throwable) {
        view.displayToast("Error: " + error.message)
    }
}