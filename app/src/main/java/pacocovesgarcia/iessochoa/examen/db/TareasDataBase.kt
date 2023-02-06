package pacocovesgarcia.iessochoa.examen.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pacocovesgarcia.iessochoa.examen.model.Tarea
import kotlin.random.Random

@Database(entities = [Tarea::class], version = 1, exportSchema = false)
public abstract class TareasDataBase : RoomDatabase() {
    abstract fun tareasDao(): TareasDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: TareasDataBase? = null

        fun getDatabase(context: Context): TareasDataBase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TareasDataBase::class.java,
                    "tareas_database")
                    //.addCallback(InicioDbCallback())
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    //***************CallBack******************************
    /**
     * Permite iniciar la base de datos con Tareas
     */
    private class InicioDbCallback() : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                GlobalScope.launch {
                    cargarDatabase(database.tareasDao())
                }
            }
        }
        //Iniciamos la base de datos con Tareas de ejemplo
        suspend fun cargarDatabase(tareasDao: TareasDAO) {
            val tecnicos = listOf(
                "Paco",
                "Luis",
                "Juan",
                "Javier",
                "Felipe"
            )
            val fecha = listOf(
                "28/01/2023",
                "27/01/2023",
                "29/01/2023",
                "30/01/2023",
                "26/01/2023"
            )
            lateinit var tarea: Tarea
            (1..10).forEach {
                // Thread.sleep(2000)
                tarea = Tarea(
                    tecnicos.random(),
                    fecha.random(),
                    Random.nextBoolean(),
                    Random.nextBoolean()
                )
                tareasDao.addTarea(tarea)
            }
        }
    }
}
