package com.mikhail.vyakhirev.presentation.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikhail.vyakhirev.data.Repository
import com.mikhail.vyakhirev.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _isRegistered = MutableLiveData<Event<Boolean>>()
    val isRegistered: LiveData<Event<Boolean>> = _isRegistered

    fun userRegistration(login:String,email:String, password:String){
        viewModelScope.launch {
            repository.registerUser(login,email,password)
        }
        _isRegistered.value = Event(true)
    }
}