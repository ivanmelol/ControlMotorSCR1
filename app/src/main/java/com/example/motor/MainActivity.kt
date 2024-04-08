package com.example.motor

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.motor.databinding.ActivityMainBinding

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import java.io.IOException
import java.util.*
import androidx.activity.result.ActivityResultLauncher

const val REQUEST_ENABLE_BT=1

class MainActivity : AppCompatActivity(), OnFragmentActionsListener {


    //Bluetooth Adapter
    lateinit var mBtAdapter: BluetoothAdapter
    var mAddressDevices: ArrayAdapter<String>?=null
    var mNameDevices: ArrayAdapter<String>?=null

    private var listener2: DispositivosCallback?=null


    companion object{
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private var m_bluetoothSocket: BluetoothSocket?=null

        var m_isConnected:Boolean=false
        lateinit var m_address: String
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        mAddressDevices= ArrayAdapter(this, android.R.layout.simple_list_item_1)
        mNameDevices=ArrayAdapter(this, android.R.layout.simple_list_item_1)

        //Inicializacion del bluettoth adapter
        mBtAdapter= (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        //Revisar si encendido o apagado el Bluetooth
        if(mBtAdapter==null){
            Toast.makeText(this, "EL Bluetooth no esta disponible en este dispositivo", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "El Bluetooth esta disponible en este dispositivo", Toast.LENGTH_LONG).show()
        }


    }

    //Encender el bluetooth
    override fun onBluetooth() {
        //Toast.makeText(this, "Hola OSIS COSIS PEREZ_OSIS", Toast.LENGTH_LONG).show()
        if(mBtAdapter.isEnabled){
            //Si ua esta activo
            Toast.makeText(this, "Bluetooht ya se encuentra activado", Toast.LENGTH_LONG).show()
        }else{
            //Encender el Bluetooth
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)!=PackageManager.PERMISSION_GRANTED){
                Log.i("MainActivity","ActivityCampat#requestPermissions")
            }
            //someActivityResultLauncher.launch(enableBtIntent)
        }
    }

    //Apagar el bluetooth
    override fun offBluetooth() {
        if(!mBtAdapter.isEnabled){
            //Si ya esta desactivado
            Toast.makeText(this, "Bluetooht ya se encuentra desactivado", Toast.LENGTH_LONG).show()
        }else{
            //Encender el bluetooth
            mBtAdapter.disable()
            Toast.makeText(this, "Se ha desactivado el Bluetooht", Toast.LENGTH_LONG).show()
        }
    }

    //Mostra la lista de dispositivos
    override fun showDispositivos(): ArrayAdapter<String>  {

        if(mBtAdapter.isEnabled){
            val pairedDevices: Set<BluetoothDevice>?= mBtAdapter?.bondedDevices
            mAddressDevices!!.clear()
            mNameDevices!!.clear()
            var aux=""
            pairedDevices?.forEach{device->
                val deviceName=device.name
                aux=deviceName
                val deviceHarwareAddress=device.address //MAC adress
                mAddressDevices!!.add(deviceHarwareAddress)
                mNameDevices!!.add(deviceName)
            }
            //Toast.makeText(this, "bluetooth $aux", Toast.LENGTH_LONG).show()
            //listener2?.onDispositivosReady(mNameDevices!!)
            return mNameDevices!!

        }else{
            val noDevices="Ningun dispositivo pudo ser emparejado"
            mAddressDevices!!.add(noDevices)
            mNameDevices!!.add(noDevices)
            Toast.makeText(this, "Primero activie el bluetooth", Toast.LENGTH_LONG).show()
            return mNameDevices!!
        }
    }

    override fun conectarDispositivo(IntValSpin: Int) {
        try{
         if(m_bluetoothSocket==null || !m_isConnected){
             m_address=mAddressDevices!!.getItem(IntValSpin).toString()
             Toast.makeText(this, m_address, Toast.LENGTH_LONG).show()
             //Cancel discovery
             mBtAdapter?.cancelDiscovery()
             val device: BluetoothDevice = mBtAdapter.getRemoteDevice(m_address)
             m_bluetoothSocket=device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
             m_bluetoothSocket!!.connect()
         }
            Toast.makeText(this, "CONEXION EXITOSA", Toast.LENGTH_LONG).show()
            Log.i("MainActivity", "CONEXION EXITOSA")


        }catch (e: IOException){
            //Conexxion no satisfactoria
            e.printStackTrace()
            Toast.makeText(this, "ERROR CONEXION", Toast.LENGTH_LONG).show()
            Log.i("MainActivity", "ERROR CONEXION")

        }
    }


    override fun sendCommand(input: String) {
        if(m_bluetoothSocket!=null){
            try{
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
}