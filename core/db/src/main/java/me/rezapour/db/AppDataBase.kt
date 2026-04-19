package me.rezapour.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.rezapour.db.dao.TimerDao
import me.rezapour.db.entites.TimerEntity
import kotlin.jvm.java

@Database(entities = [TimerEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun timerDao(): TimerDao

    companion object {
        fun databaseBuilder(context: Context) =
            Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "database-name"
            ).build()
    }
}