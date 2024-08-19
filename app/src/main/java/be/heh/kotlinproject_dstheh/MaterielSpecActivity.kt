package be.heh.kotlinproject_dstheh

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import be.heh.kotlindbsql.R
import be.heh.kotlindbsql.databinding.ActivityMainBinding
import be.heh.kotlindbsql.databinding.ActivityMaterielSpecBinding
import be.heh.kotlinproject_dstheh.db.MyDB
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MaterielSpecActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMaterielSpecBinding
    var account=""
    var idpass=0
    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMaterielSpecBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val prefs_datas= PreferenceManager.getDefaultSharedPreferences((applicationContext))
        account = prefs_datas?.getString("account", "null") ?: "default_value"
        idpass = (prefs_datas?.getString("id", "null") ?: "default_value").toInt()
        GlobalScope.launch(Dispatchers.Main) {
            val materials = withContext(Dispatchers.IO) {
                val db = Room.databaseBuilder(
                    applicationContext,
                    MyDB::class.java, "MyDataBase"
                ).build()
                val dao = db.materialsDao()
                dao.get(idpass)
            }
            val table = binding.tableLayout
            table.removeAllViews()
            val typeRow = TableRow(this@MaterielSpecActivity)
            val idRow = TableRow(this@MaterielSpecActivity)
            val headerId = TextView(this@MaterielSpecActivity).apply {
                text = "ID : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val idView = TextView(this@MaterielSpecActivity).apply {
                text = materials.id.toString()
                setTextAppearance(R.style.TableText)
            }
            idRow.addView(headerId)
            idRow.addView(idView)
            table.addView(idRow)
            val headerType = TextView(this@MaterielSpecActivity).apply {
                text = "Type : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val typeView = TextView(this@MaterielSpecActivity).apply {
                text = materials.type
                setTextAppearance(R.style.TableText)
            }
            typeRow.addView(headerType)
            typeRow.addView(typeView)
            table.addView(typeRow)
            val modelRow = TableRow(this@MaterielSpecActivity)
            val headerModel = TextView(this@MaterielSpecActivity).apply {
                text = "Modèle : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val modelView = TextView(this@MaterielSpecActivity).apply {
                text = materials.modele
                setTextAppearance(R.style.TableText)
            }
            modelRow.addView(headerModel)
            modelRow.addView(modelView)
            table.addView(modelRow)
            val qantityRow = TableRow(this@MaterielSpecActivity)
            val headerQuantite = TextView(this@MaterielSpecActivity).apply {
                text = "Quantitée : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val quantiteView = TextView(this@MaterielSpecActivity).apply {
                text = materials.quantity.toString()
                setTextAppearance(R.style.TableText)
            }
            qantityRow.addView(headerQuantite)
            qantityRow.addView(quantiteView)
            table.addView(qantityRow)
            val sitewebRow = TableRow(this@MaterielSpecActivity)
            val headerSiteweb = TextView(this@MaterielSpecActivity).apply {
                text = "Site Web : "
                setTextAppearance(R.style.TableHeaderText)
            }
            val webButton = Button(this@MaterielSpecActivity).apply {
                text = "Vers le site"
                setTextAppearance(R.style.DeleteButton)
                setOnClickListener {
                    val editeur_datas = prefs_datas!!.edit()
                    editeur_datas.putString("web",materials.website)
                    editeur_datas.commit()
                    val intent=Intent(this@MaterielSpecActivity,WebviewActivity::class.java)
                    startActivity(intent)
                }
            }
            sitewebRow.addView(headerSiteweb)
            sitewebRow.addView(webButton)
            table.addView(sitewebRow)
            val str =materials.website+"\n#"+materials.id.toString()+"#"+materials.type+"#"+materials.modele+"#"+materials.quantity.toString()
            val ivQrCode=findViewById<ImageView>(R.id.ivQRCode)
            val bitmap=generateQRCode(str)
            ivQrCode.setImageBitmap(bitmap)
        }
    }
    fun generateQRCode(text: String): Bitmap? {
        val width = 500
        val height = 500
        val bitMatrix: BitMatrix = try {
            MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, null)
        } catch (e: IllegalArgumentException) {
            return null
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        return bitmap
    }
    fun onMSpecClickManager(v:View){
        when(v.id){
            binding.btMspecMinfo.id->comeBack()
        }
    }
    fun comeBack(){
        when(account){
            "R"-> toR()
            "RW"->toRW()
            "SA"->toSA()
        }
    }
    fun toR(){
        val iMa= Intent(this,MaterielInfoActivity::class.java)
        startActivity(iMa)
    }
    fun toRW(){
        val iMa= Intent(this,MaterielInfoRwActivity::class.java)
        startActivity(iMa)
    }
    fun toSA(){
        val iMa= Intent(this,MaterielInfoSuperadminActivity::class.java)
        startActivity(iMa)
    }
}