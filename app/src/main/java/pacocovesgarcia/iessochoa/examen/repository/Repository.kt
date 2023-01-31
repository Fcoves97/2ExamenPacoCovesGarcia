package pacocovesgarcia.iessochoa.examen.repository

import android.app.Application
import android.content.Context
import pacocovesgarcia.iessochoa.examen.db.TareasDAO
import pacocovesgarcia.iessochoa.examen.db.TareasDataBase
import pacocovesgarcia.iessochoa.examen.model.Tarea


object Repository {
    //instancia al modelo
    //private lateinit var modelTareas:ModelTempTareas
    private lateinit var modelTareas: TareasDAO
    //el context suele ser necesario para recuperar datos
    private lateinit var application: Application
    //inicio del objeto singleton
    operator fun invoke(context: Context){
        this.application= context.applicationContext as Application
        //iniciamos el modelo
        //ModelTempTareas(application)
        //modelTareas = ModelTempTareas

        modelTareas= TareasDataBase.getDatabase(application).tareasDao()
    }
    //Métodos que llaman a los otros métodos
    fun addTarea(tarea: Tarea)= modelTareas.addTarea(tarea)
    fun delTarea(tarea: Tarea)= modelTareas.delTarea(tarea)
    fun getAllTareas()= modelTareas.getAllTareas()
    fun getTareasFiltroSinPagar (soloSinPagar:Boolean)= modelTareas.getTareasFiltroSinPagar(soloSinPagar)
}