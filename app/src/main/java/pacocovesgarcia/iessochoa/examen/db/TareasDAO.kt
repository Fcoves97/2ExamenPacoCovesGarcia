package pacocovesgarcia.iessochoa.examen.db

import androidx.lifecycle.LiveData
import androidx.room.*
import pacocovesgarcia.iessochoa.examen.model.Tarea

@Dao
interface TareasDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addTarea(tarea: Tarea)
    @Delete
    fun delTarea(tarea: Tarea)
    @Query("SELECT * FROM tareas ")
    fun getAllTareas(): LiveData<List<Tarea>>
    @Query("SELECT * FROM tareas WHERE Fitness= :fitness")
    fun getTareasFiltroSinPagar(fitness:Boolean):LiveData<List<Tarea>>
}