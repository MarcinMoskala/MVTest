package com.mvtest.marcinmoskala.mvtest

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class LoginPresenter(
        val buttonClicks: Observable<Any>,
        val progressVisible: Consumer<in Boolean>,
        val formVisible: Consumer<in Boolean>,
        val emailObservable: BehaviorSubject<String>,
        val passwordObservable: BehaviorSubject<String>,
        val emailErrorIdConsumer: Consumer<in Int?>,
        val passwordErrorIdConsumer: Consumer<in Int?>,
        val emailRequestFocusConsumer: Consumer<Any>,
        val passwordRequestFocusConsumer: Consumer<Any>,
        val showTextConsumer: Consumer<String>
) {

    val validateLoginFieldsUseCase by lazy { ValidateLoginFieldsUseCase() }
    val loginUseCase by lazy { LoginUseCase() }
    var subscriptions: List<Disposable> = emptyList()

    fun onCreate() {
        setUpButtonListener()
    }

    fun onDestroy() {
        subscriptions.forEach { it.dispose() }
    }

    private fun setUpButtonListener() {
        subscriptions += buttonClicks
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    setLoading(true)
                    makeValidateLoginObservable(emailObservable.value, passwordObservable.value)
                }
                .subscribeOn(Schedulers.io())
                .flatMap { (email, pass) -> makeAttemptLoginObservable(email, pass) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (token) ->
                    showTextConsumer.accept("Login succeed. Token: $token")
                    setLoading(false)
                }, { error ->
                    if(error !is LoginFieldError) showTextConsumer.accept("Error: " + error.message)
                    error.printStackTrace()
                    setLoading(false)
                    setUpButtonListener()
                })
    }

    private fun makeValidateLoginObservable(email: CharSequence, pass: CharSequence) = validateLoginFieldsUseCase
            .validateLogin(email, pass, emailErrorIdConsumer, passwordErrorIdConsumer, emailRequestFocusConsumer, passwordRequestFocusConsumer)

    private fun makeAttemptLoginObservable(email: String, pass: String) = loginUseCase
            .sendLoginRequest(email, pass)

    private fun setLoading(loading: Boolean) {
        progressVisible.accept(loading)
        formVisible.accept(!loading)
    }
}