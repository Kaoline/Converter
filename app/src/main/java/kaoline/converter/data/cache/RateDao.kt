package kaoline.converter.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kaoline.converter.data.cache.model.RateEntity

@Dao
interface RateDao {
    @Query("SELECT * FROM RateEntity")
    fun getAllRates(): List<RateEntity>

    @Insert(onConflict = REPLACE)
    fun insertAll(rates: List<RateEntity>)
}
