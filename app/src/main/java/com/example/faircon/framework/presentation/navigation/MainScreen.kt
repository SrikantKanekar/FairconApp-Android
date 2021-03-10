package com.example.faircon.framework.presentation.navigation

sealed class MainScreen(
    val route: String,
){
    object Home: MainScreen("Home")

    object Controller: MainScreen("Controller")

    object Setting: MainScreen("Setting")
}