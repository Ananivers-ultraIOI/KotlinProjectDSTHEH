package be.heh.kotlinproject_dstheh

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import be.heh.kotlindbsql.R
import be.heh.kotlindbsql.databinding.ActivityUserGestionBinding
import be.heh.kotlinproject_dstheh.db.MyDB
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException

class UserGestionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserGestionBinding
    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserGestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GlobalScope.launch(Dispatchers.Main) {
            val users = withContext(Dispatchers.IO) {
                val db = Room.databaseBuilder(
                    applicationContext,
                    MyDB::class.java, "MyDataBase"
                ).build()
                val dao = db.userDao()
                dao.get()
            }
            val table = binding.tableLayout
            table.removeAllViews()
            val headerRow = TableRow(this@UserGestionActivity)
            val headerEmail= TextView(this@UserGestionActivity).apply {
                text="Email"
                setTextAppearance(R.style.TableHeaderText)
            }
            val headerRights = TextView(this@UserGestionActivity).apply {
                text = "R&W"
                setTextAppearance(R.style.TableHeaderText)
            }
            val headerDelete=TextView(this@UserGestionActivity).apply {
                text ="Supprimer ?"
                setTextAppearance(R.style.TableHeaderText)
            }
            headerRow.addView(headerEmail)
            headerRow.addView(headerRights)
            headerRow.addView(headerDelete)
            table.addView(headerRow)
            users.forEach{ user ->
                if (user.rights!=2){
                    val tableRow =TableRow(this@UserGestionActivity)
                    val emailView =TextView(this@UserGestionActivity).apply {
                        text=user.email
                        setTextAppearance(R.style.TableText)
                    }
                    tableRow.addView(emailView)
                    val checkBox = CheckBox(this@UserGestionActivity).apply {
                        isChecked = user.rights == 1
                        setOnCheckedChangeListener { _, isChecked ->
                            GlobalScope.launch(Dispatchers.IO) {
                                user.rights = if (isChecked) 1 else 0
                                val db = Room.databaseBuilder(
                                    applicationContext,
                                    MyDB::class.java, "MyDataBase"
                                ).build()
                                db.userDao().updatePersonne(user)
                            }
                        }
                    }
                    tableRow.addView(checkBox)
                    val deleteButton = Button(this@UserGestionActivity).apply {
                        text = "Delete"
                        setTextAppearance(R.style.DeleteButton)
                        setOnClickListener {
                            GlobalScope.launch(Dispatchers.IO) {
                                val db = Room.databaseBuilder(
                                    applicationContext,
                                    MyDB::class.java, "MyDataBase"
                                ).build()
                                db.userDao().deletePersonne(user)
                                withContext(Dispatchers.Main) {
                                    table.removeView(tableRow)
                                }
                            }
                        }
                    }
                    tableRow.addView(deleteButton)
                    table.addView(tableRow)
                }
            }
        }
    }
    fun onUserClickManager(v: View){
        when(v.id){
            binding.tvUserDeconnexion.id -> logout()
            binding.btUserMateriel.id -> toMaterials()
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
    fun toMaterials(){
        val iMateriel= Intent(this,MaterielInfoSuperadminActivity::class.java)
        startActivity(iMateriel)
    }
}