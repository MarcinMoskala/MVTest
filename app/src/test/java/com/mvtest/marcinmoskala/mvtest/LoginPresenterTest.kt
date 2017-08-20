package com.mvtest.marcinmoskala.mvtest

import org.junit.Assert.assertEquals
import org.junit.Test
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers

class LoginPresenterTest {

    init {
        val rxAndroidPlugins = RxAndroidPlugins.getInstance()
        rxAndroidPlugins.reset()
        rxAndroidPlugins.registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler = Schedulers.immediate()
        })
    }

    @Test
    fun checkBothLoginFieldsEmpty() {
        // Given
        val view = LoginViewMock("", "")
        val presenter = LoginPresenter(view, RxView.visibility(progressView), RxView.visibility(loginFormView), RxTextView.textChanges(emailView), RxTextView.textChanges(passwordView), RxTextView.errorRes(emailView), RxTextView.errorRes(passwordView), RxView.focusChanges(emailView), RxView.focusChanges(passwordView))
        // When
        presenter.attemptLogin()
        // Then
        assertEquals(R.string.error_field_required, view.emailErrorId)
        assertEquals(R.string.error_field_required, view.passwordErrorId)
    }

    @Test
    fun checkBothLoginFieldsErrored() {
        // Given
        val view = LoginViewMock("MarcinMoskala", "KOKO")
        val presenter = LoginPresenter(view, RxView.visibility(progressView), RxView.visibility(loginFormView), RxTextView.textChanges(emailView), RxTextView.textChanges(passwordView), RxTextView.errorRes(emailView), RxTextView.errorRes(passwordView), RxView.focusChanges(emailView), RxView.focusChanges(passwordView))
        // When
        presenter.attemptLogin()
        // Then
        assertEquals(R.string.error_invalid_email, view.emailErrorId)
        assertEquals(R.string.error_invalid_password, view.passwordErrorId)
    }

    @Test
    fun checkEmailFieldError() {
        // Given
        val view = LoginViewMock("MarcinMoskala", "KOKOKOKO")
        val presenter = LoginPresenter(view, RxView.visibility(progressView), RxView.visibility(loginFormView), RxTextView.textChanges(emailView), RxTextView.textChanges(passwordView), RxTextView.errorRes(emailView), RxTextView.errorRes(passwordView), RxView.focusChanges(emailView), RxView.focusChanges(passwordView))
        // When
        presenter.attemptLogin()
        // Then
        assertEquals(R.string.error_invalid_email, view.emailErrorId)
    }

    @Test
    fun checkPasswordFieldErrored() {
        // Given
        val view = LoginViewMock("marcinmoskala@gmail.com", "KOKO")
        val presenter = LoginPresenter(view, RxView.visibility(progressView), RxView.visibility(loginFormView), RxTextView.textChanges(emailView), RxTextView.textChanges(passwordView), RxTextView.errorRes(emailView), RxTextView.errorRes(passwordView), RxView.focusChanges(emailView), RxView.focusChanges(passwordView))
        // When
        presenter.attemptLogin()
        // Then
        assertEquals(R.string.error_invalid_password, view.passwordErrorId)
    }

    @Test
    fun networkErrorShowedOnly() {
        // Given
        val errorMessage = "Network connection error"
        LoginRepository.override = object : LoginRepository {
            override fun attemptLogin(email: String, pass: String): Observable<LoginResponse> {
                return Observable.error(Error(errorMessage))
            }
        }
        val view = LoginViewMock("marcinmoskala@gmail.com", "KOKOKOKO",
                onLoginError = { /* Then */ assertEquals(errorMessage, it.message) },
                onLoginSuccess = { returnedToken -> assert(false) }
        )
        val presenter = LoginPresenter(view, RxView.visibility(progressView), RxView.visibility(loginFormView), RxTextView.textChanges(emailView), RxTextView.textChanges(passwordView), RxTextView.errorRes(emailView), RxTextView.errorRes(passwordView), RxView.focusChanges(emailView), RxView.focusChanges(passwordView))
        // When
        presenter.attemptLogin()
        waitUntilAllUnsubscribed(presenter)
    }

    @Test
    fun correctLoginInformShowedOnly() {
        // Given
        val token = "SomeToken"
        LoginRepository.override = object : LoginRepository {
            override fun attemptLogin(email: String, pass: String): Observable<LoginResponse> {
                return Observable.just(LoginResponse(token))
            }
        }
        val mockedView = LoginViewMock("marcinmoskala@gmail.com", "KOKOKOKO",
                onLoginError = { assert(false) },
                onLoginSuccess = { returnedToken -> /* Then */ assertEquals(token, returnedToken) }
        )
        val presenter = LoginPresenter(mockedView, RxView.visibility(progressView), RxView.visibility(loginFormView), RxTextView.textChanges(emailView), RxTextView.textChanges(passwordView), RxTextView.errorRes(emailView), RxTextView.errorRes(passwordView), RxView.focusChanges(emailView), RxView.focusChanges(passwordView))
        presenter.attemptLogin()
        // When
        waitUntilAllUnsubscribed(presenter)
        LoginRepository.override = null
    }

    private fun waitUntilAllUnsubscribed(presenter: LoginPresenter) {
        while (!presenter.subscriptions.all { it.isUnsubscribed })
            Thread.sleep(20)
    }
}