package be.heh.kotlinproject_dstheh

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import be.heh.kotlindbsql.databinding.ActivityQrBinding

class QrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs_datas= PreferenceManager.getDefaultSharedPreferences((applicationContext))
        val mode = prefs_datas?.getString("mode", "null") ?: "default_value"
        println(mode)
    }
}