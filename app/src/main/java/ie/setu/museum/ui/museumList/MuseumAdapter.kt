package ie.setu.museum.ui.museumList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.setu.museum.R
import ie.setu.museum.databinding.CardMuseumBinding
import ie.setu.museum.models.museum.MuseumModel
import java.util.Locale

interface MuseumListener {
    fun onMuseumClick(museum: MuseumModel)
    fun onEditMuseumClick(museum: MuseumModel)
}

class MuseumAdapter(private var museums: ArrayList<MuseumModel>,
                    private var favouriteList: ArrayList<MuseumModel>?,
                    private val listener: MuseumListener,
                    private val readOnly: Boolean) :
    RecyclerView.Adapter<MuseumAdapter.MainHolder>(), Filterable {

    private val completeList = museums
    //private val favouriteList = museums.map { it ->
    //    MuseumModelWithFavourites(it.name,it.category,it.shortDescription,it.review,it.rating,it.museumPreviewImage,it.profilePic,it.images,it.lat,it.lng,it.zoom,it.email,it.uid)
    //}
    private var filteredList = ArrayList<MuseumModel>()
    var context: Context? = null

    init{/*
        for(museum in favouriteList){

            museum.isFavourite = false
        }*/
        filteredList = completeList

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardMuseumBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return MainHolder(binding,readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val museum = filteredList[holder.adapterPosition]
        holder.bind(context,museum,favouriteList,listener)

    }



    override fun getItemCount(): Int = filteredList.size


    class MainHolder(private val binding : CardMuseumBinding, private val readOnly: Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(context:Context?, museum: MuseumModel, favouriteList: ArrayList<MuseumModel>?, listener: MuseumListener) {
            //binding.name.text = museum.name
            //binding.category.text = museum.category
            binding.museum = museum
            binding.root.tag = museum


            if (museum.images?.size != 0) {
                Picasso.get()
                    .load(museum.images!![0].toUri())
                    .resize(200, 200)
                    .into(binding.imageIcon)
            } else {/*
                Picasso.get()
                    .load(R.drawable.splash_screen_icon)
                    .resize(200, 200)
                    .into(binding.imageIcon)*/
            }
            when (museum.category) {
                "Natural History" -> binding.cardLayout.setBackgroundColor(context!!.getColor(R.color.light_blue))
                "History" -> binding.cardLayout.setBackgroundColor(context!!.getColor(R.color.light_green))
                "Science" -> binding.cardLayout.setBackgroundColor(context!!.getColor(R.color.light_yellow))
                "Art" -> binding.cardLayout.setBackgroundColor(context!!.getColor(R.color.light_orange))
            }
            if (favouriteList?.size != 0) {
                for (favouriteMuseum in favouriteList!!) {
                    if (museum.uid == favouriteMuseum?.uid) {
                        binding.favouriteButton.setBackgroundResource(R.drawable.ic_favorite_red)
                        museum.isFavourite = true
                        break
                    } else {
                        binding.favouriteButton.setBackgroundResource(R.drawable.ic_favorite_grey)
                        museum.isFavourite = false
                    }
                }
            }

            binding.favouriteButton.setOnClickListener{
                if(museum.isFavourite){

                    favouriteList.remove(museum)
                    museum.isFavourite = false
                    binding.favouriteButton.setBackgroundResource(R.drawable.ic_favorite_grey)
                } else {
                    museum.isFavourite = true
                    favouriteList.add(museum)
                    binding.favouriteButton.setBackgroundResource(R.drawable.ic_favorite_red)
                }


            }
            binding.root.setOnClickListener{ listener.onMuseumClick(museum) }
            binding.executePendingBindings()
        }
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var query = constraint.toString().lowercase(Locale.ROOT)
                if (query.isEmpty()) {
                    filteredList = completeList as ArrayList<MuseumModel>
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

            override fun publishResults(constraint: CharSequence?, results: FilterResults? ) {

                if ((results?.values as ArrayList<MuseumModel>).size == 0 ) {
                    filteredList = ArrayList<MuseumModel>()
                    if(context != null){
                        Toast.makeText(context,"No results", Toast.LENGTH_LONG).show()}
                }
                else {
                    filteredList = results?.values as ArrayList<MuseumModel>
                }
                notifyDataSetChanged()
            }
        }
    }

    fun removeAt(position: Int) {
        museums.removeAt(position)
        notifyItemRemoved(position)
    }


}