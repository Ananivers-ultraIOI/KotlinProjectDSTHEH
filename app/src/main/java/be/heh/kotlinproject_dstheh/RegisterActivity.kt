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
                        val dbL = dao?.get()
                        println(dbL)
                    }
                }
            }
        }
    }
}