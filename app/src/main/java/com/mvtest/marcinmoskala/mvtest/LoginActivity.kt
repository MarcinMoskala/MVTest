package com.mvtest.marcinmoskala.mvtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val presenter by lazy {
        LoginPresenter(
                RxView.clicks(loginButton),
                RxView.visibility(progressView),
                RxView.visibility(loginFormView),
                emailView.toSubject(),
                passwordView.toSubject(),
                emailView.errorRes(),
                passwordView.errorRes(),
                emailView.requestFocusConsumer(),
                passwordView.requestFocusConsumer(),
                Consumer { toast(it) }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        presenter.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}

