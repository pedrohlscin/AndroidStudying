package com.pedroso.beelog.database.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "location_table")
data class Location(
    @ColumnInfo(name = "latitude") val latitude: Double,
    @ColumnInfo(name = "longitude") val longitude: Double,
    @ColumnInfo(name = "datetime") val date : Date
    ){
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}