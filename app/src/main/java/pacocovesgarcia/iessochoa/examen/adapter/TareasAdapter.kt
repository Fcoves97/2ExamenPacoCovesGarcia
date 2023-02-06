package pacocovesgarcia.iessochoa.examen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pacocovesgarcia.iessochoa.examen.databinding.FragmentTareaaBinding
import pacocovesgarcia.iessochoa.examen.model.Tarea

class TareasAdapter(): RecyclerView.Adapter<TareasAdapter.TareaViewHolder>()
{
    var onTareaClickListener:OnTareaClickListener?=null
    var listaTareas: List<Tarea>?=null

    fun setLista(lista:List<Tarea>){
        listaTareas=lista
        //notifica al adaptador que hay cambios y tiene que redibujar el ReciclerView
        notifyDataSetChanged()
    }
    inner class TareaViewHolder(val binding: FragmentTareaaBinding)
        : RecyclerView.ViewHolder(binding.root){
            init {
                //inicio del click de icono borrar
                binding.fabGuardar.setOnClickListener(){
                    //recuperamos la tarea de la lista
                    val tarea= listaTareas?.get(this.adapterPosition)
                    //llamamos al evento borrar que estará definido en el fragment
                    onTareaClickListener?.onTareaBorrarClick(tarea)
                }
                //inicio del click sobre el Layout(constraintlayout)
                binding.root.setOnClickListener(){
                    val tarea= listaTareas?.get(this.adapterPosition)
                    onTareaClickListener?.onTareaClick(tarea)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            TareaViewHolder {
        //utilizamos binding, en otro caso hay que indicar el item.xml. Para más detalles puedes verlo en la documentación
        val binding = FragmentTareaaBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }

    override fun onBindViewHolder(tareaViewHolder: TareaViewHolder, pos:
    Int) {
        //Nos pasan la posición del item a mostrar en el viewHolder
        with(tareaViewHolder) {
            //cogemos la tarea a mostrar y rellenamos los campos del ViewHolder
            with(listaTareas!!.get(pos)) {
                binding.tvNombreDato.text = Nommbre
                binding.tvFechaDato.text = Fecha
                binding.rbFavoritoDato.isChecked = Favorito
            }
        }
    }

    override fun getItemCount(): Int = listaTareas?.size?:0

    interface OnTareaClickListener{
        //editar tarea que contiene el ViewHolder
        fun onTareaClick(tarea:Tarea?)
        //borrar tarea que contiene el ViewHolder
        fun onTareaBorrarClick(tarea:Tarea?)
    }
}