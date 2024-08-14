package hoanglong180903.myproject.socialhub.viewmodelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hoanglong180903.myproject.socialhub.repository.DeezerRepository
import hoanglong180903.myproject.socialhub.viewmodel.DeezerViewModel

class DeezerViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeezerViewModel::class.java)) {
            return DeezerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}