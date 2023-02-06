package pacocovesgarcia.iessochoa.examen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import pacocovesgarcia.iessochoa.examen.databinding.FragmentTareaaBinding
import pacocovesgarcia.iessochoa.examen.model.Tarea
import pacocovesgarcia.iessochoa.examen.viewmodel.AppViewModel

class TareaFragment : Fragment() {

    private var _binding: FragmentTareaaBinding? = null
    val args: TareaFragmentArgs by navArgs()
    private val viewModel: AppViewModel by activityViewModels()
    //será una tarea nueva si no hay argumento
    val esNuevo by lazy { args.tarea==null }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTareaaBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iniciaFavorito()
        iniciaFitness()
        ComprobarDatos()
        //si es nueva tarea o es una edicion
        if (esNuevo)//nueva tarea
        //cambiamos el título de la ventana
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "Nueva tarea"
        else
            iniciaTarea(args.tarea!!)

    }

    private fun ComprobarDatos() {
        binding.fabGuardar.setOnClickListener{
            if (binding.tvNombreDato.text.toString().isEmpty() || binding.tvFechaDato.text.toString().isEmpty())
                muestraMensajeError()
            else
                guardaTarea()
        }
    }

    private fun muestraMensajeError(){
        Snackbar.make(binding.root,"Es necesario rellenar todos los campos", Snackbar.LENGTH_LONG)
            .setAction("Action",null).show()
    }

    private fun guardaTarea() {
        //recuperamos los datos
        val nombre=binding.tvNombreDato.text.toString()
        val fecha=binding.tvFechaDato.text.toString()
        val fitness=binding.rbFitnessDato.isChecked
        val favorito=binding.rbFavoritoDato.isChecked

        //creamos la tarea: si es nueva, generamos un id, en otro caso le asignamos su id
        val tarea = if(esNuevo)
            Tarea(nombre,fecha,fitness,favorito)
        else
            Tarea(args.tarea!!.id,nombre,fecha,fitness,favorito)
        //guardamos la tarea desde el viewmodel
        viewModel.addTarea(tarea)
        //salimos de editarFragment
        findNavController().popBackStack()
    }

    private fun iniciaFavorito() {
        binding.rbFavoritoDato.setOnCheckedChangeListener { _, isChecked ->
            //cambiamos el icono si está marcado o no el switch
            //asignamos la imagen desde recursos
            binding.rbFavoritoDato.isChecked=true
        }
        //iniciamos a valor false
        binding.rbFavoritoDato.isChecked=false
    }

    private fun iniciaFitness() {
        binding.rbFitnessDato.setOnCheckedChangeListener { _, isChecked ->
            //cambiamos el icono si está marcado o no el switch
            //asignamos la imagen desde recursos
            binding.rbFitnessDato.isChecked=true
        }
        //iniciamos a valor false
        binding.rbFitnessDato.isChecked=false
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Carga los valores de la tarea a editar
     */
    private fun iniciaTarea(tarea: Tarea) {
        binding.rbFavoritoDato.isChecked = tarea.Favorito
        binding.rbFitnessDato.isChecked = tarea.Fitness
        binding.tvNombreDato.text= tarea.Nommbre
        binding.tvFechaDato.text = tarea.Fecha
        //cambiamos el título
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Tarea ${tarea.id}"
    }
}