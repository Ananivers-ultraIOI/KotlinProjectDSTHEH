package be.heh.kotlinproject_dstheh

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.heh.kotlindbsql.databinding.ActivityMainBinding
import be.heh.kotlindbsql.databinding.ActivityMaterielInfoBinding
import java.io.FileNotFoundException
import java.io.IOException

class MaterielInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterielInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMaterielInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
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