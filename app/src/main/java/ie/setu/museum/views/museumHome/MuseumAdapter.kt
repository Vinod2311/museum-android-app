package ie.setu.museum.views.museumHome

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.museum.databinding.CardMuseumBinding
import ie.setu.museum.models.museum.MuseumModel
import java.util.Locale

interface MuseumListener {
    fun onMuseumClick(museum: MuseumModel)
    fun onEditMuseumClick(museum: MuseumModel, position: Int)
}

class MuseumAdapter constructor(private var museums: ArrayList<MuseumModel>, private val listener: MuseumListener) :
    RecyclerView.Adapter<MuseumAdapter.MainHolder>(), Filterable {

    private val completeList = museums
    private var filteredList = ArrayList<MuseumModel>()

    init{
        filteredList = completeList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMuseumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val museum = filteredList[holder.adapterPosition]
        holder.bind(museum,listener)
    }



    override fun getItemCount(): Int = filteredList.size


    class MainHolder(private val binding : CardMuseumBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(museum: MuseumModel, listener: MuseumListener) {
            binding.name.text = museum.name

            binding.category.text = museum.category
            Picasso.get()
                .load(museum.image[0])
                .resize(200,200)
                .into(binding.imageIcon)
            binding.editButton.setOnClickListener{listener.onEditMuseumClick(museum,adapterPosition)}
            binding.root.setOnClickListener{ listener.onMuseumClick(museum) }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var query = constraint.toString().lowercase(Locale.ROOT)
                if (query.isEmpty()) {
                    filteredList = completeList
                } else {
                    val resultList = ArrayList<MuseumModel>()
                    for (museum in completeList) {
                        if (museum.name.lowercase(Locale.ROOT)
                                .contains(query)
                        ) {
                            resultList.add(museum)
                        }
                    }
                    filteredList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                if (results?.values == null) {
                    filteredList = ArrayList<MuseumModel>()
                    //Toast.makeText(,"No results", Toast.LENGTH_LONG).show()
                }
                else {
                    filteredList = results.values as ArrayList<MuseumModel>
                }
                notifyDataSetChanged()
            }
        }
    }


}