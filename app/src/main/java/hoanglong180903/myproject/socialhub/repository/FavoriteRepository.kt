package hoanglong180903.myproject.socialhub.repository

import androidx.lifecycle.LiveData
import hoanglong180903.myproject.socialhub.database.DAO
import hoanglong180903.myproject.socialhub.model.Favorite


class FavoriteRepository(private val dao : DAO) {
    val getAllFavorite : LiveData<List<Favorite>> = dao.getAllFavorite()

    suspend fun insertFavorite(favorite: Favorite){
        dao.insertFavorite(favorite)
    }

    suspend fun deleteDisFavorite(favorite: Favorite){
        dao.deleteDisFavorite(favorite)
    }
}