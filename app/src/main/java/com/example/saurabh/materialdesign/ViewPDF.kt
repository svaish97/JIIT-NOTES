package com.example.saurabh.materialdesign

import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_view_pdf.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ViewPDF : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pdf)

        val url=intent.getStringExtra("PDFurl")

        //Log.d("pdf",url)

//        pdfView.fromUri(Uri.parse(url)).load()

        loading.visibility= View.VISIBLE
        pdfView.visibility=View.GONE
        RetrievePDFStream().execute(url)
    }

inner class RetrievePDFStream: AsyncTask<String, Void, InputStream>() {
    override fun doInBackground(vararg params: String?): InputStream? {
        var inputStream:InputStream?=null
        try {
            val url=URL(params[0])
            val httpURLConnection=url.openConnection() as HttpURLConnection
            if(httpURLConnection.responseCode==200) {
                inputStream = BufferedInputStream(httpURLConnection.inputStream)

            }

        }
        catch (e:IOException){
            return null
        }
        return inputStream
    }

    override fun onPostExecute(result: InputStream?) {
        loading.visibility=View.GONE
        pdfView.visibility=View.VISIBLE
        pdfView.fromStream(result).load()

    }
}

}
