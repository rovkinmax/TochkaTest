package ru.rovkinmax.tochkatest.feature.global.presentation

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import io.reactivex.functions.Consumer
import retrofit2.HttpException
import ru.rovkinmax.tochkatest.R
import ru.rovkinmax.tochkatest.feature.global.data.ApiError
import ru.rovkinmax.tochkatest.feature.global.data.toCommonException
import ru.rovkinmax.tochkatest.feature.global.domain.CommonException
import ru.rovkinmax.tochkatest.feature.global.presentation.view.ErrorView
import ru.rovkinmax.tochkatest.model.ResourceProvider
import ru.rovkinmax.tochkatest.model.system.flow.GlobalRouter
import ru.rovkinmax.tochkatest.model.system.network.gson.fromJson
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


interface ErrorHandler {
    fun proceed(errorView: ErrorView? = null, action: ((CommonException) -> Unit)? = null): Consumer<Throwable>
    fun proceedInvoke(errorView: ErrorView? = null, e: Throwable, action: ((CommonException) -> Unit)? = null)
    fun commonExceptionToString(exception: CommonException): String
}

open class DefaultErrorHandler @Inject constructor(private val resourceProvider: ResourceProvider,
                                                   private val gson: Gson) : ErrorHandler {

    override fun proceedInvoke(errorView: ErrorView?, e: Throwable, action: ((CommonException) -> Unit)?) {
        proceed(errorView, action).accept(e)
    }

    override fun proceed(errorView: ErrorView?, action: ((CommonException) -> Unit)?): Consumer<Throwable> {
        return Consumer { e ->
            proceedException(errorView, e, action)
        }
    }

    private fun proceedException(errorView: ErrorView?, e: Throwable?, action: ((CommonException) -> Unit)?) {
        Timber.tag(javaClass.simpleName).w(e)
        when (e) {
            is HttpException -> proceedHttpException(errorView, e, action)
            is UnknownHostException,
            is SocketTimeoutException -> proceedNetworkException(errorView, action)
            else -> proceedUnknownException(errorView, action)
        }
    }

    protected open fun proceedHttpException(errorView: ErrorView?, e: HttpException, action: ((CommonException) -> Unit)?) {
        val jsonElement = jsonElementFromException(e)
        val exception = when {
            jsonElement.isJsonObject -> apiErrorBodyToException(jsonElement)
            else -> CommonException(message = resourceProvider.getString(R.string.error_unknown), code = CommonException.CODE_UNKNOWN)
        }

        dispatchExceptionToListeners(exception, errorView, action)
    }

    @Suppress("LiftReturnOrAssignment")
    private fun jsonElementFromException(e: HttpException): JsonElement {
        try {
            val body = e.response().errorBody()?.string() ?: ""
            return gson.fromJson<JsonElement>(body, JsonElement::class.java)
        } catch (e: Exception) {
            return JsonNull.INSTANCE
        }
    }

    protected open fun proceedApiError(apiError: ApiError): CommonException {
        return apiError.toCommonException()
    }

    private fun apiErrorBodyToException(body: JsonElement): CommonException {
        val apiError = gson.fromJson<ApiError>(body)
        return proceedApiError(apiError)
    }

    private fun proceedNetworkException(errorView: ErrorView?, action: ((CommonException) -> Unit)?) {
        val exception = CommonException(CommonException.CODE_NETWORK, resourceProvider.getString(R.string.error_network))
        dispatchExceptionToListeners(exception, errorView, action)
    }

    protected fun proceedUnknownException(errorView: ErrorView?, action: ((CommonException) -> Unit)?) {
        val exception = CommonException(CommonException.CODE_UNKNOWN, resourceProvider.getString(R.string.error_unknown))
        dispatchExceptionToListeners(exception, errorView, action)
    }

    protected fun dispatchExceptionToListeners(exception: CommonException,
                                               errorView: ErrorView?,
                                               action: ((CommonException) -> Unit)?) {
        errorView?.showErrorMessage(commonExceptionToString(exception))
                ?: action?.invoke(exception)
    }

    override fun commonExceptionToString(exception: CommonException): String {
        val unknownMessage = resourceProvider.getString(R.string.error_unknown)
        return exception.message ?: unknownMessage
    }
}

class SessionErrorHandler @Inject constructor(private val flowRouter: GlobalRouter,
                                              resourceProvider: ResourceProvider,
                                              gson: Gson) : DefaultErrorHandler(resourceProvider, gson) {

}