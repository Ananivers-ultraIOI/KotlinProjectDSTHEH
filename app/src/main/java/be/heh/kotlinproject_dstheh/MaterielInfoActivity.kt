package be.heh.kotlinproject_dstheh

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.heh.kotlindbsql.databinding.ActivityMainBinding
import be.heh.kotlindbsql.databinding.ActivityMaterielInfoBinding

class MaterielInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterielInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMaterielInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}