package be.heh.kotlinproject_dstheh

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
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
import be.heh.kotlindbsql.databinding.ActivityMaterielInfoBinding
import be.heh.kotlinproject_dstheh.db.MyDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException

class MaterielInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterielInfoBinding
    var prefs_datas: SharedPreferences? = null
    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMaterielInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs_datas = PreferenceManager.getDefaultSharedPreferences((applicationContext))
        GlobalScope.launch(Dispatchers.Main) {
            val materials = withContext(Dispatchers.IO) {
                val db = Room.databaseBuilder(
                    applicationContext,
                    MyDB::class.java, "MyDataBase"
                ).build()
                val dao = db.materialsDao()
                dao.get()
            }

            val table = binding.tableLayout
            table.removeAllViews()

            val headerRow = TableRow(this@MaterielInfoActivity)
            val headerModèle = TextView(this@MaterielInfoActivity).apply {
                text = "Modèle"
                setTextAppearance(R.style.TableHeaderText)
            }
            val headerQuantity = TextView(this@MaterielInfoActivity).apply {
                text = "Quantité"
                setTextAppearance(R.style.TableHeaderText)
            }
            val headerInfo = TextView(this@MaterielInfoActivity).apply {
                text = "Info"
                setTextAppearance(R.style.TableHeaderText)
            }

            headerRow.addView(headerModèle)
            headerRow.addView(headerQuantity)
            headerRow.addView(headerInfo)
            table.addView(headerRow)

            materials.forEach { material ->
                val tableRow = TableRow(this@MaterielInfoActivity)

                val modeleView = TextView(this@MaterielInfoActivity).apply {
                    text = material.modele
                    setTextAppearance(R.style.TableText)
                }
                tableRow.addView(modeleView)

                val quantityView = TextView(this@MaterielInfoActivity).apply {
                    text = material.quantity.toString()
                    setTextAppearance(R.style.TableText)
                }
                tableRow.addView(quantityView)

                val infosButton = Button(this@MaterielInfoActivity).apply {
                    text = "Infos"
                    setTextAppearance(R.style.DeleteButton)
                    setOnClickListener {
                        val editeur_datas = prefs_datas!!.edit()
                        editeur_datas.putString("account", "R")
                        editeur_datas.putString("id",material.id.toString())
                        editeur_datas.commit()
                        val intent=Intent(this@MaterielInfoActivity,MaterielSpecActivity::class.java)
                        startActivity(intent)
                        }
                    }

                tableRow.addView(infosButton)

                table.addView(tableRow)
            }
        }
    }
    fun onMatClickManager(v: View){
        when(v.id){
            binding.tvMatDeconnexion.id -> logout()
        }
    }
    fun logout(){
        try {
            val ous = openFileOutput("login.txt", MODE_PRIVATE)
            val tab = "".toByteArray()
            ous.write(tab)
            ous.close()
            val iConnection= Intent(this,MainActivity::class.java)
            startActivity(iConnection)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}