package com.example.saurabh.materialdesign.Adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.saurabh.materialdesign.R
import com.example.saurabh.materialdesign.ViewPDF
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.view_layout.view.*


data class ViewpPDFPojo(var filename:String,var url:String,val username:String,val email:String){
    constructor():this("","","","")
}


class ViewPDFAdapter(val data:ArrayList<ViewpPDFPojo>):RecyclerView.Adapter<ViewPDFAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li=parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val holder=li.inflate(R.layout.view_layout,parent,false)
        return ViewHolder(holder)
    }

    override fun getItemCount(): Int =data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemView.FileName.text=data[position].filename


        holder.itemView.setOnClickListener{
           // val i = Intent(Intent.ACTION_VIEW)
            val i=Intent(holder.itemView.context,ViewPDF::class.java)
//            i.data= Uri.parse(data[position].url)
//            i.setDataAndType(Uri.parse(
//                    "http://drive.google.com/viewer?url=" + data[position].url),
//                    "text/html" )
            i.putExtra("PDFurl",data[position].url)

            it.context.startActivity(i)
        }

//        holder.itemView.setOnLongClickListener{
//            val builder=AlertDialog.Builder(holder.itemView.context)
//            builder.setTitle("Are you sure!!")
//            builder.setMessage("Do you want to delete this item")
//            builder.setPositiveButton("Yes",object : DialogInterface.OnClickListener{
//                override fun onClick(dialog: DialogInterface?, which: Int) {
//                    val sReference=FirebaseStorage.getInstance().getReferenceFromUrl(data[position].url)
//                    val mReference=FirebaseDatabase.getInstance().getReference()
//                    val query=mReference.child("3rd year").orderByChild("url").equalTo(data[position].url)
//
//                    sReference.delete().addOnSuccessListener {
//                        query.addListenerForSingleValueEvent(object:ValueEventListener{
//                            override fun onCancelled(p0: DatabaseError) {
//
//                            }
//
//                            override fun onDataChange(p0: DataSnapshot) {
//                                for(i in p0.children)
//                                {
//                                    i.ref.removeValue()
//                                }
//                            }
//
//                        })
//                        data.removeAt(position)
//                        notifyDataSetChanged()
//
//                    }
//                }
//
//            })
//            builder.setNegativeButton("No",{_,_->Unit})
//            builder.show()
//            true
//
//
//
//
//        }

    }
    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)
    {

    }
}