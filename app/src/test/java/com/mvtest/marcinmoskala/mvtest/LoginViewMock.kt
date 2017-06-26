package com.mvtest.marcinmoskala.mvtest

class LoginViewMock(
        override var email: String = "",
        override var password: String = "",
        override var progressVisible: Boolean = false,
        override var emailErrorId: Int? = null,
        override var passwordErrorId: Int? = null,
        override val emailRequestFocus: () -> Unit = {},
        override val passwordRequestFocus: () -> Unit = {},
        val onLoginError: (error: Throwable) -> Unit = {},
        val onLoginSuccess: (token: String) -> Unit = {}
) : LoginView {

    override fun informAboutLoginSuccess(token: String) {
        onLoginSuccess(token)
    }

    override fun informAboutError(error: Throwable) {
        onLoginError(error)
    }

}
