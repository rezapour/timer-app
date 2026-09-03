package me.rezapour.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.rezapour.db.converter.DataBaseConverter
import me.rezapour.db.dao.WorkoutDao
import me.rezapour.db.dao.WorkoutSessionDao
import me.rezapour.db.entites.WorkoutEntity
import me.rezapour.db.entites.WorkoutSessionEntity

@Database(
    entities = [WorkoutEntity::class, WorkoutSessionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DataBaseConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutSessionDao(): WorkoutSessionDao

    companion object {

        const val DATA_BASE_NAME = "workout_app_db"
        fun databaseBuilder(context: Context) =
            Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                DATA_BASE_NAME
            ).build()
    }
}
