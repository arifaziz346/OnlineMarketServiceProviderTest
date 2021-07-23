package com.example.onlinemarketserviceprovider.Products

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.renderscript.ScriptGroup
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.databinding.ActivityCreateProductBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlin.math.log

class CreateProduct : AppCompatActivity() {
    var ImageOne:ImageView?=null
    var ImageTwo:ImageView?=null
    var ImageThree:ImageView?=null
    var ImageFour:ImageView?=null
    val REQUEST_CODE =1122

    var ImageStatus:String= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityCreateProductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        //Intialization
         inti()

//        if (askForPermissions()) {
//            Toast.makeText(this,"Ask for Permission",Toast.LENGTH_SHORT).show()
//        }

        //Buttons to upload image-------------------------------------------------------------->
        binding.PhotoOne.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoOne"
        })

        binding.PhotoTwo.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoTwo"
        })

        binding.PhotoThree.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoThree"
        })

        binding.PhotoFour.setOnClickListener(View.OnClickListener {
        openGalleryCameraForImage()
            ImageStatus="PhotoFour"
        })
        //Button to remove image from ImageView------------------------------------------------>
        binding.IBRemovePhotoOne.setOnClickListener(View.OnClickListener {
            ImageOne!!.setImageDrawable(resources.getDrawable(R.drawable.image_upload_product));
        })
        binding.IBRemovePhotoTwo.setOnClickListener(View.OnClickListener {
            ImageTwo!!.setImageDrawable(resources.getDrawable(R.drawable.image_upload_product));
        })
        binding.IBRemovePhotoThree.setOnClickListener(View.OnClickListener {
            ImageThree!!.setImageDrawable(resources.getDrawable(R.drawable.image_upload_product));
        })
        binding.IBRemovePhotoFour.setOnClickListener(View.OnClickListener {
            ImageFour!!.setImageDrawable(resources.getDrawable(R.drawable.image_upload_product));
        })





    }

    private fun openGalleryCameraForImage(){

        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    private fun inti() {
        ImageOne =findViewById(R.id.PhotoOne)
        ImageTwo =findViewById(R.id.PhotoTwo)
        ImageThree =findViewById(R.id.PhotoThree)
        ImageFour =findViewById(R.id.PhotoFour)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            if(ImageStatus.equals("PhotoOne")){
                Toast.makeText(this,"OnActivityResult",Toast.LENGTH_SHORT).show()
                ImageOne!!.setImageURI(uri)
                ImageStatus= ""}
            else if(ImageStatus=="PhotoTwo"){
                ImageTwo!!.setImageURI(uri)
                ImageStatus= ""}
            else if(ImageStatus=="PhotoThree"){
                ImageThree!!.setImageURI(uri)
                ImageStatus= ""}
            else if(ImageStatus=="PhotoFour"){
                ImageFour!!.setImageURI(uri)
                ImageStatus= ""}


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    //Check is Permission Allowed
    fun isPermissionAllowed(): Boolean {
      return if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          false
        } else true
    }

    //Ask for Permissions
    fun askForPermissions(): Boolean {
        if (!isPermissionAllowed()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
             showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(this as Activity,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE)
            }
            return true
        }
        return false
    }


    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission is granted, you can perform your operation here
                } else {
                    // permission is denied, you can ask for permission again, if you want
                    //  askForPermissions()
                }
                return
            }
        }
    }



    //ShowPermissionDeniedDialog
    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    // send to app settings if permission is denied permanently
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package", getPackageName(), null)
                    intent.data = uri
                    startActivity(intent)
                })
            .setNegativeButton("Cancel",null)
            .show()
    }

}
