package pacocovesgarcia.iessochoa.examen.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Entity(tableName = "tareas")
@Parcelize
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    var id:Long?=null,
    val Nommbre:String,
    val Fecha:String,
    val Fitness:Boolean,
    val Favorito:Boolean,
): Parcelable {
    constructor(
                Nombre:String,
                Fecha:String,
                Fitness: Boolean,
                Favorito: Boolean,
                ):this(null,Nombre,Fecha,Fitness,Favorito){}

    companion object{
        var idContador=1L
        private fun generateId():Long{
            return idContador++
        }
    }
    override fun equals(other: Any?):Boolean{
        return (other is Tarea)&&(this.id == other?.id)
    }
}
