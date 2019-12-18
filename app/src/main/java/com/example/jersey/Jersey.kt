package com.example.jersey

data class Jersey(var playerName: String, var playerNumber: Int, var isRed: Boolean) {

    companion object {
        val PREFS = "PREFS"
        val KEY_NAME = "KEY_NAME"
        val KEY_NUMBER = "KEY_NUMBER"
        val KEY_IS_RED = "KEY_IS_RED"
    }
}