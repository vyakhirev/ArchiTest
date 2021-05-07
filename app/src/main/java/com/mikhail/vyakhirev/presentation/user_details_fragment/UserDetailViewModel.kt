package com.mikhail.vyakhirev.presentation.user_details_fragment

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.*
import com.google.firebase.auth.FirebaseAuth
import com.mikhail.vyakhirev.R
import com.mikhail.vyakhirev.data.Repository
import com.mikhail.vyakhirev.data.model.UserModel
import com.mikhail.vyakhirev.utils.GOOGLE_PRIVATE_CLIENT_ID
import com.mikhail.vyakhirev.utils.extensions.hasInternet
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
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
    }

    fun signOut(application: Context) {
        if (hasInternet(application)) {
//            val account = GoogleSignIn.getLastSignedInAccount(application)
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GOOGLE_PRIVATE_CLIENT_ID)
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(application,gso)

            googleSignInClient.signOut()
//            repository .signOut(googleSignInClient)
//            finish.value = Event("")
        }
    }

}