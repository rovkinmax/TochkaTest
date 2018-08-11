package ru.rovkinmax.socialnetwork.plus

import android.support.v4.app.FragmentManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import io.reactivex.Completable
import io.reactivex.Single
import ru.rovkinmax.socialnetwork.RxSocial
import ru.rovkinmax.socialnetwork.RxSocialFragment
import ru.rovkinmax.socialnetwork.SocialException
import ru.rovkinmax.socialnetwork.UserInfo

class RxPlus(fragmentManager: FragmentManager) : RxSocial(fragmentManager) {
    override fun createFragment(): RxSocialFragment = RxPlusFragment()

    override fun getUserInfo(): Single<UserInfo> {
        return Single.create { emitter ->
            val account = GoogleSignIn.getLastSignedInAccount(fragment.activity)
            if (account != null) {
                emitter.onSuccess(UserInfo(account.displayName.orEmpty(), account.photoUrl?.toString()))
            } else {
                emitter.onError(SocialException("Can't get user info"))
            }
        }
    }

    override fun logout(): Completable {
        return Completable.create { emitter ->
            (fragment as RxPlusFragment).client.revokeAccess()
                    .addOnCompleteListener { emitter.onComplete() }
        }
    }
}