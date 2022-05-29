package com.example.ladm_mapas_u5_atlas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ladm_mapas_u5_atlas.databinding.ActivityLugaresBinding
import com.example.ladm_mapas_u5_atlas.databinding.ActivityMapsBinding
import com.google.common.collect.Maps

class LugaresActivity : AppCompatActivity() {
    lateinit var binding: ActivityLugaresBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLugaresBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var extra = intent.extras
        var nombre= extra!!.getString("nombreLugar")
        var descrip=extra!!.getString("descripcLugar")
        var direcl=extra!!.getString("direclugar")
        var imgruta=extra!!.getString("imagLugar")

        binding.txtNombre.setText(nombre)
        binding.txtDescripcion.setText(descrip)
        binding.txtDireccion.setText(direcl)

        if(imgruta=="imgtejuino"){
            binding.imagenLugares.setImageResource(R.drawable.tejuino)
        }else if(imgruta=="imgloncheria"){
            binding.imagenLugares.setImageResource(R.drawable.loncheria)
        }else if(imgruta=="imgparroquia"){
            binding.imagenLugares.setImageResource(R.drawable.parroquia)
        }else if(imgruta=="imgplazab"){
            binding.imagenLugares.setImageResource(R.drawable.plaza)
        }else if(imgruta=="imgcremeria"){
            binding.imagenLugares.setImageResource(R.drawable.cremeria)
        }else if(imgruta=="imgfloreria"){
            binding.imagenLugares.setImageResource(R.drawable.floreria)
        }else if(imgruta=="imgtortas"){
            binding.imagenLugares.setImageResource(R.drawable.tortas)
        }else if(imgruta=="imgarcos"){
            binding.imagenLugares.setImageResource(R.drawable.losarcos)
        }
        binding.btnRegresar.setOnClickListener {
            val intent = Intent(this, MapsActivity ::class.java)
            startActivity(intent)
        }
    }
}