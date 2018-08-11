package ru.rovkinmax.socialnetwork.vk

import android.content.Context
import android.content.Intent
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.VKServiceActivity
import com.vk.sdk.api.VKError
import ru.rovkinmax.socialnetwork.RxSocialFragment
import ru.rovkinmax.socialnetwork.SocialException
import ru.rovkinmax.socialnetwork.SocialLoginResult
import ru.rovkinmax.socialnetwork.SocialType

class RxVkFragment : RxSocialFragment(), VKCallback<VKAccessToken> {
    companion object {
        private const val KEY_TYPE = "arg1"
        private const val KEY_SDK_CUSTOM_INITIALIZE = "arg4"
        private const val KEY_SCOPE_LIST = "arg2"
    }

    override fun login() {
        val intent = createIntent(activity, VKServiceActivity.VKServiceType.Authorization)
        intent.putStringArrayListExtra(KEY_SCOPE_LIST, arrayListOf())
        startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.outerCode)
    }

    private fun createIntent(appCtx: Context, type: VKServiceActivity.VKServiceType): Intent {
        val intent = Intent(appCtx, VKServiceActivity::class.java)
        intent.putExtra(KEY_TYPE, type.name)
        intent.putExtra(KEY_SDK_CUSTOM_INITIALIZE, VKSdk.isCustomInitialize())
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (VKSdk.onActivityResult(requestCode, resultCode, data, this).not())
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResult(res: VKAccessToken?) {
        if (res != null) subject.onNext(SocialLoginResult(SocialType.VK))
        else subject.onError(SocialException("Problem with vk login"))
    }

    override fun onError(error: VKError?) {
        subject.onError(SocialException("Problem with vk login cause ${error?.errorReason} ${error?.errorMessage}"))
    }
}