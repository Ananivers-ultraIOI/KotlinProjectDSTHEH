package be.heh.kotlinproject_dstheh

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import be.heh.kotlindbsql.databinding.ActivityMainBinding
import be.heh.kotlindbsql.databinding.ActivityMaterielRegisterBinding
import be.heh.kotlinproject_dstheh.db.Materials
import be.heh.kotlinproject_dstheh.db.MaterialsRecord
import be.heh.kotlinproject_dstheh.db.MyDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException

class MaterielRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterielRegisterBinding
    var mode=""
    var account=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMaterielRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs_datas= PreferenceManager.getDefaultSharedPreferences((applicationContext))
        mode = prefs_datas?.getString("mode", "null") ?: "default_value"
        account = prefs_datas?.getString("account", "null") ?: "default_value"
    }
    fun onMRegisterClickManager(v:View){
        when(v.id) {
            binding.btLightCameraAction.id -> {
                val idText = binding.etMregisterId.text.toString()
                val id = idText.toIntOrNull()
                if (id == null) {
                    Toast.makeText(applicationContext, "Veuillez entrer un nombre valide pour l'ID", Toast.LENGTH_SHORT).show()
                } else {
                    action(id.toInt())
                }
            }
            binding.btMregister.id -> {
                if (binding.etMregisterType.text.toString().isEmpty()||binding.etMregisterModele.text.toString().isEmpty()||binding.etMregisterSw.text.toString().isEmpty()){
                    Toast.makeText(applicationContext, "Veuillez completer tout les champs !", Toast.LENGTH_SHORT).show()
                }else
                {
                    val type = binding.etMregisterType.text.toString()
                    val modele = binding.etMregisterModele.text.toString()
                    val sw = binding.etMregisterSw.text.toString()
                    val quantityText = binding.etMregisterQ.text.toString()
                    val quantity = quantityText.toIntOrNull()
                    if (quantity == null) {
                        Toast.makeText(applicationContext, "Veuillez entrer un nombre valide pour la quantité", Toast.LENGTH_SHORT).show()
                    } else {
                        addMaterial(type, modele, sw, quantity.toInt())
                    }
                }
            }
        }
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun action(i:Int){
        GlobalScope.launch(Dispatchers.Main) {
            val materials = withContext(Dispatchers.IO) {
                val db = Room.databaseBuilder(
                    applicationContext,
                    MyDB::class.java, "MyDataBase"
                ).build()
                val dao = db.materialsDao()
                dao.get(i)
            }
            if (materials != null) {
                when(mode){
                    "REMISE" -> add(materials)
                    "EMPRUN" -> remove(materials)
                }
            } else {
                Toast.makeText(applicationContext, "L'item n'a pas été trouvé", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun remove(m:MaterialsRecord){
        if (m.quantity==0){
            if(account=="SA"){
                AsyncTask.execute{
                    val db = Room.databaseBuilder(
                        applicationContext,
                        MyDB::class.java, "MyDataBase"
                    ).build()
                    val dao = db.materialsDao()
                    dao.deleteMateriel(m)
                }
            }else{
                Toast.makeText(applicationContext, "Vous n'avez pas le droit de supprimer un item", Toast.LENGTH_SHORT).show()
            }
            when(account){
                "RW"->toRW()
                "SA"->toSA()
            }
        }else{
            m.quantity -= 1
            AsyncTask.execute{
                val db = Room.databaseBuilder(
                    applicationContext,
                MyDB::class.java, "MyDataBase"
                ).build()
                val dao = db.materialsDao()
                dao.updateMateriel(m)
            }
            when(account){
                "RW"->toRW()
                "SA"->toSA()
            }
        }
    }
    fun add(m:MaterialsRecord){
        m.quantity += 1
        AsyncTask.execute{
            val db = Room.databaseBuilder(
                applicationContext,
                MyDB::class.java, "MyDataBase"
            ).build()
            val dao = db.materialsDao()
            dao.updateMateriel(m)
        }
        when(account){
            "RW"->toRW()
            "SA"->toSA()
        }
    }
    fun addMaterial(t:String,m:String,sw:String,q:Int){
        val mat = MaterialsRecord(0, t, m, sw, q)
        AsyncTask.execute{
            val db = Room.databaseBuilder(
                applicationContext,
                MyDB::class.java, "MyDataBase"
            ).build()
            val dao = db.materialsDao()
            dao.insertMaterial(mat)
        }
        when(account){
            "RW"->toRW()
            "SA"->toSA()
        }
    }
    fun toRW(){
        val iMa= Intent(this,MaterielInfoRwActivity::class.java)
        startActivity(iMa)
    }
    fun toSA(){
        val iMa= Intent(this,MaterielInfoSuperadminActivity::class.java)
        startActivity(iMa)
    }
}