package hoanglong180903.myproject.socialhub.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hoanglong180903.myproject.socialhub.model.Favorite
import hoanglong180903.myproject.socialhub.model.Track

@Dao
interface DAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteDisFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorite_table ORDER BY id ASC")
    fun getAllFavorite(): LiveData<List<Favorite>>

}