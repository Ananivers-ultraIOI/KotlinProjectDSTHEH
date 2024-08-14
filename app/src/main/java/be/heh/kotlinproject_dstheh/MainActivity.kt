package be.heh.kotlinproject_dstheh

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import be.heh.kotlindbsql.databinding.ActivityMainBinding
import be.heh.kotlinproject_dstheh.db.MyDB
import be.heh.kotlinproject_dstheh.db.UserRecord
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            val ins = openFileInput("login.txt")
            val reader = BufferedReader(InputStreamReader(ins))
            val line=reader.readLine()
            reader.close()
            ins.close()
            if (line != null) {
                val parts = line.split("#")
                val email = parts[0]
                val password = parts[1]
                AsyncTask.execute{
                    val db = Room.databaseBuilder(
                        applicationContext,
                        MyDB::class.java, "MyDataBase"
                    ).build()
                    val dao = db.userDao()
                    val dbL= dao.get(email)
                    if (password==dbL?.pwd){
                        when(dbL.rights){
                            0 -> toMaterials()
                            1 -> toMaterialsRW()
                            2 -> toUser()
                        }
                    }
                }
            }
        } catch (e:FileNotFoundException ) {
            e.printStackTrace();
        } catch (e:IOException) {
            e.printStackTrace();
        }

    }
    fun onMainClickManager(v:View){
        when(v.id){
            binding.btMainConnexion.id -> connexion()
            binding.tvMainRegister.id -> toRegister()
        }
    }
    fun toRegister(){
        val iRegister=Intent(this,RegisterActivity::class.java)
        startActivity(iRegister)
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun connexion() {
        GlobalScope.launch(Dispatchers.Main) {
            if (binding.etMainEmail.text.toString().isEmpty() || binding.etMainPwd.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(applicationContext, "Complétez tout les champs !", Toast.LENGTH_LONG)
                    .show()
            } else {
                val userRecord = withContext(Dispatchers.IO) {
                    val db = Room.databaseBuilder(
                        applicationContext,
                        MyDB::class.java, "MyDataBase"
                    ).build()
                    val dao = db.userDao()
                    dao?.get(binding.etMainEmail.text.toString())
                }
                if (userRecord==null){
                    Toast.makeText(applicationContext, "L'email n'existe pas", Toast.LENGTH_LONG).show()
                }else{
                    if (userRecord.pwd!=binding.etMainPwd.text.toString()) {
                        Toast.makeText(applicationContext, "Les mots de passes ne correspondent pas", Toast.LENGTH_LONG).show()
                    } else {
                        val str=binding.etMainEmail.text.toString()+"#"+binding.etMainPwd.text.toString()
                        try {
                            val ous = openFileOutput("login.txt", MODE_PRIVATE)
                            val tab = str.toByteArray()
                            ous.write(tab)
                            ous.close()
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        when(userRecord.rights){
                            0 -> toMaterials()
                            1 -> toMaterialsRW()
                            2 -> toUser()
                        }
                    }
                }
            }
        }
    }
    fun toMaterials(){
        val iRegister=Intent(this,MaterielInfoActivity::class.java)
        startActivity(iRegister)
    }
    fun toMaterialsRW(){
        val iRegister=Intent(this,MaterielInfoRwActivity::class.java)
        startActivity(iRegister)
    }
    fun toUser(){
        val iRegister=Intent(this,UserGestionActivity::class.java)
        startActivity(iRegister)
    }
}
