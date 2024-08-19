package be.heh.kotlinproject_dstheh.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MaterialsDao {
    @Query("SELECT * FROM MaterialTable")
    fun get():List<MaterialsRecord>
    @Query("SELECT * FROM MaterialTable WHERE id = :id")
    fun get(id: Int): MaterialsRecord
    @Query("SELECT * FROM MaterialTable WHERE modele = :modele")
    fun getByModele(modele: String): MaterialsRecord
    @Insert
    fun insertMaterial(vararg listCategories: MaterialsRecord)
    @Update
    fun updateMateriel(task: MaterialsRecord)
    @Delete
    fun deleteMateriel(task: MaterialsRecord)
}