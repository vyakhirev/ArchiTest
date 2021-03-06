package com.mikhail.vyakhirev.presentation.login_fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.Repository
import com.mikhail.vyakhirev.data.model.AuthType
import com.mikhail.vyakhirev.data.model.AuthUiModel
import com.mikhail.vyakhirev.data.model.MaterialDialogContent
import com.mikhail.vyakhirev.data.model.Result
import com.mikhail.vyakhirev.utils.Event
import com.mikhail.vyakhirev.utils.GOOGLE_PRIVATE_CLIENT_ID
import com.mikhail.vyakhirev.utils.RC_GOOGLE_SIGN_IN_CODE
import com.mikhail.vyakhirev.utils.await
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val application: Context,
    private val firebaseAuth: FirebaseAuth,
    private val repository: Repository
) : ViewModel() {

    val uiState: LiveData<AuthUiModel> get() = _uiState

    private val resources = application.resources
    private val _uiState = MutableLiveData<AuthUiModel>()

    /////////////////////////// Firebase Facebook Authentication Starts ////////////////////////////

    val loginManager: LoginManager = LoginManager.getInstance()
    private val mCallbackManager = CallbackManager.Factory.create()
    private val mFacebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            val credential = FacebookAuthProvider.getCredential(result?.accessToken?.token!!)
            handleFacebookCredential(credential)

        }

        override fun onCancel() {
            viewModelScope.launch {
                emitUiState(
                    error = Event(
                        AuthType.FACEBOOK to MaterialDialogContent(
                            R.string.try_again,
                            R.string.operation_cancelled_content,
                            R.string.operation_cancelled,
                            R.string.cancel
                        )
                    )
                )
            }
        }

        override fun onError(error: FacebookException?) {
            viewModelScope.launch {
                sendErrorState(AuthType.FACEBOOK)
            }
        }
    }

    init {
        loginManager.registerCallback(mCallbackManager, mFacebookCallback)
    }

    private fun handleFacebookCredential(authCredential: AuthCredential) {
        viewModelScope.launch {
            emitUiState(showProgress = true)
            safeApiCall { Result.Success(signInWithCredential(authCredential)!!) }.also {
                if (it is Result.Success && it.data.user != null) {
                    emitUiState(success = true)
                    repository.saveAuthResult(authCredential.zza())
                }
               else
                    emitUiState(success = true)
//                else if (it is Result.Error) handleErrorStateForSignInCredential(
//                    it.exception, AuthType.FACEBOOK
//                )
            }
        }
    }

    //////////////////////// Firebase Facebook Authentication Ends /////////////////////////////////


    //////////////////////// Firebase Google Authentication Starts /////////////////////////////////

    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(GOOGLE_PRIVATE_CLIENT_ID)
        .requestEmail()
        .build()
    private val mGoogleSignClient by lazy {
        GoogleSignIn.getClient(application, gso)
    }

    private fun handleGoogleSignInResult(data: Intent) {
        viewModelScope.launch {
            emitUiState(showProgress = true)
            safeApiCall {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).await()
                val authResult =
                    signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null))!!
                Result.Success(authResult)
            }.also {
                if (it is Result.Success && it.data.user != null)
                    emitUiState(success = true)
                else sendErrorState(AuthType.GOOGLE)
            }
        }
    }

    fun googleSignIn() = mGoogleSignClient.signInIntent

    //////////////////////// Firebase Google Authentication Ends ///////////////////////////////////


    //////////////////////// Firebase Twitter Authentication Starts ////////////////////////////////

    private val twitterAuthProvider = OAuthProvider.newBuilder("twitter.com")
        .build()

    fun doTwitterAuthentication(activity: Activity) {
        val twitterTask =
            firebaseAuth.startActivityForSignInWithProvider(activity, twitterAuthProvider)
        viewModelScope.launch {
            safeApiCall { Result.Success(twitterTask.await()) }.also {
                if (it is Result.Success && it.data.user != null)
                    emitUiState(success = true)
                else if (it is Result.Error) handleErrorStateForSignInCredential(
                    it.exception, AuthType.TWITTER
                )
            }
        }
    }

    //////////////////////// Firebase Twitter Authentication Ends //////////////////////////////////


    //////////////////////// Firebase GitHub Authentication Starts /////////////////////////////////

    private val githubAuthProvider = OAuthProvider.newBuilder("github.com")
        .build()

    fun doGithubAuthentication(activity: Activity) {
        val githubTask =
            firebaseAuth.startActivityForSignInWithProvider(activity, githubAuthProvider)
        viewModelScope.launch {
            safeApiCall { Result.Success(githubTask.await()) }.also {
                if (it is Result.Success && it.data.user != null)
                    emitUiState(success = true)
                else if (it is Result.Error) handleErrorStateForSignInCredential(
                    it.exception, AuthType.GITHUB
                )
            }
        }
    }

    //////////////////////// Firebase GitHub Authentication Ends ///////////////////////////////////

    private suspend fun sendErrorState(authType: AuthType) {
        emitUiState(
            error = Event(
                authType to MaterialDialogContent(
                    R.string.try_again, R.string.internet_not_working,
                    R.string.limited_internet_connection, R.string.cancel
                )
            )
        )
    }

    /**
     * Fetch all providers associated with this email. In case when user previously login with xyz provider and after logout he/she try to login with abc provider.
     */

    fun fetchAllProviderForEmail(email: String) {
        viewModelScope.launch {
            safeApiCall {
                Result.Success(
                    firebaseAuth.fetchSignInMethodsForEmail(email).await()
                )
            }.also {
                if (it is Result.Success && it.data.signInMethods != null)
                    emitUiState(
                        linkProvider = Event(
                            it.data.signInMethods!! to MaterialDialogContent(
                                R.string.select, null, R.string.user_collision, R.string.cancel,
                                String.format(
                                    resources.getString(R.string.auth_user_collision_message), email
                                )
                            )
                        )
                    )
                else sendErrorState(AuthType.EMAIL.apply { authValue = email })
            }
        }
    }

    //////////////////////// Firebase Authentication Common Code Starts ////////////////////////////

    private suspend fun handleErrorStateForSignInCredential(
        exception: Exception, authType: AuthType
    ) {
        if (exception is FirebaseAuthUserCollisionException) {
            val email = exception.email
            if (email != null) fetchAllProviderForEmail(email)
            else sendErrorState(authType)
        } else sendErrorState(authType)
    }

    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GOOGLE_SIGN_IN_CODE && data != null) {
            handleGoogleSignInResult(data)
        } else if (mCallbackManager.onActivityResult(requestCode, resultCode, data))
            println("Result should be handled")
    }

    @Throws(Exception::class)
    private suspend fun signInWithCredential(authCredential: AuthCredential): AuthResult? {
        return firebaseAuth.signInWithCredential(authCredential).await()
    }

    private suspend fun emitUiState(
        showProgress: Boolean = false,
        error: Event<Pair<AuthType, MaterialDialogContent>>? = null,
        success: Boolean = false,
        linkProvider: Event<Pair<List<String>, MaterialDialogContent>>? = null
    ) = withContext(Dispatchers.Main)
    {
        AuthUiModel(showProgress, error, success, linkProvider).also {
            _uiState.value = it
        }
    }

    //////////////////////// Firebase Authentication Common Code Ends //////////////////////////////

    private suspend fun <T : Any> safeApiCall(call: suspend () -> Result<T>): Result<T> {
        return try {
            call()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}