package com.nft.maker.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import com.nft.maker.R

object  ValidationAlert {
        // CUSTOM PROGRESS DIALOG
        fun getValidationDialog(context: Context?, cancelable: Boolean?,headings:String,message:String): AlertDialog {

            val dialogBuilder = AlertDialog.Builder(context)

            val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val dialogView = inflater.inflate(R.layout.custom_alert_dialog_layout, null)
            dialogBuilder.setView(dialogView)


            val dialog = dialogBuilder.create()
            dialog.show()
            dialog.setCanceledOnTouchOutside(cancelable!!)
            val heading = dialogView.findViewById<TextView>(R.id.heading)
            val subheading = dialogView.findViewById<TextView>(R.id.subHeading)
            val clickPositive = dialogView.findViewById<Button>(R.id.yesButton)
            heading.text=headings
            subheading.text=message
            clickPositive.setOnClickListener { dialog.dismiss() }
            val width = (context.resources.displayMetrics.widthPixels * 0.67f).toInt()
            dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            return dialog
        }





}