package com.example.faircon.framework.datasource.network.webSocket

import com.example.faircon.business.domain.model.WebSocketEvent
import com.example.faircon.business.domain.util.printLogD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener : WebSocketListener() {

    private val _socketUpdate = MutableSharedFlow<WebSocketEvent>()
    val socketUpdate: SharedFlow<WebSocketEvent> = _socketUpdate

    override fun onOpen(webSocket: WebSocket, response: Response) {
        printLogD("WebSocketListener", "onOpen: called")
        emit(WebSocketEvent(isConnected = true))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        printLogD("WebSocketListener", "onMessage: called")
        emit(WebSocketEvent(message = text))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        printLogD("WebSocketListener", "onClosing: called")
        emit(WebSocketEvent(isConnected = false))
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        printLogD("WebSocketListener", "onClosed: called")
        emit(WebSocketEvent(isConnected = false))
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        printLogD("WebSocketListener", "onFailure: called")
        emit(
            WebSocketEvent(
                isConnected = false,
                exception = t
            )
        )
    }

    private fun emit(update: WebSocketEvent) {
        CoroutineScope(IO).launch {
            _socketUpdate.emit(update)
        }
    }
}