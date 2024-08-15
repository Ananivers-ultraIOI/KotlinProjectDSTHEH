package be.heh.kotlinproject_dstheh

import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.KeyEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import be.heh.kotlindbsql.R
import be.heh.kotlindbsql.databinding.ActivityWebviewBinding

class WebviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebviewBinding
    var web=""
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs_datas= PreferenceManager.getDefaultSharedPreferences((applicationContext))
        web=prefs_datas?.getString("web", "url") ?: "default_value"
        binding.wvWebviewStart.loadUrl(web)
        binding.wvWebviewStart.settings.javaScriptEnabled=true
        binding.wvWebviewStart.webViewClient=object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean{
                view!!.loadUrl(request!!.url.toString())
                return true
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.wvWebviewStart.canGoBack()){
            binding.wvWebviewStart.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}