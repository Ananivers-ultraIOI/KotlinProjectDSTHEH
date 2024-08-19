package be.heh.kotlinproject_dstheh

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import be.heh.kotlindbsql.databinding.ActivityQrBinding
import be.heh.kotlinproject_dstheh.db.MaterialsRecord
import be.heh.kotlinproject_dstheh.db.MyDB
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrBinding
    var mode=""
    var account=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs_datas= PreferenceManager.getDefaultSharedPreferences((applicationContext))
        mode = prefs_datas?.getString("mode", "null") ?: "default_value"
        account=prefs_datas?.getString("account","null") ?:"default_value"
        IntentIntegrator(this).initiateScan()
    }
    @OptIn(DelicateCoroutinesApi::class)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan annulé", Toast.LENGTH_LONG).show()
                when(account){
                    "RW"->toRW()
                    "SA"->toSA()
                }
            } else {
                val parts = result.contents.split("#")
                GlobalScope.launch(Dispatchers.Main) {
                    val materials = withContext(Dispatchers.IO) {
                        val db = Room.databaseBuilder(
                            applicationContext,
                            MyDB::class.java, "MyDataBase"
                        ).build()
                        val dao = db.materialsDao()
                        dao.get(parts.get(1).toInt())
                    }
                    if (materials != null) {
                        when(mode){
                            "REMISE" -> add(materials)
                            "EMPRUN" -> remove(materials)
                        }
                    } else {
                        addMaterial(parts.get(2),parts.get(3),parts.get(0),parts.get(4).toInt())
                    }
                }
                Toast.makeText(this, "Scanné : " + parts.get(1), Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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
    fun remove(m: MaterialsRecord){
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
    fun add(m: MaterialsRecord){
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
}