package com.example.motor.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.motor.DispositivosCallback
import com.example.motor.OnFragmentActionsListener
import com.example.motor.databinding.FragmentDashboardBinding
import java.io.IOException

class DashboardFragment : Fragment() {

    var mNameDevices: ArrayAdapter<String>?=null

    private var _binding: FragmentDashboardBinding? = null
    private var listener: OnFragmentActionsListener? =null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val idBtnOnBT: Button = binding.idBtnOnBT
        val idBtnOffBT:Button =binding.idBtnOffBT
        val idBtnDispBT:Button=binding.idBtnDispBT
        val idBtnConect:Button = binding.idBtnConect
        val idSpinDisp:Spinner = binding.idSpinDisp

        /*val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        //llamar al listening para que se ejecuete en el MAIN
        idBtnOnBT.setOnClickListener{
            listener?.onBluetooth()
        }

        //llamar al listening para que se ejjecute en el MAIN apagar el bluettoth
        idBtnOffBT.setOnClickListener{
            listener?.offBluetooth()
        }

        //Llamar el listinig para mostrar los dispositivos
        idBtnDispBT.setOnClickListener{
            this.mNameDevices=listener?.showDispositivos()
            idSpinDisp.setAdapter(this.mNameDevices)
        }

        //Realizar la conexion con el dispositivo
        idBtnConect.setOnClickListener{
            val IntValSpin=idSpinDisp.selectedItemPosition
            listener?.conectarDispositivo(IntValSpin)
        }


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}