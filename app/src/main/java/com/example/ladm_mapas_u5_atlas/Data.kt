package com.example.ladm_mapas_u5_atlas

import com.google.firebase.firestore.GeoPoint

class Data {
    var nombreLugar: String="";
    var posicion1 : GeoPoint = GeoPoint(0.0,0.0)
    var posicion2 : GeoPoint = GeoPoint(0.0,0.0)
    var descripcionLugar=""
    var direccionLugar=""
    var imagenurl=""

    override fun toString(): String{
        return nombreLugar+"\n"+posicion1.latitude+','+posicion1.longitude+"\n"+
                posicion2.latitude+','+posicion2.longitude+"\n"+
                descripcionLugar+"\n"+direccionLugar+"\n"+imagenurl
    } //MetodotoString
    fun estoyEn(posicionActual: GeoPoint):Boolean{
        if(posicionActual.latitude>=posicion1.latitude &&
            posicionActual.latitude <= posicion2.latitude){
            if(invertir(posicionActual.longitude)>=invertir(posicion1.longitude) &&
                invertir(posicionActual.longitude)<= invertir(posicion2.longitude)){
                return true;
            }
        }
        return false
    }
    private fun invertir(valor:Double):Double{
        return valor*-1
    }
}