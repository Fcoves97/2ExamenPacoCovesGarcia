package pacocovesgarcia.iessochoa.examen.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pacocovesgarcia.iessochoa.examen.model.Tarea
import pacocovesgarcia.iessochoa.examen.repository.Repository

class AppViewModel(application: Application): AndroidViewModel(application) {

    private val repositorio: Repository
    //liveData de lista de tareas
    val tareasLiveData : LiveData<List<Tarea>>
    private val estado = MutableLiveData(3)
    //LiveData que cuando se modifique un filtro cambia el tareasLiveData
    val SOLO_SIN_FITNESS="SOLO_SIN_FITNESS"

    private val filtrosLiveData by lazy {//inicio tardío
        val mutableMap = mutableMapOf<String, Any?>(
            SOLO_SIN_FITNESS to false
        )
        MutableLiveData(mutableMap)
    }
    //creamos el LiveData de tipo Booleano. Repesenta nuestro filtro
    private val soloSinPagarLiveData= MutableLiveData(false)
    //inicio ViewModel
    init {
        //inicia repositorio
        Repository(getApplication<Application>().applicationContext)
        repositorio=Repository
        //tareasLiveData =repositorio.getAllTareas()
        /*tareasLiveData= Transformations.switchMap(soloSinPagarLiveData)
        {soloSinPagar->Repository.getTareasFiltroSinPagar(soloSinPagar)}*/
        /*tareasLiveData= Transformations.switchMap(estado)
        {estado->Repository.getTareasFiltroEstado(estado)}*/
        tareasLiveData=Transformations.switchMap(filtrosLiveData)
        { mapFiltro ->
            val AplicarFitness = mapFiltro!![SOLO_SIN_FITNESS] as Boolean
            //Devuelve el resultado del when
            when {//trae toda la lista de tareas
                (!AplicarFitness) ->
                    repositorio.getAllTareas()
                (AplicarFitness) ->
                    repositorio.getTareasFiltroSinPagar(
                        AplicarFitness
                    )//Filtra por ambos
                else ->
                    repositorio.getTareasFiltroSinPagar(AplicarFitness)
            }
        }
    }
    fun addTarea(tarea: Tarea) = viewModelScope.launch (Dispatchers.IO){
        Repository.addTarea(tarea)
    }
    fun delTarea(tarea: Tarea) = viewModelScope.launch (Dispatchers.IO){
        Repository.delTarea(tarea)
    }

    /**
     * Modifica el Map filtrosLiveData el elemento "SOLO_SIN_PAGAR"
     * que activará el Transformations de TareasLiveData
     */
    fun setSoloSinFitness(soloSinFitness: Boolean) {
        //recuperamos el map
        val mapa = filtrosLiveData.value
        //modificamos el filtro
        mapa!![SOLO_SIN_FITNESS] = soloSinFitness
        //activamos el LiveData
        filtrosLiveData.value = mapa
    }
}