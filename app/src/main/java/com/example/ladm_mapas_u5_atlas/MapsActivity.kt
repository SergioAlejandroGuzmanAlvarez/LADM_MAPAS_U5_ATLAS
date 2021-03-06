package com.example.ladm_mapas_u5_atlas

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ladm_mapas_u5_atlas.databinding.ActivityMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var baseRemota = FirebaseFirestore.getInstance()
    var posicion = ArrayList<Data>()
    private lateinit var locacion: LocationManager
    var listaID = ArrayList<String>()
    var resultado = "";
    var pos=0
    var nombLugar=""
    var descripLugar=""
    var direccLugar=""
    var imgurlLugar=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        //Inicio de c??digos
        // Aqui hacemos los permisos para permitir la ubicaci??n

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 1
            )
        }
        //Hacemos una consulta de la base datos guardando la posici??n de cada ID mas aparte guardar todo en un arreglo
        baseRemota.collection("xaliscobd")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    //binding.txtUbicacion.setText("ERROR: " + firebaseFirestoreException.message)
                    AlertDialog.Builder(this)
                        .setMessage("ERROR: " + firebaseFirestoreException.message)
                        .setPositiveButton("OK"){p,q-> }
                        .show()
                    return@addSnapshotListener
                }
                listaID.clear()
                posicion.clear()
                for (document in querySnapshot!!) {
                    var data = Data();
                    data.nombreLugar = document.getString("nombreLugar").toString()
                    data.posicion1 = document.getGeoPoint("posicion1")!!
                    data.posicion2 = document.getGeoPoint("posicion2")!!
                    data.descripcionLugar = document.getString("descripcionLugar").toString()
                    data.direccionLugar = document.getString("direccionLugar").toString()
                    data.imagenurl = document.get("imagenLugar").toString()
                    resultado += data.toString() + "\n\n"
                    listaID.add(document.id)
                    posicion.add(data)
                }//for
                //binding.txtUbicacion.setText(resultado)
                /*AlertDialog.Builder(this)
                    .setTitle("DATA DE LUGARES >> XALISCO")
                    .setMessage(resultado)
                    .setPositiveButton("ACEPTAR"){p,q-> }
                    .show()*/
            }// Llamada a la colecci??n
        locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var oyente = Oyente(this)
        locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,01f,oyente)

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
        mMap = googleMap
        //Colocamos la vista principal en el Centro de Xalisco, poniendole un ZOOM para evitar que aparezca desde lejos
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(21.444761, -104.899832),16f),3000,null )
        marcadoresMaps()
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled=true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.acerca->{
                AlertDialog.Builder(this)
                    .setTitle("Laboratorio de Aplicaciones de Dispositivos M??viles.")
                    .setMessage("Integrantes:\n - Guzman Alvarez Sergio Alejandro\n Canales Almendares Cristopher David")
                    .setPositiveButton("OK"){p,q-> }
                    .show()
            }
            R.id.data->{
                AlertDialog.Builder(this)
                    .setTitle("DATA DE LUGARES >> XALISCO")
                    .setMessage(resultado)
                    .setPositiveButton("ACEPTAR"){p,q-> }
                    .show()
            }
            R.id.lugares->{
                AlertDialog.Builder(this)
                    .setTitle("INGENIEROS TRABAJANDO")
                    .setMessage(resultado)
                    .setPositiveButton("OK"){p,q-> }
                    .show()
            }
            R.id.proyecto->{
                AlertDialog.Builder(this)
                    .setTitle("Proyecto ATLAS.")
                    .setMessage("ATLAS consiste en ser una aplicaci??n de guia para el turista promedio, que quiera visitar la regi??n de XALISCO.")
                    .setPositiveButton("OK"){p,q-> }
                    .show()
            }
            R.id.miubi->{
                miUbicacion()
            }
            R.id.salir->{
                System.exit(0)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menuoculto,menu)
        return true
    }

    private fun marcadoresMaps(){
        val tejuinoPlaza=MarkerOptions().position(LatLng(21.443895, -104.899367)).title("Tejuino - La Plaza")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            .snippet("Zona de bebidas")
            .flat(true).rotation(0f)
        val parroquia=MarkerOptions().position(LatLng(21.444244, -104.899205)).title("Parroquia de San Cayetano")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            .snippet("Iglesias")
            .flat(true).rotation(0f)
        val plazajuarez=MarkerOptions().position(LatLng(21.444697, -104.900428)).title("Plaza p??blica Benito Ju??rez")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            .snippet("Centro p??blico")
            .flat(true).rotation(0f)
        val tortas=MarkerOptions().position(LatLng(21.445016, -104.899771)).title("TORTAS > De la gas < ")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            .snippet("Restaurantes")
            .flat(true).rotation(0f)
        val loncheria=MarkerOptions().position(LatLng(21.443952, -104.900448)).title("LONCHERIA > Lety < ")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            .snippet("Restaurantes")
            .flat(true).rotation(0f)
        val floreria=MarkerOptions().position(LatLng(21.443099, -104.899912)).title("FLORERIA > Hawaii < ")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
            .snippet("Florerias")
            .flat(true).rotation(0f)
        val cremeria=MarkerOptions().position(LatLng(21.444887, -104.900451)).title("Cremeria > Mart??nez < ")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
            .snippet("Cremerias")
            .flat(true).rotation(0f)
        val arcos=MarkerOptions().position(LatLng(21.444760, -104.900608)).title("Los Arcos ")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            .snippet("Centro comercial")
            .flat(true).rotation(0f)
        mMap.addMarker(tejuinoPlaza)
        mMap.addMarker(parroquia)
        mMap.addMarker(plazajuarez)
        mMap.addMarker(tortas)
        mMap.addMarker(loncheria)
        mMap.addMarker(floreria)
        mMap.addMarker(cremeria)
        mMap.addMarker(arcos)
    }
    private fun miUbicacion(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        LocationServices.getFusedLocationProviderClient(this)
            .lastLocation.addOnSuccessListener {
                var geoPosicion = GeoPoint(it.latitude, it.longitude)
                AlertDialog.Builder(this)
                    .setMessage("Altitud: ${it.latitude}, Latitud: ${it.longitude}")
                    .setPositiveButton("OK"){p,q->}
                    .show()
                for(item in posicion){
                    if(item.estoyEn(geoPosicion)){
                        AlertDialog.Builder(this)
                            .setMessage("Usted se encuentra actualmente en: "+item.nombreLugar)
                            .setTitle("ATENCION")
                            .setPositiveButton("OK"){p,q->}
                            .show()
                    }
                }//for
            }.addOnFailureListener {
                AlertDialog.Builder(this)
                    .setMessage("No se pudo encontrar su ubicacion")
                    .setTitle("ERROR")
                    .setPositiveButton("OK"){p,q->}
                    .show()
            }
    }//Metodo miUbicacion
    fun guardarDatos(nomb:String,descripc:String,direcl:String,imgurll:String){
        nombLugar=nomb
        descripLugar=descripc
        direccLugar=direcl
        imgurlLugar=imgurll
        val intent = Intent(this, LugaresActivity ::class.java)
        intent.putExtra("nombreLugar",nombLugar)
        intent.putExtra("descripcLugar",descripLugar)
        intent.putExtra("direclugar",direccLugar)
        intent.putExtra("imagLugar",imgurlLugar)
        startActivity(intent)
    }
    class Oyente(puntero:MapsActivity) : LocationListener {
        var p = puntero
        override fun onLocationChanged(l: Location) {
            var geoPosicionGPS = GeoPoint(l.latitude,l.longitude)
            Toast.makeText(p,"Te encuentras en las coordenadas: \n${l.latitude}, ${l.longitude}", Toast.LENGTH_LONG).show()
            for(item in p.posicion){
                if(item.estoyEn(geoPosicionGPS)){
                    AlertDialog.Builder(p)
                        .setTitle("ATENCI??N.")
                        .setMessage("Actualmente usted se encuentra en : ${item.nombreLugar}  (onLocationChanged)\n ??Desea saber m??s informaci??n?")
                        .setPositiveButton("OK"){d,i->
                            p.guardarDatos(item.nombreLugar,item.descripcionLugar,item.direccionLugar,item.imagenurl)
                        }
                        .setNegativeButton("NO"){d,i->}
                        .show()
                }
            }
        }
    }
}