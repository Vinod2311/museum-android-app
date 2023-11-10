package ie.setu.museum.helpers

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

fun showImagePicker(intentLauncher: ActivityResultLauncher<Intent>, context: Context) {
    var imagePickerTargetIntent = Intent()

    imagePickerTargetIntent.action = Intent.ACTION_OPEN_DOCUMENT
    imagePickerTargetIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
    imagePickerTargetIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    imagePickerTargetIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    imagePickerTargetIntent.type = "image/*"
    imagePickerTargetIntent = Intent.createChooser(imagePickerTargetIntent,
        "Choose an image")
    intentLauncher.launch(imagePickerTargetIntent)
}