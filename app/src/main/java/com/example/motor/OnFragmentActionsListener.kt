package com.example.motor

import android.widget.ArrayAdapter

interface OnFragmentActionsListener {
    fun onBluetooth()
    fun offBluetooth()
    fun showDispositivos(): ArrayAdapter<String>
    fun conectarDispositivo(IntValSpin:Int)
    fun sendCommand(input:String)

}