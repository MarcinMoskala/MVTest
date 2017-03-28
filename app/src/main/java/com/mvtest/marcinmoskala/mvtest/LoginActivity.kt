package com.mvtest.marcinmoskala.mvtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    val presenter by lazy { LoginPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailSignInButton.setOnClickListener { presenter.attemptLogin() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun getEmail(): String = emailView.text.toString()

    override fun getPassword(): String = passwordView.text.toString()

    override fun setEmailError(id: Int?) {
        emailView.setErrorId(id)
    }

    override fun setPasswordError(id: Int?) {
        passwordView.setErrorId(id)
    }

    override fun requestEmailFocus() {
        emailView.requestFocus()
    }

    override fun requestPasswordFocus() {
        passwordView.requestFocus()
    }

    override fun informAboutLoginSuccess(token: String) {
        toast("Login succeed. Token: $token")
    }

    override fun informAboutError(error: Throwable) {
        toast("Error: " + error.message)
    }

    override fun showProgress(show: Boolean) {
        progressView.visibility = if (show) View.VISIBLE else View.GONE
        loginFormView.visibility = if (show) View.GONE else View.VISIBLE
    }
}

