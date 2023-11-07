package ie.setu.museum.views.museumHome

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.museum.databinding.CardMuseumBinding
import ie.setu.museum.models.MuseumModel

interface MuseumListener {
    fun onMuseumClick(museum: MuseumModel, position: Int)
}

class MuseumAdapter constructor( private var museums: List<MuseumModel>, private val listener: MuseumListener) :
    RecyclerView.Adapter<MuseumAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMuseumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val museum = museums[holder.adapterPosition]
        holder.bind(museum,listener)
    }

    override fun getItemCount(): Int = museums.size


    class MainHolder(private val binding : CardMuseumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(museum: MuseumModel, listener: MuseumListener) {
            binding.name.text = museum.name
            binding.description.text = museum.shortDescription
            binding.category.text = museum.category
            Picasso.get()
                .load(museum.image)
                .resize(200,200)
                .into(binding.imageIcon)
            binding.root.setOnClickListener{ listener.onMuseumClick(museum,adapterPosition) }
        }
    }
}