package ie.setu.museum.ui.addMuseum

/*
class AddMuseumPresenter(private val view: AddMuseumView) {

    var museum = MuseumModel()
    var app: MainApp = view.application as MainApp
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    var edit = false;
    val imageList = ArrayList<SlideModel>()
    lateinit var loggedInUser: UserModel

    init {
        if (view.intent.hasExtra("museum_edit")) {
            edit = true
            museum = view.intent.extras?.getParcelable("museum_edit")!!
            if (museum.image[0] != Uri.EMPTY) {
                for (x in museum.image) {
                    imageList.add(SlideModel(x.toString()))
                }
            }
            view.showMuseum(imageList,museum)
        }
         //registerImagePickerCallback()
         //registerMapCallback()
    }


    fun doAddOrSave(name: String,description: String,category: String, review: String, rating: Float ){
        museum.name = name
        museum.shortDescription = description
        museum.review = review
        museum.rating = rating
        museum.category = category
        loggedInUser= view.intent.extras?.getParcelable("user")!!
        museum.user = loggedInUser
        if (edit) {
            app.museums.update(museum)
        } else {
            app.museums.create(museum)
        }
        view.setResult(RESULT_OK)
        view.finish()
    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher,view)
    }

    fun doSetLocation() {
        val location = Location(52.245696, -7.139102, 15f)
        if (museum.zoom != 0f) {
            location.lat =  museum.lat
            location.lng = museum.lng
            location.zoom = museum.zoom
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
            .putExtra("museum",museum)
        mapIntentLauncher.launch(launcherIntent)
    }

    fun cacheMuseum(name: String,description: String,category: String, review: String, rating: Float){
        museum.name = name
        museum.shortDescription = description
        museum.category = category
        museum.rating = rating
        museum.review = review
    }

    fun doCancel() {
        view.finish()
    }
    fun doDelete() {
        view.setResult(99)
        app.museums.delete(museum)
        view.finish()
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data!!.clipData != null) {
                            Timber.i("Got Result ${result.data!!.clipData}")
                            var count:Int = result.data!!.clipData!!.itemCount
                            var currentItem = 0
                            museum.image.clear()
                            imageList.clear()
                            while (currentItem < count) {
                                museum.image.add(result.data!!.clipData!!.getItemAt(currentItem).uri)
                                view.contentResolver.takePersistableUriPermission(
                                    museum.image[currentItem],
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                                )
                                currentItem += 1
                            }
                            for (x in museum.image) {
                                imageList.add(SlideModel(x.toString(), "some text"))
                            }
                            view.updateImage(imageList)
                        } else if(result.data != null) {
                            Timber.i("Got Result ${result!!.data!!.data}")
                            museum.image.clear()
                            museum.image.add(result.data!!.data!!)
                            imageList.clear()
                            view.contentResolver.takePersistableUriPermission(
                                museum.image[0],
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            imageList.add(SlideModel(museum.image[0].toString()))
                            view.updateImage(imageList)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            museum.lat = location.lat
                            museum.lng = location.lng
                            museum.zoom = location.zoom
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }
            }
    }


}

 */