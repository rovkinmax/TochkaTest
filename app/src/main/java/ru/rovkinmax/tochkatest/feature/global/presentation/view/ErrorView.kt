package ru.rovkinmax.tochkatest.feature.global.presentation.view

interface ErrorView {
    fun showErrorMessage(message: String, needCallback: Boolean = false)
    fun hideErrorMessage() {}
}