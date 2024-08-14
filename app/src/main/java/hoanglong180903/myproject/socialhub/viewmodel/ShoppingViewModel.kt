package hoanglong180903.myproject.socialhub.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import hoanglong180903.myproject.socialhub.model.Album
import hoanglong180903.myproject.socialhub.model.Categories
import hoanglong180903.myproject.socialhub.model.Product
import hoanglong180903.myproject.socialhub.model.Track
import hoanglong180903.myproject.socialhub.repository.DeezerRepository
import hoanglong180903.myproject.socialhub.repository.ShoppingRepository
import hoanglong180903.myproject.socialhub.responses.TracksResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingViewModel(application: Application) : ViewModel() {
    private val repository: ShoppingRepository = ShoppingRepository(application)
    private val _categories = MutableLiveData<List<Categories>>()
    val categories: LiveData<List<Categories>> get() = _categories
    //
    private val _product = MutableLiveData<List<Product>>()
    val products : LiveData<List<Product>> get() = _product


    fun fetchCategories(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCategories(url)
            _categories.postValue(response.data)
        }
    }

    fun fetchProducts(url: String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProducts(url)
            _product.postValue(response.data)
        }
    }
}
