package ru.rovkinmax.socialnetwork.plus

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import ru.rovkinmax.socialnetwork.RxSocialFragment
import ru.rovkinmax.socialnetwork.SocialLoginResult
import ru.rovkinmax.socialnetwork.SocialType
import javax.security.auth.login.LoginException


class RxPlusFragment : RxSocialFragment() {

    companion object {
        private const val REQUEST_CODE_GOOGLE = 1
    }

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestProfile()
            .build()

    internal val client by lazy { GoogleSignIn.getClient(activity, gso) }

    override fun login() {
        val account = GoogleSignIn.getLastSignedInAccount(activity)
        if (account != null)
            dispatchAccount(account)
        else {
            startActivityForResult(client.signInIntent, REQUEST_CODE_GOOGLE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GOOGLE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            fetchAccountFromTask(task)
        }
    }

    private fun fetchAccountFromTask(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(ApiException::class.java)
            dispatchAccount(account)
        } catch (e: ApiException) {
            subject.onError(LoginException("Problem with G+ login ${e.message}"))
        }
    }

    private fun dispatchAccount(account: GoogleSignInAccount) {
        subject.onNext(SocialLoginResult(SocialType.PLUS))
    }
}