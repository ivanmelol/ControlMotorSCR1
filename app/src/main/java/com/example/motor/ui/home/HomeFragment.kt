package com.example.motor.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.motor.OnFragmentActionsListener
import com.example.motor.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var listener: OnFragmentActionsListener? =null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentActionsListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    public var angulo:Int=0
    public var Vmean:String="0.0"
    public var Vrms:String="0.0"
    public var Vel:String="0.0"

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textHome
        val alfa: TextView =binding.alfa //vincular el XML del text de alfa con kotlin
        val buttonOn: Button = binding.buttonOn //Vincular el XML del boton ON con kotlin
        val buttonOff: Button = binding.buttonOff // Vincular el boton de OFF con kotlin
        val AlfaControl: SeekBar = binding.AlfaControl // Vincular el XML del Slider con Kotlin
        val textVMEAN: TextView =binding.textVMEAN //Vincular el XML del texto de Voltaje medio con Kotlin
        val textVRMS: TextView = binding.textVRMS //Vincular el XML del texto de voltaje RMS con Kotlin
        val textVEL: TextView =binding.textVEL //Vincular el XML del texto de velocidad con kotlin
        AlfaControl.isEnabled=false //Setear por defecto el estado del Seek en disable
        buttonOff.isEnabled=false
        buttonOn.isEnabled=true

        //val buttonON = findViewById(R.id.buttonON) as Button
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        //Evento click del boton ON
        buttonOn.setOnClickListener{
            Toast.makeText(context,"Angulo de disparo en 10 Grados, para arranque de poca corriente", Toast.LENGTH_SHORT).show()
            angulo=10
            AlfaControl.progress=angulo
            AlfaControl.isEnabled=true
            alfa.text=angulo.toString()
            listener?.sendCommand("alfa<${angulo.toString()}>")
            buttonOff.isEnabled=true
            buttonOn.isEnabled=false
        }

        //Metodo para setear el valor de voltaje medio
        fun setVmean(valor: String){
            textVMEAN.text=valor
        }

        //Metodo para setear el valor de voltaje RMS
        fun setVrms(valor: String){
            textVRMS.text=valor
        }

        //Metodo para setear el valor de voltaje RMS
        fun setVelocidad(valor: String){
            textVEL.text=valor
        }



        //Evento click del boton OFF
        buttonOff.setOnClickListener{
            Toast.makeText(context,"Angulo de disparo 0 para apagar el motor", Toast.LENGTH_SHORT).show()
            this.angulo=0
            AlfaControl.progress=angulo
            AlfaControl.isEnabled=false
            listener?.sendCommand("alfa<${angulo.toString()}>")
            alfa.text=angulo.toString()
            buttonOff.isEnabled=false
            buttonOn.isEnabled=true
        }


        AlfaControl.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Aquí puedes realizar acciones según el progreso cambie
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Acciones cuando se comienza a arrastrar el SeekBar
            }

            // Acciones cuando se detiene de arrastrar el SeekBar
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar?.progress ?: 0 // Obtener el progreso del SeekBar, si es null asignar 0
                Toast.makeText(context,"Angulo Alfa: $progress", Toast.LENGTH_SHORT).show()
                angulo=progress
                listener?.sendCommand("alfa<${angulo.toString()}>")
                alfa.text=angulo.toString()
            }
        })




        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

