package be.heh.kotlinproject_dstheh.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MaterialTable")
data class MaterialsRecord (
    @ColumnInfo(name = "id")@PrimaryKey(autoGenerate = true) var id:Int=0,
    @ColumnInfo(name = "type") var type:String,
    @ColumnInfo(name = "modele") var modele:String,
    @ColumnInfo(name = "website") var website:String,
    @ColumnInfo(name = "quantity") var quantity:Int
)