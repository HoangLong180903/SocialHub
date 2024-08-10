package hoanglong180903.myproject.socialhub.viewmodelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hoanglong180903.myproject.socialhub.viewmodel.CategoriesViewModel
import hoanglong180903.myproject.socialhub.viewmodel.DeezerViewModel

class CategoriesViewModelFactory(private val application: Application)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesViewModel::class.java)) {
            return CategoriesViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}