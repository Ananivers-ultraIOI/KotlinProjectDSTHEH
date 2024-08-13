package be.heh.kotlinproject_dstheh

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.heh.kotlindbsql.databinding.ActivityMainBinding
import be.heh.kotlindbsql.databinding.ActivityMaterielRegisterBinding

class MaterielRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterielRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMaterielRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}