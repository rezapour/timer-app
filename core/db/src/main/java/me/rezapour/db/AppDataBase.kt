package me.rezapour.db

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.rezapour.db.dao.TimerDao
import me.rezapour.db.entites.TimerDbEntity
import kotlin.jvm.java

@Database(entities = [TimerDbEntity::class], version = 1)
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