package ru.rovkinmax.tochkatest.feature.global.data

import ru.rovkinmax.tochkatest.feature.global.domain.CommonException


fun ApiError.toCommonException(): CommonException = CommonException(CommonException.CODE_API, message)