package com.mikhail.vyakhirev.presentation.user_details_fragment

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.FirebaseAuth
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.Repository
import com.mikhail.vyakhirev.data.model.AppAuthorizationModel
import com.mikhail.vyakhirev.data.model.UserModel
import com.mikhail.vyakhirev.utils.GOOGLE_PRIVATE_CLIENT_ID
import com.mikhail.vyakhirev.utils.extensions.hasInternet
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    lateinit var googleSignInClient: GoogleSignInClient

    private val _user = MutableLiveData<UserModel>()
    val user: LiveData<UserModel> = _user

    fun loadUserData(context: Context){
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null){
            _user.value = UserModel(
                account.id!!,
                account.displayName,
                account.photoUrl.toString(),
                account.email,
                listOf())
        }
        else
            viewModelScope.launch {
               if(repository.isUserLoggedNow()) {
                   val acc = repository.loadUserFromAppDb()
                   _user.value = UserModel(
                       acc.login,
                       acc.login,
                       "",
                       acc.email,
                       listOf())
               }
            }
    }

    fun signOut(application: Context) {
        if (hasInternet(application)) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_PRIVATE_CLIENT_ID)
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(application,gso)
            googleSignInClient.signOut()
        }

        //Logout app authorization user
        viewModelScope.launch {
            val acc = repository.loadUserFromAppDb()
            acc.isLogged = false
            repository.logoutUser(acc)
        }
    }

}