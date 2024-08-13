package be.heh.kotlinproject_dstheh.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class UserRecord (
    @ColumnInfo(name = "id")@PrimaryKey(autoGenerate = true) var id:Int=0,
    @ColumnInfo(name = "email") var email:String,
    @ColumnInfo(name = "pwd") var pwd:String,
    @ColumnInfo(name = "Right") var rights:Int
)