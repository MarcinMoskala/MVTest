package com.mvtest.marcinmoskala.mvtest

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks

import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask

import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

import java.util.ArrayList

import android.Manifest.permission.READ_CONTACTS
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity(), LoginView {

    override var progressVisible by Delegates.observable(false) { _, _, n ->
        progressView.visibility = if (n) View.VISIBLE else View.GONE
        loginFormView.visibility = if (n) View.GONE else View.VISIBLE
    }
    override var email: String by bindToText { emailView }
    override var password: String by bindToText { passwordView }
    override var emailErrorId: Int? by bindToErrorId { emailView }
    override var passwordErrorId: Int? by bindToErrorId { passwordView }

    val presenter by lazy { LoginPresenter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        passwordView.setOnEditorActionListener { _, id, _ ->
            when (id) {
                EditorInfo.IME_NULL -> {
                    presenter.attemptLogin()
                    true
                }
                else -> false
            }
        }
        loginButton.setOnClickListener { presenter.attemptLogin() }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
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
}

