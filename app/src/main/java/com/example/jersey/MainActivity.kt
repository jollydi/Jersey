package com.example.jersey

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_edit.*
import kotlinx.android.synthetic.main.dialog_edit.view.*

class MainActivity : AppCompatActivity() {

    var previousName: String = "ANDROID"
    var previousNumber: String = "17"
    var jersey: Jersey = Jersey("ANDROID", 17, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            showEditDialog()
        }
        updateView()
    }

    private fun showEditDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.edit_dialog_title))
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_edit, null, false)
        builder.setView(view)
        view.name_edit_text.setText(jersey.playerName)
        view.number_edit_text.setText(jersey.playerNumber.toString())
        if (!jersey.isRed) {
            view.toggle_button.toggle()
        }
        builder.setPositiveButton(android.R.string.ok) {_, _ -> {}
            jersey.playerName = view.name_edit_text.text.toString()
            jersey.playerNumber = view.number_edit_text.text.toString().toInt()
            jersey.isRed = view.toggle_button.text == getString(R.string.red)
            updateView()
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    private fun updateView() {
        name_text_view.text = jersey.playerName
        number_text_view.text = jersey.playerNumber.toString()
        if(jersey.isRed) {
            image_view.setImageResource(R.drawable.red_jersey)
        } else {
            image_view.setImageResource(R.drawable.blue_jersey)
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
