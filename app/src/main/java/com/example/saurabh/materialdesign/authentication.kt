package com.example.saurabh.materialdesign

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_authentication.*

class authentication : AppCompatActivity() {

    var number:String =""
    lateinit var mGoogleSignInClient:GoogleSignInClient
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        mAuth=FirebaseAuth.getInstance()

        val gso=GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        mGoogleSignInClient=GoogleSignIn.getClient(this,gso)

            val signIntent=mGoogleSignInClient.signInIntent
            startActivityForResult(signIntent,1)




//
//        verify.setOnClickListener{
//            number=phoneNo.text.toString()
//            Log.d("Phone",number)
//            if(!number.isEmpty()){
//
//                val intent=Intent(this,verifyPhone::class.java)
//                intent.putExtra("Phone",number)
//                startActivity(intent)
//
//            }
//        }
//    }
//    override fun onStart() {
//        super.onStart()
//        if(FirebaseAuth.getInstance().currentUser!=null)
//        {
//            val intent=Intent(this,MainActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        }
//    }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==1)
        {
            val task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account=task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            }
            catch (e:ApiException)
            {
                Toast.makeText(this,"Sign-In Failed!!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account:GoogleSignInAccount){
        val credential=GoogleAuthProvider.getCredential(account.idToken,null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Sign-In Failed!!",Toast.LENGTH_SHORT).show()

                    }

                }
    }

    override fun onStart() {
        super.onStart()

        if(FirebaseAuth.getInstance().currentUser!=null)
        {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
