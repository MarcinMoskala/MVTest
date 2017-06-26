package com.mvtest.marcinmoskala.mvtest

class

LoginViewMock(private val email: String, private val password: String) : LoginView {

    var mockedProgressVisible = false
    var mockedEmailError: Int? = null
    var mockedPasswordError: Int? = null
    var focus: PossibleLoginFocus = PossibleLoginFocus.NONE
    var isSuccessInformation: String? = null
    var isNetworkErrorInformation: String? = null

    override fun showProgress(show: Boolean) {
        mockedProgressVisible = show
    }

    override fun getEmail() = email

    override fun getPassword() = password

    override fun setEmailError(id: Int?) {
        mockedEmailError = id
    }

    override fun setPasswordError(id: Int?) {
        mockedPasswordError = id
    }

    override fun requestEmailFocus() {
        focus = PossibleLoginFocus.EMAIL
    }

    override fun requestPasswordFocus() {
        focus = PossibleLoginFocus.PASS
    }

    override fun informAboutLoginSuccess(token: String) {
        isSuccessInformation = token
    }

    override fun informAboutError(error: Throwable) {
        isNetworkErrorInformation = error.message
    }

    enum class PossibleLoginFocus {NONE, EMAIL, PASS }
}