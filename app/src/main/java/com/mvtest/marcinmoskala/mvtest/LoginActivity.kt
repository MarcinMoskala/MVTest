package com.mvtest.marcinmoskala.mvtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.marcinmoskala.kotlinandroidviewbindings.bindToErrorId
import com.marcinmoskala.kotlinandroidviewbindings.bindToLoading
import com.marcinmoskala.kotlinandroidviewbindings.bindToRequestFocus
import com.marcinmoskala.kotlinandroidviewbindings.bindToText
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    override var progressVisible by bindToLoading(R.id.progressView, R.id.loginFormView)
    override var email by bindToText(R.id.emailView)
    override var password by bindToText(R.id.passwordView)
    override var emailErrorId by bindToErrorId(R.id.emailView)
    override var passwordErrorId by bindToErrorId(R.id.passwordView)
    override val emailRequestFocus by bindToRequestFocus(R.id.emailView)
    override val passwordRequestFocus by bindToRequestFocus(R.id.passwordView)

    val presenter by lazy { LoginPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginButton.setOnClickListener { presenter.attemptLogin() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun informAboutLoginSuccess(token: String) {
        toast("Login succeed. Token: $token")
    }

    override fun informAboutError(error: Throwable) {
        toast("Error: " + error.message)
    }
}

