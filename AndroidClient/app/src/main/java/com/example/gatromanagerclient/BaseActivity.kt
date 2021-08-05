package com.example.gatromanagerclient

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gatromanagerclient.R

open class BaseActivity : AppCompatActivity() {
    public lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    public fun initializeBaseActivityViews() {
        dialog = Dialog(this)
    }


}