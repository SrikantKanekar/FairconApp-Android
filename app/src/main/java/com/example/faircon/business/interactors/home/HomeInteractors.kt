package com.example.faircon.business.interactors.home

class HomeInteractors(
    val syncController: SyncController,
    val getParameters: GetParameters,
    val setMode: SetMode,
    val connectToFaircon: ConnectToFaircon,
    val disconnectFromFaircon: DisconnectFromFaircon
)