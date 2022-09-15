package kaoline.converter.data.cache.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Represents a rate from USD
 */
@Entity
data class RateEntity(
    @PrimaryKey val currency: String,
    val rate: Float,
    val lastModified: Date = Date()
)
