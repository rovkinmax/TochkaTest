package ru.rovkinmax.tochkatest.model.ui.extentions

import android.view.View

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}