package com.example.jersey

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.InputFilter
import android.view.LayoutInflater
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_edit.*
import kotlinx.android.synthetic.main.dialog_edit.view.*
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    private var previousName: String = "ANDROID"
    private var previousNumber: Int = 17
    private var previousIsRed: Boolean = true
    private var jersey: Jersey = Jersey("ANDROID", 17, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            showEditDialog()
        }
        retrieveSharedPreferencesData()
        updateView()
    }

    private fun retrieveSharedPreferencesData() {
        val prefs = getSharedPreferences(Jersey.PREFS, Context.MODE_PRIVATE)
        val name = prefs.getString(Jersey.KEY_NAME, getString(R.string.default_jersey_name)) ?: getString(R.string.default_jersey_name)
        val number = prefs.getInt(Jersey.KEY_NUMBER, 17)
        val isRed = prefs.getBoolean(Jersey.KEY_IS_RED, true)
        jersey.playerName = name
        jersey.playerNumber = number
        jersey.isRed = isRed
    }

    override fun onPause() {
        super.onPause()
        val prefs = getSharedPreferences(Jersey.PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(Jersey.KEY_NAME, jersey.playerName)
        editor.putInt(Jersey.KEY_NUMBER, jersey.playerNumber)
        editor.putBoolean(Jersey.KEY_IS_RED, jersey.isRed)
        editor.commit()
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
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
            jersey.playerName = view.name_edit_text.text.toString()
            try {
                jersey.playerNumber = view.number_edit_text.text.toString().toInt()
            } catch (e: NumberFormatException) {
                jersey.playerNumber = 0
            }
            jersey.isRed = view.toggle_button.text == getString(R.string.red)
            updateView()
        }
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }

    private fun updateView() {
        name_text_view.text = jersey.playerName
        number_text_view.text = jersey.playerNumber.toString()
        if (jersey.isRed) {
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
            R.id.action_settings -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.reset -> {
                storeJerseyInfo()
                resetJersey()
                updateView()
                showSnackbar()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun resetJersey() {
        jersey.playerName = getString(R.string.default_jersey_name)
        jersey.playerNumber = 17
        jersey.isRed = true
    }

    private fun storeJerseyInfo() {
        previousName = jersey.playerName
        previousNumber = jersey.playerNumber
        previousIsRed = jersey.isRed
    }

    private fun retrieveJerseyInfo() {
        jersey.playerName = previousName
        jersey.playerNumber = previousNumber
        jersey.isRed = previousIsRed
    }

    private fun showSnackbar() {
        var snackbar = Snackbar.make(findViewById(R.id.coordinator_layout),"Jersey cleared", Snackbar.LENGTH_LONG)
        snackbar.setAction("UNDO", View.OnClickListener {
            retrieveJerseyInfo()
            updateView()
        }).show()
    }
}
