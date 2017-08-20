package com.mvtest.marcinmoskala.mvtest

import android.app.Application
import android.databinding.ViewDataBinding
import android.support.design.widget.TextInputLayout
import android.databinding.BindingAdapter
import android.widget.EditText


class App: Application() {

    override fun onCreate() {
        super.onCreate()
//        BindingUtils.setDefaultBinder(object : ViewModelBinder() {
//            fun bind(viewDataBinding: ViewDataBinding, viewModel: ViewModel) {
//                viewDataBinding.setVariable(BR.vm, viewModel)
//            }
//        })
    }
}

@BindingAdapter("app:errorId")
fun setErrorMessage(view: EditText, errorId: Int?) {
    view.setErrorId(errorId)
}