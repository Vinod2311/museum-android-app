package ie.setu.museum.ui.addMuseum

/*
class AddMuseumView : AppCompatActivity() {

    private lateinit var binding: ActivityAddMuseumBinding
    private lateinit var presenter: AddMuseumPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMuseumBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)
        val dropdownItems = resources.getStringArray(R.array.simple_items)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, dropdownItems)
        //val dropdownMenu = findViewById<AutoCompleteTextView>(R.id.category)
        //dropdownMenu.setAdapter(arrayAdapter)
        binding.category.setAdapter(arrayAdapter)
        presenter = AddMuseumPresenter(this)

        binding.chooseImage.setOnClickListener{
            presenter.cacheMuseum(binding.nameText.text.toString(),binding.shortDescriptionText.text.toString(),binding.category.text.toString(), binding.reviewText.text.toString(), binding.ratingBar.rating)
            presenter.doSelectImage()
        }

        binding.location.setOnClickListener{
            presenter.cacheMuseum(binding.nameText.text.toString(),binding.shortDescriptionText.text.toString(),binding.category.text.toString(), binding.reviewText.text.toString(), binding.ratingBar.rating)
            presenter.doSetLocation()
        }

        binding.createButton.setOnClickListener{

            if (binding.nameText.text.toString().isNotEmpty() && binding.category.text.toString().isNotEmpty()){
                presenter.cacheMuseum(binding.nameText.text.toString(), binding.shortDescriptionText.text.toString(),binding.category.text.toString(), binding.reviewText.text.toString(), binding.ratingBar.rating)
                presenter.doAddOrSave(binding.nameText.text.toString(), binding.shortDescriptionText.text.toString(),binding.category.text.toString(), binding.reviewText.text.toString(), binding.ratingBar.rating)
            } else if (binding.nameText.text.toString().isEmpty()){
                binding.name.error = "Museum name is required"

            } else if (binding.category.text.toString().isEmpty()){
                binding.categoryLayout.error = "Category is required"
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_secondary, menu)
        val deleteMenu: MenuItem = menu.findItem(R.id.item_delete)
        deleteMenu.isVisible = presenter.edit
        if(presenter.edit){
            binding.createButton.setText(R.string.save_changes)
            binding.name.isHelperTextEnabled = false
            binding.categoryLayout.isHelperTextEnabled = false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                presenter.doDelete()
            }
            R.id.item_cancel -> {
                presenter.doCancel()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun showMuseum(imageList: ArrayList<SlideModel>, museum: MuseumModel) {
        binding.nameText.setText(museum.name)
        binding.shortDescriptionText.setText(museum.shortDescription)
        binding.category.setText(museum.category,false)
        binding.ratingBar.rating = museum.rating
        binding.reviewText.setText(museum.review)
        if (imageList.isNotEmpty())
            binding.imageView.setImageList(imageList, ScaleTypes.CENTER_CROP)
        if (museum.image[0] != Uri.EMPTY) {
            binding.chooseImage.setText(R.string.change_image)

        }
    }

    fun updateImage(imageList: ArrayList<SlideModel>) {
        Timber.i("Image updated")
        binding.imageView.setImageList(imageList, ScaleTypes.CENTER_CROP)
        binding.chooseImage.setText(R.string.change_image)
    }




}

 */