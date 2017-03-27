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

class LoginActivity : AppCompatActivity() {

    private val emailView: AutoCompleteTextView by bindView(R.id.email)
    private val passwordView: EditText by bindView(R.id.password)
    private val progressView: View by bindView(R.id.login_progress)
    private val loginFormView: View by bindView(R.id.login_form)
    private val emailSignInButton: Button by bindView(R.id.email_sign_in_button)

    val controller by lazy { LoginController(this, emailView, passwordView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        passwordView.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                controller.attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        emailSignInButton.setOnClickListener { controller.attemptLogin() }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.onDestroy()
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2) fun showProgress(show: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)

            loginFormView.visibility = if (show) View.GONE else View.VISIBLE
            loginFormView.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 0 else 1).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    loginFormView.visibility = if (show) View.GONE else View.VISIBLE
                }
            })

            progressView.visibility = if (show) View.VISIBLE else View.GONE
            progressView.animate().setDuration(shortAnimTime.toLong()).alpha(
                    (if (show) 1 else 0).toFloat()).setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    progressView.visibility = if (show) View.VISIBLE else View.GONE
                }
            })
        } else {
            progressView.visibility = if (show) View.VISIBLE else View.GONE
            loginFormView.visibility = if (show) View.GONE else View.VISIBLE
        }
    }
}

