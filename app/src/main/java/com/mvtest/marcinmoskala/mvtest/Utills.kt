package com.mvtest.marcinmoskala.mvtest

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

inline fun <reified T> AppCompatActivity.bindView(viewId: Int) = lazy { findViewById(viewId) as T }

fun <T> Observable<T>.applySchedulers(): Observable<T> =
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.smartSubscribe(
        onStart: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onFinish: (() -> Unit)? = null,
        onSuccess: (T) -> Unit = {}): Disposable =
        addStartFinishActions(onStart, onFinish)
                .subscribe(onSuccess, { onError?.invoke(it) })

fun <T> Observable<T>.addStartFinishActions(onStart: (() -> Unit)? = null, onFinish: (() -> Unit)? = null): Observable<T> {
    onStart?.invoke()
    return doOnComplete({ onFinish?.invoke() }).doOnError { onFinish?.invoke() }
}

fun Context.toast(text: String, length: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, text, length).show()
}

fun View.requestFocusConsumer(): Consumer<Any> = Consumer { requestFocus() }

// TODO this is not secure
fun EditText.toSubject(): BehaviorSubject<String> = BehaviorSubject.create<String>().also {
    it.onNext(this.text.toString())
    var lastSetText: String? = null
    it.subscribe { if (it != lastSetText) this.setText(it) }
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s?.toString() ?: return
            if (text != it.value) {
                lastSetText = text
                it.onNext(text)
            }
        }

    })
}

fun TextView.errorRes(): Consumer<Int?> {
    return Consumer { id -> error = if (id == null) null else context.resources.getText(id) }
}