package ru.rovkinmax.socialnetwork.fb

import android.net.Uri
import android.support.v4.app.FragmentManager
import com.facebook.AccessToken
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.internal.Utility
import com.facebook.login.LoginManager
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.json.JSONObject
import ru.rovkinmax.socialnetwork.RxSocial
import ru.rovkinmax.socialnetwork.RxSocialFragment
import ru.rovkinmax.socialnetwork.SocialException
import ru.rovkinmax.socialnetwork.UserInfo

class RxFb(fragmentManager: FragmentManager) : RxSocial(fragmentManager) {
    companion object {
        private const val PHOTO_SIZE = 100
    }

    override fun createFragment(): RxSocialFragment = RxFbFragment()

    override fun getUserInfo(): Single<UserInfo> {
        return Single.create { emitter ->
            try {
                val profile = Profile.getCurrentProfile()
                if (profile != null)
                    dispatchProfileToEmitter(emitter, profile)
                else fetchProfile { dispatchProfileToEmitter(emitter, it) }
            } catch (e: Exception) {
                emitter.onError(SocialException(e.message.orEmpty()))
            }
        }
    }

    private fun dispatchProfileToEmitter(emitter: SingleEmitter<UserInfo>, profile: Profile) {
        emitter.onSuccess(UserInfo(profile.name, profile.getProfilePictureUri(PHOTO_SIZE, PHOTO_SIZE).toString()))
    }

    private fun fetchProfile(func: ((Profile) -> Unit)) {
        Utility.getGraphMeRequestWithCacheAsync(AccessToken.getCurrentAccessToken().token,
                object : Utility.GraphMeRequestWithCacheCallback {
                    override fun onSuccess(userInfo: JSONObject) {
                        val id = userInfo.optString("id") ?: return
                        val link = userInfo.optString("link")
                        val profile = Profile(
                                id,
                                userInfo.optString("first_name"),
                                userInfo.optString("middle_name"),
                                userInfo.optString("last_name"),
                                userInfo.optString("name"),
                                if (link != null) Uri.parse(link) else null
                        )
                        Profile.setCurrentProfile(profile)
                        func.invoke(profile)
                    }

                    override fun onFailure(error: FacebookException) {
                        return
                    }
                })
    }

    override fun logout(): Completable {
        return Completable.create {
            try {
                LoginManager.getInstance().logOut()
                it.onComplete()
            } catch (e: Exception) {
                it.onError(e)
            }

        }
    }
}