package com.example.onlinemarketserviceprovider.Products

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import com.example.onlinemarketserviceprovider.databinding.ActivityCreateProductBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Base64
import androidx.annotation.NonNull
import com.example.onlinemarketserviceprovider.Helper.LoadingDialog
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class CreateProduct : AppCompatActivity() {
    private var ImageOne:ImageView?=null
    private var ImageTwo:ImageView?=null
    private var ImageThree:ImageView?=null
    private var ImageFour:ImageView?=null
    private val REQUEST_CODE =1122
    private var ImageStatus:String= ""
    private var Name:String=""
    private var CostPrice:String=""
    private var SellPrice:String=""
    private var Quantity:String=""
    private var Color:String=""
    private var Description:String=""
    private var Size:String=""
    private var Discount:String=""
    private var bitmap:Bitmap? = null
    private var imageConvertedString: String =""
    private var shop_id:String=""
    private var token:String=""
    private var ImageOneConverted:String=""
    private var ImageTwoConverted:String=""
    private var ImageThreeConverted:String=""
    private var ImageFourConverted:String=""
    private var isImageEmpty:Boolean?=null
    private  var loadingDialog:LoadingDialog=LoadingDialog()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
         val binding = ActivityCreateProductBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)






        binding.btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })


        binding.btnCancel.setOnClickListener(View.OnClickListener {
//            finish()
            val intent =Intent(this,MyProduct::class.java)
            startActivity(intent)
        })

        //Intialization
         inti()

//        if (askForPermissions()) {
//            Toast.makeText(this,"Ask for Permission",Toast.LENGTH_SHORT).show()
//        }

        //Buttons to upload image-------------------------------------------------------------->
        binding!!.PhotoOne.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoOne"

        })

        binding!!.PhotoTwo.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoTwo"
            isImageEmpty=true
        })

        binding!!.PhotoThree.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoThree"

        })

        binding!!.PhotoFour.setOnClickListener(View.OnClickListener {
        openGalleryCameraForImage()
            ImageStatus="PhotoFour"

        })
        //Button to remove image from ImageView------------------------------------------------>
        binding!!.IBRemovePhotoOne.setOnClickListener(View.OnClickListener {

            ImageOneConverted=""
            ImageOne!!.setImageResource(0)

        })
        binding!!.IBRemovePhotoTwo.setOnClickListener(View.OnClickListener {

            ImageTwoConverted=""
            ImageTwo!!.setImageResource(0)

        })
        binding!!.IBRemovePhotoThree.setOnClickListener(View.OnClickListener {

            ImageThreeConverted=""
            ImageThree!!.setImageResource(0)
        })
        binding!!.IBRemovePhotoFour.setOnClickListener(View.OnClickListener {

            ImageFourConverted=""
            ImageFour!!.setImageResource(0)
        })



   binding!!.btnSubmit.setOnClickListener(
       View.OnClickListener {

           Name=binding.etProductName.getText().toString()
           CostPrice=binding.etProductCostPrice.getText().toString()
           SellPrice=binding.etProductSellPrice.getText().toString()
           Quantity=binding.etProductQuantity.getText().toString()
           Color=binding.etProductColor.getText().toString()
           Description=binding.etProductDescription.getText().toString()
           Size=binding.etProductSize.getText().toString()
           Discount=binding.etProductDiscount.getText().toString()

           if(validate(binding)){
               uploadData()
           }

   })

    }

    //todo converting image to base64 string
     private fun convertingImageToBase64(Image:Uri):String{

        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Image);
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        imageConvertedString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return imageConvertedString
        }
    //todo --------------------------->inti
    private fun inti() {
        var sharedPreferences:SharedPreferences =getSharedPreferences("ShopDetail", MODE_PRIVATE)
         token = sharedPreferences.getString("Token",null).toString()

        shop_id =sharedPreferences.getString("ShopID",null).toString()
        ImageOne =findViewById(R.id.PhotoOne)
        ImageTwo =findViewById(R.id.PhotoTwo)
        ImageThree =findViewById(R.id.PhotoThree)
        ImageFour =findViewById(R.id.PhotoFour)

        //it is used to check image.drawable is equal to null
        ImageOne!!.setImageResource(0)
        ImageTwo!!.setImageResource(0)
        ImageThree!!.setImageResource(0)
        ImageFour!!.setImageResource(0)
          isImageEmpty=false

    }

    //todo --------------------------->Open Gallery or Camera
    private fun openGalleryCameraForImage(){
      ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    //todo --------------------------->Upload image and data to serve
    private fun uploadData(){
        loadingDialog.startLoad(this)
//        loadingDialog.startLoad(this)


        //Volley
        val queue=Volley.newRequestQueue(this)
        val jsonObjectRequest:StringRequest =object:StringRequest(Method.POST,UrlConstant.CreateProduct,
          Response.Listener {

              try {
                  var jsonObject =JSONObject(it)
                  if(jsonObject.getBoolean("success")){
                      //Recall this Activity again(used for Refresh this activity)
                      finish();
                      startActivity(getIntent());


                  Toast.makeText(this,"Success"+jsonObject.get("message"),Toast.LENGTH_LONG).show()
                      loadingDialog.isDismiss()
                  }else{

                      Toast.makeText(this,"error:"+jsonObject.getString("message"),Toast.LENGTH_LONG).show()
                      loadingDialog.isDismiss()
                  }
              }catch(e:Exception){

                  Toast.makeText(this,"Json Error"+e.printStackTrace(),Toast.LENGTH_LONG).show()
                  loadingDialog.isDismiss()
              }

          },Response.ErrorListener {
                Toast.makeText(this,"Volley Error"+it.printStackTrace(),Toast.LENGTH_LONG).show()
                loadingDialog.isDismiss()
        }){
            override fun getHeaders(): MutableMap<String, String> {

                val gh:HashMap<String,String> = HashMap()
                gh["Authorization"]="Bearer "+token
                return gh
            }

            override fun getParams(): MutableMap<String, String> {

                val paras:HashMap<String,String> = HashMap()
                    paras["Shop_id"]=shop_id.toString()
                    paras["Name"]=Name
                    paras["CostPrice"]=CostPrice
                    paras["SellPrice"]=SellPrice
                    paras["Quantity"]=Quantity
                    paras["Color"]=Color
                    paras["Description"]=Description
                    paras["Size"]=Size
                    paras["Discount"]=Discount
                    paras["photoOne"]= ImageOneConverted
                    paras["photoTwo"]= ImageTwoConverted
                    paras["photoThree"]= ImageThreeConverted
                    paras["photoFour"]= ImageFourConverted

                return paras
            }
        }
        queue.add(jsonObjectRequest)
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
                ImageOneConverted = convertingImageToBase64(uri)
                imageConvertedString=""
                ImageStatus= ""}
            else if(ImageStatus=="PhotoTwo"){
                ImageTwo!!.setImageURI(uri)
                ImageTwoConverted = convertingImageToBase64(uri)
                imageConvertedString=""
                ImageStatus= ""}
            else if(ImageStatus=="PhotoThree"){
                ImageThree!!.setImageURI(uri)
                ImageThreeConverted = convertingImageToBase64(uri)
                imageConvertedString=""
                ImageStatus= ""}
            else if(ImageStatus=="PhotoFour"){
                ImageFour!!.setImageURI(uri)
                ImageFourConverted = convertingImageToBase64(uri)
                imageConvertedString=""
                ImageStatus= ""}


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    //todo -------------------------->Check is Permission Allowed
    fun isPermissionAllowed(): Boolean {
      return if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          false
        } else true
    }

    //todo -------------------------->Ask for Permissions
