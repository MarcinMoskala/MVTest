package com.mvtest.marcinmoskala.mvtest

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mvtest.marcinmoskala.mvtest.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    private val viewModel by lazy { LoginViewModel(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
                .also { it.viewmodel = viewModel }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
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
