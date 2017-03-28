package com.mvtest.marcinmoskala.mvtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity(), LoginView {

    private val emailView: AutoCompleteTextView by bindView(R.id.email)
    private val passwordView: EditText by bindView(R.id.password)
    private val progressView: View by bindView(R.id.login_progress)
    private val loginFormView: View by bindView(R.id.login_form)
    private val emailSignInButton: Button by bindView(R.id.email_sign_in_button)

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
        toast("Error: "+error.message)
    }

    override fun showProgress(show: Boolean) {
        progressView.visibility = if (show) View.VISIBLE else View.GONE
        loginFormView.visibility = if (show) View.GONE else View.VISIBLE
    }
}

