package me.rezapour.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.rezapour.db.converter.DataBaseConverter
import me.rezapour.db.dao.RoutineDao
import me.rezapour.db.dao.RoutineSessionDao
import me.rezapour.db.entites.RoutineEntity
import me.rezapour.db.entites.RoutineSessionEntity

@Database(entities = [RoutineEntity::class, RoutineSessionEntity::class], version = 1)
@TypeConverters(DataBaseConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun RoutineDao(): RoutineDao
    abstract fun RoutineSessionDao(): RoutineSessionDao

    companion object {
        fun databaseBuilder(context: Context) =
            Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "database-name"
            ).build()
    }
}