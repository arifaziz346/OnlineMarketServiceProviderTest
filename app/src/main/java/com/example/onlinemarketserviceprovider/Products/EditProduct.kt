package com.example.onlinemarketserviceprovider.Products


import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.example.onlinemarketserviceprovider.databinding.ActivityEditProductBinding

import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.onlinemarketserviceprovider.R
import com.example.onlinemarketserviceprovider.UrlConstant
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class EditProduct : AppCompatActivity() {
    private var shop_id:Int=0
    private var product_id:Int=0
    private var Name:String?=null
    private var Description:String?=null
    private var Color:String?=null
    private var Size:String?=null
    private var CostPrice:Int=0
    private var SellPrice:Int=0
    private var Quantity:Int=0
    private var Discount:Int=0
    //
    private var NamePhotoOne:String=""
    private var NamePhotoTwo:String=""
    private var NamePhotoThree:String=""
    private var NamePhotoFour:String=""

    private var PhotoOne:ImageView?=null
    private var PhotoTwo:ImageView?=null
    private var PhotoThree:ImageView?=null
    private var PhotoFour:ImageView?=null

    //Check for update image
    private var isUpdatePhotoOne:String=""
    private var isUpdatePhotoTwo:String=""
    private var isUpdatePhotoThree:String=""
    private var isUpdatePhotoFour:String=""
    //Use to validate Photo
    private var isPhotoOneEmpty:Boolean?=null
    private var isPhotoTwoEmpty:Boolean?=null
    private var isPhotoThreeEmpty:Boolean?=null
    private var isPhotoFourEmpty:Boolean?=null
    //Shard prefe
    private lateinit var sharedPreferences: SharedPreferences
    //Bitmp
    private val REQUEST_CODE =1122
    private var bitmap:Bitmap? = null
    private var imageConvertedString: String =""
    private var ImageStatus:String= ""
    private var ImageOneConverted=""
    private var ImageTwoConverted=""
    private var ImageThreeConverted=""
    private var ImageFourConverted=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**----------------todo Initialize Value**/
        inti(binding)

        //Btn used to cancel
        binding.btnCancel.setOnClickListener(
            View.OnClickListener {
                finish()
            })

        binding.btnBack.setOnClickListener(
            View.OnClickListener {
                finish()
            })

        //Btn Update
        binding.btnSubmit.setOnClickListener(View.OnClickListener {
          if (validate(binding)){
              editProduct(binding)
          }
        })

        //todo Buttons to upload image-------------------------------------------------------------->
        binding!!.PhotoOne.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoOne"
            isPhotoOneEmpty=false
            binding.PhotoOne.setImageDrawable(null)


        })

        binding!!.PhotoTwo.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoTwo"
            isPhotoTwoEmpty=false

        })

        binding!!.PhotoThree.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoThree"
            isPhotoThreeEmpty=false

        })

        binding!!.PhotoFour.setOnClickListener(View.OnClickListener {
            openGalleryCameraForImage()
            ImageStatus="PhotoFour"
            isPhotoFourEmpty=false

        })

        //todo--------------------->Buttons use to remove picture
        binding.IBRemovePhotoOne.setOnClickListener(View.OnClickListener {
            binding.PhotoOne.setImageResource(0)
            isPhotoOneEmpty=true

        })
        binding.IBRemovePhotoTwo.setOnClickListener(View.OnClickListener {
            binding.PhotoTwo.setImageResource(0)
            isPhotoTwoEmpty=true
        })
        binding.IBRemovePhotoThree.setOnClickListener(View.OnClickListener {
            binding.PhotoThree.setImageResource(0)
            isPhotoThreeEmpty=true
        })
        binding.IBRemovePhotoFour.setOnClickListener(View.OnClickListener {
            binding.PhotoFour.setImageResource(0)
            isPhotoFourEmpty=true
        })
    }



    //todo---------------------------------------->Initialization----------------
    private fun inti(binding:ActivityEditProductBinding) {
        sharedPreferences =getSharedPreferences("ShopDetail",AppCompatActivity.MODE_PRIVATE)
        PhotoOne=binding.PhotoOne
        PhotoTwo=binding.PhotoTwo
        PhotoThree=binding.PhotoThree
        PhotoFour=binding.PhotoFour

        val bundle: Bundle? = intent.extras
        if(bundle!=null) {

            shop_id = bundle.getInt("shop_id")
            product_id = bundle.getInt("product_id")
            Name = bundle.getString("Name").toString()
            Description = bundle.getString("Description").toString()
            Color = bundle.getString("Color").toString()
            Size = bundle.getString("Size").toString()
            SellPrice = bundle.getInt("SellPrice")
            CostPrice = bundle.getInt("CostPrice")
            Quantity = bundle.getInt("Quantity")
            Discount=bundle.getInt("Discount")
            NamePhotoOne =bundle.getString("PhotoOne").toString()
            NamePhotoTwo =bundle.getString("PhotoTwo").toString()
            NamePhotoThree =bundle.getString("PhotoThree").toString()
            NamePhotoFour =bundle.getString("PhotoFour").toString()

            binding.etProductName.setText(Name)
            binding.etProductCostPrice.setText(CostPrice.toString())
            binding.etProductSellPrice.setText(SellPrice.toString())
            binding.etProductColor.setText(Color)
            binding.etProductQuantity.setText(Quantity.toString())
            binding.etProductColor.setText(Color)
            binding.etProductDescription.setText(Description.toString())
            binding.etProductDiscount.setText(Discount.toString())

            //Set Images
            Picasso.get().load(UrlConstant.URL+"storage/products/"+NamePhotoOne).into(binding.PhotoOne)
            Picasso.get().load(UrlConstant.URL+"storage/products/"+NamePhotoTwo).into(binding.PhotoTwo)
            Picasso.get().load(UrlConstant.URL+"storage/products/"+NamePhotoThree).into(binding.PhotoThree)
            Picasso.get().load(UrlConstant.URL+"storage/products/"+NamePhotoFour).into(binding.PhotoFour)
        }else{
            Toast.makeText(this,"Null",Toast.LENGTH_LONG).show()
        }
    }
    //todo converting image to base64 string
    private fun convertingImageToBase64(Image: Uri):String{

        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Image);
        val baos = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes: ByteArray = baos.toByteArray()
        imageConvertedString = Base64.encodeToString(imageBytes, Base64.DEFAULT)
        return imageConvertedString
    }

    //todo --------------------------->Open Gallery or Camera
    private fun openGalleryCameraForImage(){
        ImagePicker.with(this)
            .crop()	    			//Crop image(Optional), Check Customization for more option
            .compress(1024)			//Final image size will be less than 1 MB(Optional)
            .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
            .start()
    }

    //todo ----------------------------------->Validation------------------------
    private fun validate(binding: ActivityEditProductBinding):Boolean{

        if(binding.PhotoOne.drawable==null || binding.PhotoTwo.drawable==null ||
            binding.PhotoThree.drawable==null || binding.PhotoFour.drawable==null){
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

        if(binding.etProductDiscount.getText().toString().toInt()>binding.etProductSellPrice.getText().toString().toInt()){
            Toast.makeText(this,"Cost Price Can't be greater than Sell Price",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    //todo ---------------------------------->onActivityResult-------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            if(ImageStatus.equals("PhotoOne")){
                Picasso.get().load(uri).into(PhotoOne)
                ImageOneConverted = convertingImageToBase64(uri)
                imageConvertedString=""
                isUpdatePhotoOne=NamePhotoOne
                Toast.makeText(this,"isUpdatePhotoOne"+isUpdatePhotoOne,Toast.LENGTH_SHORT).show()
            }
            else if(ImageStatus=="PhotoTwo"){
                PhotoTwo!!.setImageURI(uri)
                ImageTwoConverted = convertingImageToBase64(uri)
                imageConvertedString=""
                isUpdatePhotoTwo=NamePhotoTwo
            }
            else if(ImageStatus=="PhotoThree"){
                PhotoThree!!.setImageURI(uri)
                ImageThreeConverted = convertingImageToBase64(uri)
                imageConvertedString=""
                isUpdatePhotoThree=NamePhotoThree
            }
            else if(ImageStatus=="PhotoFour"){
                PhotoFour!!.setImageURI(uri)
                ImageFourConverted = convertingImageToBase64(uri)
                imageConvertedString=""
                isUpdatePhotoFour=NamePhotoFour

            }


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

   //todo ----------------------------------->EditProduct to server side---------
    private fun editProduct(binding:ActivityEditProductBinding) {
        val queue=Volley.newRequestQueue(this)
        var editJORequest:StringRequest = object:StringRequest(Method.POST,UrlConstant.EditProduct,
            Response.Listener {
                try {
                    var JSONObject = JSONObject(it)
                    if(JSONObject.getBoolean("success")){
                        Toast.makeText(this,JSONObject.getString("message"),Toast.LENGTH_SHORT).show()
                        finish()
                       val intent =Intent(this,MyProduct::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this,"Fail:"+JSONObject.getString("message"),Toast.LENGTH_SHORT).show()
                    }
                }
                catch (e:JSONException){
                    Toast.makeText(this,"JsonException Fail:"+e.printStackTrace(),Toast.LENGTH_SHORT).show()
                }

        },Response.ErrorListener {
                Toast.makeText(this,"Volley Error:"+it,Toast.LENGTH_SHORT).show()
        }){
            val token =sharedPreferences.getString("Token",null)
            override fun getHeaders(): MutableMap<String, String> {
                var ghToken:HashMap<String,String> = HashMap()
                ghToken["Authorization"]="Bearer "+token
                return ghToken
            }
            override fun getParams(): MutableMap<String, String> {

                val gParams:HashMap<String,String> = HashMap()
                gParams["Shop_id"]=shop_id.toString()
                gParams["product_id"]=product_id.toString()
                gParams["Name"]=binding.etProductName.getText().toString()
                gParams["CostPrice"]=binding.etProductCostPrice.getText().toString()
                gParams["SellPrice"]=binding.etProductSellPrice.getText().toString()
                gParams["Quantity"]=binding.etProductQuantity.getText().toString()
                gParams["Color"]=binding.etProductColor.getText().toString()
                gParams["Description"]=binding.etProductDescription.getText().toString()
                gParams["Size"]=binding.etProductSize.getText().toString()
                gParams["Discount"]=binding.etProductDiscount.getText().toString()
                gParams["photoOne"]= ImageOneConverted
                gParams["photoTwo"]= ImageTwoConverted
                gParams["photoThree"]=ImageThreeConverted
                gParams["photoFour"]= ImageFourConverted
                //list of image name which are use to remove and upate image list..
                gParams["isUpdatePhotoOne"]= isUpdatePhotoOne
                gParams["isUpdatePhotoTwo"]= isUpdatePhotoTwo
                gParams["isUpdatePhotoThree"]= isUpdatePhotoThree
                gParams["isUpdatePhotoFour"]= isUpdatePhotoFour
                return gParams
            }
        }
        queue.add(editJORequest)
    }

}