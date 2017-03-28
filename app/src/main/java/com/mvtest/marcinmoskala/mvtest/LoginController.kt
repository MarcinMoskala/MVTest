package com.mvtest.marcinmoskala.mvtest

import rx.Subscription

    class LoginController(val view: LoginActivity, val emailFieldView: FormFieldView, val passwordFieldView: FormFieldView) {

        val loginRepository by LoginRepository.lazyGet()
        var subscriptions: List<Subscription> = emptyList()

        fun onDestroy() {
            subscriptions.forEach { it.unsubscribe() }
        }

        fun attemptLogin() {
            val emailIsValid = emailFieldView.validateValue()
            val passwordIsValid = passwordFieldView.validateValue()
            if(emailIsValid && passwordIsValid) sendLoginRequest()
        }

        private fun sendLoginRequest() {
            val email = emailFieldView.value()
            val password = passwordFieldView.value()
            subscriptions += loginRepository.attemptLogin(email, password)
                    .applySchedulers()
                    .smartSubscribe(
                            onStart = { view.showProgress(true) },
                            onSuccess = { (token) -> view.toast("Login succeed. Token: $token") },
                            onError = { view.toast("Error occurred") },
                            onFinish = { view.showProgress(false) }
                    )
        }
    }


