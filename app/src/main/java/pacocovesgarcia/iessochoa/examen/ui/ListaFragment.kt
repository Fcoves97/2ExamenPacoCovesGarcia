package pacocovesgarcia.iessochoa.examen.ui

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pacocovesgarcia.iessochoa.examen.R
import pacocovesgarcia.iessochoa.examen.adapter.TareasAdapter
import pacocovesgarcia.iessochoa.examen.databinding.ListaFragmentBinding
import pacocovesgarcia.iessochoa.examen.model.Tarea
import pacocovesgarcia.iessochoa.examen.viewmodel.AppViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ListaFragment : Fragment() {

    private var _binding: ListaFragmentBinding? = null
    private val viewModel: AppViewModel by activityViewModels()
    private lateinit var tareasAdapter: TareasAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = ListaFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniciaRecyclerView()
        iniciaFiltros()
        iniciaCRUD()

        binding.fabNuevo.setOnClickListener {
            findNavController().navigate(R.id.action_listaFragment_to_tareaFragment)
        }

        viewModel.tareasLiveData.observe(viewLifecycleOwner, Observer<List<Tarea>> { lista ->
            tareasAdapter.setLista(lista)
        })

        binding.rbFitness.setOnCheckedChangeListener { _, i ->

            val estado:Boolean;
            if(binding.rbFitness.isChecked)
            {
                estado = true
            }
            else{
                estado = false
            }
            viewModel.setSoloSinFitness(estado)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun iniciaFiltros(){
        binding.rbFitness.setOnCheckedChangeListener( ) { _,isChecked->
            //actualiza el LiveData SoloSinPagarLiveData que a su vez modifica tareasLiveData
            //mediante el Transformation
            viewModel.setSoloSinFitness(isChecked)}
    }

    private fun iniciaRecyclerView() {
        //creamos el adaptador
        tareasAdapter = TareasAdapter()

        with(binding.rvTareas) {
            //Creamos el layoutManager
            layoutManager = LinearLayoutManager(activity)
            //le asignamos el adaptador
            adapter = tareasAdapter
        }
    }

    private fun iniciaCRUD(){
        binding.fabNuevo.setOnClickListener{
            val action = ListaFragmentDirections.actionListaFragmentToTareaFragment(null)
            findNavController().navigate(action)
        }
        tareasAdapter.onTareaClickListener = object :
            TareasAdapter.OnTareaClickListener {
            //**************Editar Tarea*************
            override fun onTareaClick(tarea: Tarea?) {
                //creamos acción enviamos argumento la tarea para editarla
                val action = ListaFragmentDirections.actionListaFragmentToTareaFragment(tarea)
                findNavController().navigate(action)
            }
            //***********Borrar Tarea************
            override fun onTareaBorrarClick(tarea: Tarea?) {
                //borramos directamente la tarea
                //viewModel.delTarea(tarea!!)
                if (tarea != null) {
                    borrarTarea(tarea)
                }
            }
        }
    }

    fun borrarTarea(tarea:Tarea){
        AlertDialog.Builder(activity as Context)
            .setTitle(android.R.string.dialog_alert_title)
            //recuerda: todo el texto en string.xml
            .setMessage("Desea borrar la Tarea ${tarea.id}?")
            //acción si pulsa si
            .setPositiveButton(android.R.string.ok){v,_->
                //borramos la tarea
                viewModel.delTarea(tarea)
                //cerramos el dialogo
                v.dismiss()
            }
            //accion si pulsa no
            .setNegativeButton(android.R.string.cancel){v,_->v.dismiss()}
            .setCancelable(false)
            .create()
            .show()
    }
}