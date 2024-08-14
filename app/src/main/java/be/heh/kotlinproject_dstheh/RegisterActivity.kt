package be.heh.kotlinproject_dstheh

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import be.heh.kotlindbsql.databinding.ActivityRegisterBinding
import be.heh.kotlinproject_dstheh.db.MyDB
import be.heh.kotlinproject_dstheh.db.User
import be.heh.kotlinproject_dstheh.db.UserRecord
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun onRegisterClickManager(v: View){
        when(v.id){
            binding.btRegister.id -> register()
            binding.tvRegisterConnexion.id -> toConnexion()
        }
    }
    fun toConnexion(){
        val iConnection= Intent(this,MainActivity::class.java)
        startActivity(iConnection)
    }
    @OptIn(DelicateCoroutinesApi::class)
    fun register(){
        GlobalScope.launch ( Dispatchers.Main ){
            val rights= withContext(Dispatchers.IO){
                val db = Room.databaseBuilder(
                    applicationContext,
                    MyDB::class.java, "MyDataBase"
                ).build()
                val dao = db.userDao()
                val dbL = dao?.get()
                if (dbL?.isEmpty() == true) 2 else 0
            }
            if (binding.etRegisterEmail.text.toString().isEmpty()||binding.etRegisterPwd.text.toString().isEmpty()||binding.etRegisterPwdConfirm.text.toString().isEmpty())
            {
                Toast.makeText(applicationContext,"Complétez tout les champs !", Toast.LENGTH_LONG).show()
            }else{
                if (binding.etRegisterPwd.text.toString()!=binding.etRegisterPwdConfirm.text.toString()){
                    Toast.makeText(applicationContext,"les mdp ne correspondent pas", Toast.LENGTH_LONG).show()
                }else{
                    val emailExists = withContext(Dispatchers.IO) {
                        val db = Room.databaseBuilder(
                            applicationContext,
                            MyDB::class.java, "MyDataBase"
                        ).build()
                        val dao = db.userDao()
                        dao?.get(binding.etRegisterEmail.text.toString()) != null
                    }
                    if (emailExists){
                        Toast.makeText(applicationContext,"Adresse mail déjà utilisée", Toast.LENGTH_LONG).show()
                    }else{
                        val u=User(
                            0,binding.etRegisterEmail.text.toString(),
                            binding.etRegisterPwd.text.toString(),
                            rights
                        )
                        AsyncTask.execute{
                            val db = Room.databaseBuilder(
                                applicationContext,
                                MyDB::class.java, "MyDataBase"
                            ).build()
                            val dao = db.userDao()
                            val uIn = UserRecord(0,u.email,u.pwd,u.rights)
                            dao.insertUser(uIn)
                        }
                        val str=binding.etRegisterEmail.text.toString()+"#"+binding.etRegisterPwd.text.toString()
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
                        when(rights){
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