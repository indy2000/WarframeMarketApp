package com.fukajima.warframemarket.utils

import android.widget.ImageView
import com.fukajima.warframemarket.R
import com.squareup.picasso.Picasso


    fun ImageView.changePicassoPic(avatar:String?){
        if (avatar.isNullOrEmpty())   {
            Picasso.get()
                .load(R.drawable.logo_alpha)
                .into(this)
        }
        else {
            Picasso.get()
                .load(avatar)
                .error(R.drawable.logo_alpha)
                .into(this)
        }


    }
