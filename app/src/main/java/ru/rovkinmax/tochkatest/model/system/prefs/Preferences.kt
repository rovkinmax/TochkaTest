package ru.rovkinmax.tochkatest.model.system.prefs

import android.content.Context
import ru.rovkinmax.socialnetwork.SocialType
import javax.inject.Inject


class Preferences @Inject constructor(context: Context) {
    private val appContext: Context = context.applicationContext
    var socialType by appContext.bindEnumPreference(default = SocialType.NONE)
}