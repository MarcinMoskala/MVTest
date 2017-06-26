package com.mvtest.marcinmoskala.mvtest

import org.junit.Assert
import org.junit.Test
import rx.Observable
import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.schedulers.Schedulers


class LoginPresenterTest {

    fun getMockView(email: String, password: String) = LoginViewMock(email, password)

    init {
        val rxAndroidPlugins = RxAndroidPlugins.getInstance()
        rxAndroidPlugins.reset()
        rxAndroidPlugins.registerSchedulersHook(object : RxAndroidSchedulersHook() {
            override fun getMainThreadScheduler(): Scheduler = Schedulers.immediate()
        })
    }

    @Test
    fun checkBothLoginFieldsEmpty() {
        val mockedView = getMockView("", "")
        val presenter = LoginPresenter(mockedView)
        presenter.attemptLogin()
        checkVaildity(mockedView,
                expectedEmailError = R.string.error_field_required,
                expectedPasswordError = R.string.error_field_required
        )
    }

    @Test
    fun checkBothLoginFieldsErrored() {
        val mockedView = getMockView("MarcinMoskala", "KOKO")
        val presenter = LoginPresenter(mockedView)
        presenter.attemptLogin()
        checkVaildity(mockedView,
                expectedEmailError = R.string.error_invalid_email,
                expectedPasswordError = R.string.error_invalid_password
        )
    }

    @Test
    fun checkEmailFieldError() {
        val mockedView = getMockView("MarcinMoskala", "KOKOKOKO")
        val presenter = LoginPresenter(mockedView)
        presenter.attemptLogin()
        checkVaildity(mockedView, expectedEmailError = R.string.error_invalid_email)
    }

    @Test
    fun checkPasswordFieldErrored() {
        val mockedView = getMockView("marcinmoskala@gmail.com", "KOKO")
        val presenter = LoginPresenter(mockedView)
        presenter.attemptLogin()
        checkVaildity(mockedView, expectedPasswordError = R.string.error_invalid_password)
    }

    @Test
    fun networkErrorShowedOnly() {
        LoginRepository.override = object : LoginRepository {
            override fun attemptLogin(email: String, pass: String): Observable<LoginResponse> {
                return Observable.error(Error("Network connection error"))
            }
        }
        val mockedView = getMockView("marcinmoskala@gmail.com", "KOKOKOKO")
        val presenter = LoginPresenter(mockedView)
        presenter.attemptLogin()
        waitUntilAllUnsubscribed(presenter)
        LoginRepository.override = null
        checkVaildity(mockedView, expectedNetworkError = true)
    }

    @Test
    fun correctLoginInformShowedOnly() {
        LoginRepository.override = object : LoginRepository {
            override fun attemptLogin(email: String, pass: String): Observable<LoginResponse> {
                return Observable.just(LoginResponse("SomeToken"))
            }
        }
        val mockedView = getMockView("marcinmoskala@gmail.com", "KOKOKOKO")
        val presenter = LoginPresenter(mockedView)
        presenter.attemptLogin()
        waitUntilAllUnsubscribed(presenter)
        LoginRepository.override = null
        checkVaildity(mockedView, expectedLoginCorrect = true)
    }

    private fun checkVaildity(
            view: LoginViewMock,
            expectedEmailError: Int? = null,
            expectedPasswordError: Int? = null,
            expectedLoginCorrect: Boolean = false,
            expectedNetworkError: Boolean = false
    ) {
        Assert.assertEquals(expectedEmailError, view.mockedEmailError)
        Assert.assertEquals(expectedPasswordError, view.mockedPasswordError)
        checkNull(expectedLoginCorrect, view.isSuccessInformation)
        checkNull(expectedNetworkError, view.isNetworkErrorInformation)
    }

    private fun checkNull(shouldBeNotnull: Boolean, value: Any?) {
        if (shouldBeNotnull) {
            Assert.assertNotNull(value)
        } else {
            Assert.assertNull(value)
        }
    }

    private fun waitUntilAllUnsubscribed(presenter: LoginPresenter) {
        while (!presenter.subscriptions.all { it.isUnsubscribed })
            Thread.sleep(20)
    }
}