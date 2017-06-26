package com.mvtest.marcinmoskala.mvtest

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mvtest.marcinmoskala.mvtest.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

    class LoginActivity : AppCompatActivity(), LoginView {

        val presenter by lazy { LoginViewModel(this) }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            View.VISIBLE
            val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
            binding.viewmodel = LoginViewModel(this)
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

        override fun displayToast(text: String) {
            toast(text)
        }
    }
