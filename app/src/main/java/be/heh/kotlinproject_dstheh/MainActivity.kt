package be.heh.kotlinproject_dstheh

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import be.heh.kotlindbsql.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun onMainClickManager(v:View){
        when(v.id){
            binding.btMainConnexion.id -> Toast.makeText(applicationContext,"connexion",Toast.LENGTH_LONG).show()
            binding.tvMainRegister.id -> Toast.makeText(applicationContext,"register",Toast.LENGTH_LONG).show()
        }
    }
}
