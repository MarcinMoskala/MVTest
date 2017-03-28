package com.mvtest.marcinmoskala.mvtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    val loginRepository by LoginRepository.lazyGet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailSignInButton.setOnClickListener { attemptLogin() }
    }

    private fun attemptLogin() {
        emailView.error = null
        passwordView.error = null

        val email = emailView.text.toString()
        val password = passwordView.text.toString()

        var cancel = false
        var focusView: View? = null

        if (!TextUtils.isEmpty(password) && !passwordInvalid(password)) {
            passwordView.error = getString(R.string.error_invalid_password)
            focusView = passwordView
            cancel = true
        }

        if (TextUtils.isEmpty(email)) {
            emailView.error = getString(R.string.error_field_required)
            focusView = emailView
            cancel = true
        } else if (!emailInvalid(email)) {
            emailView.error = getString(R.string.error_invalid_email)
            focusView = emailView
            cancel = true
        }

        if (cancel) {
            focusView!!.requestFocus()
        } else {
            loginRepository.attemptLogin(email, password)
                    .applySchedulers()
                    .smartSubscribe(
                            onStart = { showProgress(true) },
                            onSuccess = { (token) -> toast("Login succeed. Token: $token") },
                            onError = { toast("Error occurred") },
                            onFinish = { showProgress(false) }
                    )
        }
    }

    private fun emailInvalid(email: String): Boolean = !email.contains("@")

    private fun passwordInvalid(password: String): Boolean = password.length <= 4

    private fun showProgress(show: Boolean) {
        progressView.visibility = if (show) View.VISIBLE else View.GONE
        loginFormView.visibility = if (show) View.GONE else View.VISIBLE
    }
}

