package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hoanglong180903.myproject.socialhub.model.Categories
import hoanglong180903.myproject.socialhub.repository.CategoriesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel(application: Application) : ViewModel() {
    private val repository: CategoriesRepository = CategoriesRepository(application)
    private val _categories = MutableLiveData<List<Categories>>()
    val categories: LiveData<List<Categories>> get() = _categories
    fun fetchCategories(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCategories(url)
            _categories.postValue(response.data)
        }
    }
}