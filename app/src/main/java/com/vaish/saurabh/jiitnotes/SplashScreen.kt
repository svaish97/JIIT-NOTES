package com.vaish.saurabh.jiitnotes

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_splash_screen)


//        progressBar.progressDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
//
       Thread(Runnable{
            for (i in 1..100 step 10)
            {
                Thread.sleep(150)
//                progressBar.progress=i
            }
            Thread.sleep(500)
            startActivity(Intent(this@SplashScreen,MainActivity::class.java))
            finish()



        }).start()



    }


}
