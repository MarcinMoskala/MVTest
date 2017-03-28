package com.mvtest.marcinmoskala.mvtest

interface FormFieldController {
    fun getErrorId(text: String): Int?
}

class EmailController : FormFieldController {

    override fun getErrorId(text: String) = when {
        text.isEmpty() -> R.string.error_field_required
        emailInvalid(text) -> R.string.error_invalid_email
        else -> null
    }

    private fun emailInvalid(email: String): Boolean = !email.contains("@")
}

class PasswordController : FormFieldController {

    override fun getErrorId(text: String) = when {
        text.isEmpty() -> R.string.error_field_required
        passwordInvalid(text) -> R.string.error_invalid_password
        else -> null
    }

    private fun passwordInvalid(password: String): Boolean = password.length <= 4
}

