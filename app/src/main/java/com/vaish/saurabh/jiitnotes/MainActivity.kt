package com.vaish.saurabh.jiitnotes

import android.Manifest
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.vaish.saurabh.jiitnotes.Adapters.ViewpPDFPojo
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.get_file_data.view.*
import kotlinx.android.synthetic.main.nav_header.view.*
import java.util.*

@Suppress("PLUGIN_WARNING")
class MainActivity : AppCompatActivity() {
    lateinit var fabOpen:Animation
    lateinit var fabClose:Animation
    lateinit var rotateX:Animation
    lateinit var rotateY:Animation
    lateinit var filepath: Uri
    var year:String=""
    var storageRefernce: StorageReference? = null
    lateinit var mstorage:DatabaseReference
    lateinit var fileNamefromUpload:String
    lateinit var mUser:GoogleSignInAccount


    var isOpen=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        if(FirebaseAuth.getInstance().currentUser!=null) {

            mUser= GoogleSignIn.getLastSignedInAccount(this)!!

            fab.visibility=View.VISIBLE
            Log.d("UserName",mUser.photoUrl.toString())
            val view=nav_view.getHeaderView(0)
            view.userName.text=mUser.displayName
            view.userEmail.text=mUser.email
            Picasso.get().load(mUser.photoUrl).resize(200,200).placeholder(R.drawable.ic_launcher_background).into(view.imageView)
            val toggle = ActionBarDrawerToggle(
                    this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer_layout.addDrawerListener(toggle)

            toggle.syncState()
            nav_view.menu.clear()
            nav_view.inflateMenu(R.menu.activity_logout_menu)
            nav_view.requestLayout()
            nav_view.bringToFront()

            nav_view.setNavigationItemSelectedListener {
                if (it.itemId == R.id.signOut) {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                if(it.itemId==R.id.privacy_logout){
                    startActivity(Intent(this,PrivacyPolicy::class.java))

                }
                true
            }
        }
        else{

            fab.visibility=View.INVISIBLE

            val toggle = ActionBarDrawerToggle(
                    this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawer_layout.addDrawerListener(toggle)

            toggle.syncState()
            nav_view.menu.clear()
            nav_view.inflateMenu(R.menu.activity_login_menu)
            nav_view.requestLayout()
            nav_view.bringToFront()

            nav_view.setNavigationItemSelectedListener {
                if (it.itemId == R.id.signIn) {
                    val intent = Intent(this, authentication::class.java)
                    startActivity(intent)
                    finish()

                }
                if(it.itemId==R.id.privacy){
                    startActivity(Intent(this,PrivacyPolicy::class.java))
                }
                true
            }

        }


        storageRefernce= FirebaseStorage.getInstance().reference
        mstorage=FirebaseDatabase.getInstance().reference

        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                , 123)




        fabOpen=AnimationUtils.loadAnimation(this,R.anim.fab_open)
        fabClose=AnimationUtils.loadAnimation(this,R.anim.fab_close)
        rotateX=AnimationUtils.loadAnimation(this,R.anim.rotate_clockwise)
        rotateY=AnimationUtils.loadAnimation(this,R.anim.rotate_anticlockwise)

        fab.setOnClickListener { view ->
            if(isOpen)
            {
                fabUpload.startAnimation(fabClose)
                fab.startAnimation(rotateY)
                fabUpload.isClickable=false
                isOpen=false

            }
            else{
                fabUpload.startAnimation(fabOpen)
                fab.startAnimation(rotateX)
                fabUpload.isClickable=true
                isOpen=true
            }
        }


        fabUpload.setOnClickListener {




           val mView=LayoutInflater.from(this).inflate(R.layout.get_file_data,null)
           val mBuilder=AlertDialog.Builder(this@MainActivity).setView(mView)

            val spinnerData: Array<String> = resources.getStringArray(R.array.year)
            val adapter:ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,spinnerData)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            mView.whichYear.adapter=adapter

           val mDialog=mBuilder.show()


            mView.whichYear.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    year=spinnerData[position]
                    Log.d("Year",year)
                }

            }



            mView.btnUpload.setOnClickListener {


                fileNamefromUpload= mView.etFileName.text.toString()



                if (!fileNamefromUpload.isEmpty()) {
                    mDialog.dismiss()
                    val intent = Intent()
                    intent.type = "application/pdf"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 123)
                }
                else{
                    Toast.makeText(this,"Please Enter The File Name",Toast.LENGTH_SHORT).show()
                }

            }


        }



        year1.setOnClickListener {
            val intent=Intent(this,Main2Activity::class.java)
            intent.putExtra("Year","1st year")
            startActivity(intent)
        }

        year2.setOnClickListener {
            val intent=Intent(this,Main2Activity::class.java)
            intent.putExtra("Year","2nd year")
            startActivity(intent)
        }


       year3.setOnClickListener {

           val intent=Intent(this,Main2Activity::class.java)
           intent.putExtra("Year","3rd year")
           startActivity(intent)
       }

        year4.setOnClickListener {
            val intent=Intent(this,Main2Activity::class.java)
            intent.putExtra("Year","4th year")
            startActivity(intent)
        }





    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null && requestCode==123) {
            filepath = data.data!!


            val storageRef = storageRefernce!!.child(year+"/" +UUID.randomUUID().toString()+"=  "+fileNamefromUpload+".pdf")

            val progressDial = ProgressDialog(this)
            progressDial.setTitle("Uploading...")
            progressDial.show()


            storageRef!!.putFile(filepath!!)
                    .addOnSuccessListener { taskSnapshot ->
                        progressDial.dismiss()
                        Toast.makeText(this, "Successfully uploaded", Toast.LENGTH_SHORT).show()
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val result = it.toString()

                            mstorage!!.child(year).push().setValue(ViewpPDFPojo(fileNamefromUpload,result,mUser.displayName!!,mUser.email!!))


                         }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Upload Failed", Toast.LENGTH_SHORT).show()
                        progressDial.dismiss()
                    }
                    .addOnProgressListener {
                        val progress = (100.0 * it.bytesTransferred / it.totalByteCount)
                        progressDial.setMessage("Uploaded " + progress.toInt() + "%")
                        progressDial.setCancelable(false)
                    }
        }
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
        val atdIntent=Intent(this@MainActivity,aboutTheDevelopers::class.java)
        startActivity(atdIntent)
        return true
//        }
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


}