//    fun askForPermissions(): Boolean {
//        if (!isPermissionAllowed()) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this as Activity,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
//             showPermissionDeniedDialog()
//            } else {
//                ActivityCompat.requestPermissions(this as Activity,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),REQUEST_CODE)
//            }
//            return true
//        }
//        return false
//    }
//
//
//    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<String>,grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            REQUEST_CODE -> {
//                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // permission is granted, you can perform your operation here
//                } else {
//                    // permission is denied, you can ask for permission again, if you want
//                    //  askForPermissions()
//                }
//                return
//            }
//        }
//    }
//
//    //ShowPermissionDeniedDialog
//    private fun showPermissionDeniedDialog() {
//        AlertDialog.Builder(this)
//            .setTitle("Permission Denied")
//            .setMessage("Permission is denied, Please allow permissions from App Settings.")
//            .setPositiveButton("App Settings",
//                DialogInterface.OnClickListener { dialogInterface, i ->
//                    // send to app settings if permission is denied permanently
//                    val intent = Intent()
//                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                    val uri = Uri.fromParts("package", getPackageName(), null)
//                    intent.data = uri
//                    startActivity(intent)
//                })
//            .setNegativeButton("Cancel",null)
//            .show()
//    }

    //todo -----------------------------------> Validation
    private fun validate(binding:ActivityCreateProductBinding):Boolean{

        if(ImageOne!!.drawable==null || ImageTwo!!.drawable==null || ImageThree!!.drawable==null || ImageFour!!.drawable==null){
                Toast.makeText(this,"Plz select at least 4 picture",Toast.LENGTH_LONG).show()

                return false
        }

        if(binding.etProductName.getText().toString()==""){
          binding.etProductName.error = "Plz Enter Product Name"
        return false
        }

        if(binding.etProductSellPrice.getText().toString()==""){
            binding.etProductSellPrice .error = "Plz Enter Price Of Product"
        return false
        }
        if(binding.etProductCostPrice.getText().toString().toInt()>binding.etProductSellPrice.getText().toString().toInt()){
            Toast.makeText(this,"Cost Price Can't be greater than Sell Price",Toast.LENGTH_LONG).show()
            return false
        }

        if(binding.etProductQuantity.getText().toString()==""){
            binding.etProductQuantity.error = "Plz Enter Product Quantity"
        return false
        }

        //Why i used try catch?? because when we pass empty discount it will be assume string  and string can't compare with integer
        try {
            if (binding.etProductDiscount.getText().toString().toInt() > binding.etProductSellPrice.getText().toString().toInt()
            ) {
                Toast.makeText(this, "Discount Can't be greater than Sell Price", Toast.LENGTH_LONG)
                    .show()
                return false
            }
        }catch(e:Exception){
            if(binding.etProductDiscount.getText().toString()==""){
                binding.etProductDiscount.setText(null)
            }
        }



     return true
    }

}
