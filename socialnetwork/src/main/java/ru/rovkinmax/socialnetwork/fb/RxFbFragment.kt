package ru.rovkinmax.socialnetwork.fb

import android.content.Intent
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import ru.rovkinmax.socialnetwork.RxSocialFragment
import ru.rovkinmax.socialnetwork.SocialException
import ru.rovkinmax.socialnetwork.SocialLoginResult
import ru.rovkinmax.socialnetwork.SocialType

class RxFbFragment : RxSocialFragment(), FacebookCallback<LoginResult> {
    companion object {
        private const val PERMISSION_PROFILE = "public_profile"
    }

    private val callbackManager = CallbackManager.Factory.create()

    override fun login() {
        LoginManager.getInstance().registerCallback(callbackManager, this)
        LoginManager.getInstance().logInWithReadPermissions(this, arrayListOf(PERMISSION_PROFILE))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccess(result: LoginResult?) {
        unregisterCallback()
        if (result != null) {
            subject.onNext(SocialLoginResult(SocialType.FB))
        } else subject.onError(SocialException("Problem with FB login"))
    }

    override fun onCancel() {
        unregisterCallback()
        subject.onError(SocialException("User cancel login in FB"))
    }

    override fun onError(error: FacebookException?) {
        unregisterCallback()
        subject.onError(SocialException("Problem with FB login cause ${error?.message}"))
    }

    private fun unregisterCallback() {
        LoginManager.getInstance().unregisterCallback(callbackManager)
    }
}