package be.heh.kotlinproject_dstheh

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.heh.kotlindbsql.databinding.ActivityMainBinding
import be.heh.kotlindbsql.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun onRegisterClickManager(v: View){
        when(v.id){
            binding.btRegister.id -> Toast.makeText(applicationContext,"register", Toast.LENGTH_LONG).show()
            binding.tvRegisterConnexion.id -> toConnexion()
        }
    }
    fun toConnexion(){
        val iConnection= Intent(this,MainActivity::class.java)
        startActivity(iConnection)
    }
}