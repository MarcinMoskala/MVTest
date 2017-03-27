package com.mvtest.marcinmoskala.mvtest

import rx.Subscription

class LoginController(val view: LoginActivity, val emailController: EmailController, val passwordController: PasswordController) {

    val loginRepository by LoginRepository.lazyGet()
    var subscriptions: List<Subscription> = emptyList()

    fun onDestroy() {
        subscriptions.forEach { it.unsubscribe() }
    }

    fun attemptLogin() {
        val emailIsValid = emailController.validate()
        val passwordIsValid = passwordController.validate()
        if(emailIsValid && passwordIsValid) sendLoginRequest()
    }

    private fun sendLoginRequest() {
        val email = emailController.value()
        val password = passwordController.value()
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


