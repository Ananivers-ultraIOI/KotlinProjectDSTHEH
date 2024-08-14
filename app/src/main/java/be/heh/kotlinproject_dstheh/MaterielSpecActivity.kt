package be.heh.kotlinproject_dstheh

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import be.heh.kotlindbsql.R
import be.heh.kotlindbsql.databinding.ActivityMainBinding
import be.heh.kotlindbsql.databinding.ActivityMaterielSpecBinding
import be.heh.kotlinproject_dstheh.db.MyDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MaterielSpecActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterielSpecBinding
    var account=""
    var idpass=0
    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMaterielSpecBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs_datas= PreferenceManager.getDefaultSharedPreferences((applicationContext))
        account = prefs_datas?.getString("account", "null") ?: "default_value"
        idpass = (prefs_datas?.getString("id", "null") ?: "default_value").toInt()
        GlobalScope.launch(Dispatchers.Main) {
            val materials = withContext(Dispatchers.IO) {
                val db = Room.databaseBuilder(
                    applicationContext,
                    MyDB::class.java, "MyDataBase"
                ).build()
                val dao = db.materialsDao()
                dao.get(idpass)
            }
            val table = binding.tableLayout
            table.removeAllViews()
            val typeRow = TableRow(this@MaterielSpecActivity)
            val headerType = TextView(this@MaterielSpecActivity).apply {
                text = "Type : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val typeView = TextView(this@MaterielSpecActivity).apply {
                text = materials.type
                setTextAppearance(R.style.TableText)
            }
            typeRow.addView(headerType)
            typeRow.addView(typeView)
            table.addView(typeRow)
            val modelRow = TableRow(this@MaterielSpecActivity)
            val headerModel = TextView(this@MaterielSpecActivity).apply {
                text = "Modèle : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val modelView = TextView(this@MaterielSpecActivity).apply {
                text = materials.modele
                setTextAppearance(R.style.TableText)
            }
            modelRow.addView(headerModel)
            modelRow.addView(modelView)
            table.addView(modelRow)
            val idRow = TableRow(this@MaterielSpecActivity)
            val headerId = TextView(this@MaterielSpecActivity).apply {
                text = "ID : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val idView = TextView(this@MaterielSpecActivity).apply {
                text = materials.id.toString()
                setTextAppearance(R.style.TableText)
            }
            idRow.addView(headerId)
            idRow.addView(idView)
            table.addView(idRow)
            val sitewebRow = TableRow(this@MaterielSpecActivity)
            val headerSiteweb = TextView(this@MaterielSpecActivity).apply {
                text = "Site Web : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val sitewebView = TextView(this@MaterielSpecActivity).apply {
                text = materials.website
                setTextAppearance(R.style.TableText)
            }
            sitewebRow.addView(headerSiteweb)
            sitewebRow.addView(sitewebView)
            table.addView(sitewebRow)
            val qantityRow = TableRow(this@MaterielSpecActivity)
            val headerQuantite = TextView(this@MaterielSpecActivity).apply {
                text = "Quantitée : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val quantiteView = TextView(this@MaterielSpecActivity).apply {
                text = materials.quantity.toString()
                setTextAppearance(R.style.TableText)
            }
            qantityRow.addView(headerQuantite)
            qantityRow.addView(quantiteView)
            table.addView(qantityRow)
            }
        }
    fun onMSpecClickManager(v:View){
        when(v.id){
            binding.btMspecMinfo.id->comeBack()
        }
    }
    fun comeBack(){
        when(account){
            "R"-> toR()
            "RW"->toRW()
            "SA"->toSA()
        }
    }
    fun toR(){
        val iMa= Intent(this,MaterielInfoActivity::class.java)
        startActivity(iMa)
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