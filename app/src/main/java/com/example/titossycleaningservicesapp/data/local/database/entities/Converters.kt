package com.example.titossycleaningservicesapp.data.local.database.entities

import androidx.room.TypeConverter
import com.example.titossycleaningservicesapp.domain.models.ApprovalStatus
import com.example.titossycleaningservicesapp.domain.models.Gender
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class Converters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    @TypeConverter
    fun fromGender(value: Gender): String {
        return value.name
    }

    @TypeConverter
    fun toGender(value: String) : Gender {
        return Gender.valueOf(value)
    }

    @TypeConverter
    fun fromApprovalStatus(value: ApprovalStatus): String {
        return value.name
    }

    @TypeConverter
    fun toApprovalStatus(value: String) : ApprovalStatus {
        return ApprovalStatus.valueOf(value)
    }

    @TypeConverter
    fun fromLocalDateTime(date : LocalDateTime?) : String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun toLocalDateTime(value: String?) : LocalDateTime? {
        return LocalDateTime.parse(value)
    }

}