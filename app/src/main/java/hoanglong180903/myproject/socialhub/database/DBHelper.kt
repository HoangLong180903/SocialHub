package hoanglong180903.myproject.socialhub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hoanglong180903.myproject.socialhub.model.Favorite

@Database(entities = [Favorite::class] , version = 1, exportSchema = false)
abstract class DBHelper : RoomDatabase() {

    abstract fun dao() : DAO

    companion object{
        @Volatile
        private var INSTANCE : DBHelper? = null
        fun getDatabase(context : Context) : DBHelper{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DBHelper::class.java,
                    "music_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}