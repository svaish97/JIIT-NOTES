package com.vaish.saurabh.jiitnotes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider

class authentication : AppCompatActivity() {

    var number:String =""
    private lateinit var mGoogleSignInClient:GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

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

                Log.d("error",e.message.toString())
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
            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
