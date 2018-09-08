package com.vaish.saurabh.jiitnotes

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.vaish.saurabh.jiitnotes.Adapters.ViewPDFAdapter
import com.vaish.saurabh.jiitnotes.Adapters.ViewpPDFPojo
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity() {

    var mstorage:DatabaseReference?=null
    val pdfs= ArrayList<ViewpPDFPojo>()
    lateinit var adapter: ViewPDFAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        display()


        val year=intent.getStringExtra("Year")
        mstorage=FirebaseDatabase.getInstance().getReference();



        mstorage!!.child(year).addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                pdfs.clear()

                for(i in p0.children){
                    val url=i.getValue(ViewpPDFPojo::class.java)
                    url?.let{
                        pdfs.add(it)
                       adapter.notifyDataSetChanged()
                    }

                }
             }
        })
    }

    fun display()
    {
         adapter=ViewPDFAdapter(pdfs)
        recyclerView.layoutManager=LinearLayoutManager(this@Main2Activity)
        recyclerView.adapter=adapter
    }
}
