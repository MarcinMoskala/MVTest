package com.mvtest.marcinmoskala.mvtest

import io.reactivex.Observable
import io.reactivex.functions.Consumer

class ValidateLoginFieldsUseCase {

    fun validateLogin(
            email: CharSequence,
            password: CharSequence,
            emailErrorIdConsumer: Consumer<in Int?>,
            passwordErrorIdConsumer: Consumer<in Int?>,
            emailRequestFocusConsumer: Consumer<Any>,
            passwordRequestFocusConsumer: Consumer<Any>
    ): Observable<Pair<String, String>> {
        val email = email.toString()
        val password = password.toString()
        val emailErrorId = getEmailErrorId(email)
        val passwordErrorId = getPasswordErrorId(password)
        emailErrorIdConsumer.accept(emailErrorId)
        passwordErrorIdConsumer.accept(passwordErrorId)
        return when {
            emailErrorId != null -> {
                emailRequestFocusConsumer.accept(Any())
                Observable.error(LoginFieldError())
            }
            passwordErrorId != null -> {
                passwordRequestFocusConsumer.accept(Any())
                Observable.error(LoginFieldError())
            }
            else -> Observable.just(email to password)
        }
    }

    private fun getEmailErrorId(email: String) = when {
        email.isEmpty() -> R.string.error_field_required
        emailInvalid(email) -> R.string.error_invalid_email
        else -> null
    }

    private fun getPasswordErrorId(password: String) = when {
        password.isEmpty() -> R.string.error_field_required
        passwordInvalid(password) -> R.string.error_invalid_password
        else -> null
    }

    private fun emailInvalid(email: String): Boolean = !email.contains("@")

    private fun passwordInvalid(password: String): Boolean = password.length <= 4
}

class LoginFieldError: Throwable()
