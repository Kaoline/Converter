package kaoline.converter.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kaoline.converter.data.cache.model.RateEntity

@Database(entities = [RateEntity::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rateDao(): RateDao
}
