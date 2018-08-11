package ru.rovkinmax.socialnetwork.vk

import android.support.v4.app.FragmentManager
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiUser
import com.vk.sdk.api.model.VKList
import io.reactivex.Completable
import io.reactivex.Single
import ru.rovkinmax.socialnetwork.RxSocial
import ru.rovkinmax.socialnetwork.RxSocialFragment
import ru.rovkinmax.socialnetwork.SocialException
import ru.rovkinmax.socialnetwork.UserInfo

class RxVk(fragmentManager: FragmentManager) : RxSocial(fragmentManager) {
    companion object {
        private const val FIELD_PHOTO = "photo_200"
    }

    override fun createFragment(): RxSocialFragment = RxVkFragment()

    override fun getUserInfo(): Single<UserInfo> {
        return Single.create { emitter ->
            VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, FIELD_PHOTO))
                    .executeSyncWithListener(object : VKRequest.VKRequestListener() {
                        @Suppress("UNCHECKED_CAST")
                        override fun onComplete(response: VKResponse) {
                            val userList = response.parsedModel as VKList<VKApiUser>
                            val user = userList.component1()
                            val photo = user.photo_200
                            val name = "${user.first_name.orEmpty()} ${user.last_name.orEmpty()}".trim()
                            emitter.onSuccess(UserInfo(name, photo))
                        }

                        override fun onError(error: VKError?) {
                            emitter.onError(SocialException(error?.errorMessage.orEmpty()))
                        }
                    })
        }
    }

    override fun logout(): Completable {
        return Completable.create {
            try {
                VKSdk.logout()
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }
        }
    }
}