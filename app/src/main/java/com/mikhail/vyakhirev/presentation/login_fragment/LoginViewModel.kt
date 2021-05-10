package com.mikhail.vyakhirev.presentation.login_fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.*
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.Repository
import com.mikhail.vyakhirev.data.model.*
import com.mikhail.vyakhirev.utils.Event
import com.mikhail.vyakhirev.utils.GOOGLE_PRIVATE_CLIENT_ID
import com.mikhail.vyakhirev.utils.RC_GOOGLE_SIGN_IN_CODE
import com.mikhail.vyakhirev.utils.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableLiveData<AuthUiModel>()
    val uiState: LiveData<AuthUiModel> = _uiState


    private val _isLogged = MutableLiveData<Event<Boolean>>()
    val isLogged: LiveData<Event<Boolean>> = _isLogged

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user


    /////////////////////////// My login ////////////////////////////
    fun myLogin(login: String, password: String) {
        viewModelScope.launch {
            _isLogged.value = Event(repository.isAccessGranted(login, password))

        }
    }
    /////////////////////////// End of my login ////////////////////////////

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
//                    repository.saveAuthResult(authCredential.zza())
                } else
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

//    private val mGoogleSignClient by lazy {
//
//    }

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

    fun googleSignIn(context: Context) = GoogleSignIn.getClient(context, gso).signInIntent

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
//                                String.format(
//                                    resources.getString(R.string.auth_user_collision_message), email
//                                )
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


    fun loadUserData() {
        viewModelScope.launch {
            val facebookToken = AccessToken.getCurrentAccessToken()
            val request = GraphRequest.newMeRequest(facebookToken) { `object`, response ->
                try {
                    val id = `object`.getString("id")
                    _user.value = UserModel(
                        id,
                        `object`.getString("first_name") + " " + `object`.getString("last_name"),
                        "https://graph.facebook.com/$id/picture",
                        `object`.getString("email"), listOf()
                    )
                    Log.d("Kan", "User is:  ${`object`.getString("first_name")}")
                    Log.d("Kan", "User is:  ${_user.value}")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            val parameters = Bundle()
            parameters.putString("fields", "first_name,last_name,email,id")
            request.parameters = parameters
            request.executeAsync()
        }
    }
}
