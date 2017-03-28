package com.mvtest.marcinmoskala.mvtest

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

class FormFieldView(context: Context, attrs: AttributeSet? = null) : EditText(context, attrs) {

    fun value() = text.toString()
    lateinit var controller: FormFieldController

    fun validateValue(): Boolean {
        val value = value()
        val errorId = controller.getErrorId(value)
        setErrorId(errorId)
        if (errorId != null) requestFocus()
        return errorId == null
    }
}