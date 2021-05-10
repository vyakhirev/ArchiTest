package com.mikhail.vyakhirev.presentation.main_activity

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.mikhail.vyakhirev.data.IRepository
import com.mikhail.vyakhirev.data.model.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONException
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {
//    private var facebookToken = AccessToken.getCurrentAccessToken()

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user

    private val _isLogged = MutableLiveData<Boolean>()
    val isLogged: LiveData<Boolean> = _isLogged

    fun isFbLogged(): Boolean {
        val facebookToken = AccessToken.getCurrentAccessToken()
        return facebookToken != null && !facebookToken.isExpired
    }

    fun isGoogleLogin(context: Context): Boolean {
        return GoogleSignIn.getLastSignedInAccount(context)?.isExpired != true
    }

    fun isMyAppLogin() {
        viewModelScope.launch {
            _isLogged.value = repository.isUserLoggedNow()
        }
    }

    fun loadFbUserData() {
        viewModelScope.launch {
            val facebookToken = AccessToken.getCurrentAccessToken()
            val request = GraphRequest.newMeRequest(facebookToken) { `object`, response ->
                try {
                    val id = `object`.getString("id")
                    _user.value = UserModel(
                        id,
                        `object`.getString("first_name") + " " + `object`.getString("last_name"),
                        "https://graph.facebook.com/$id/picture?type=small",
                        `object`.getString("email"), listOf()
                    )
//                    Log.d("Kan","User is:  ${`object`.getString("first_name")}")
//                    Log.d("Kan","User is:  ${_user.value}")
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

    fun loadGoogleUserData() {

    }
}