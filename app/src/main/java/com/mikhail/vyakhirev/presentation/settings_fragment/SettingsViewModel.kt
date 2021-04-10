package com.mikhail.vyakhirev.presentation.settings_fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mikhail.vyakhirev.data.IRepository

class SettingsViewModel(private val IRepository: IRepository) : ViewModel()  {
}
@Suppress("UNCHECKED_CAST")
class SettingsViewModelFactory(
    private val IRepository: IRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(
            IRepository
        ) as T
    }

}