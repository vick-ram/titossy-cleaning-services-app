package com.example.titossycleaningservicesapp.core

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import javax.inject.Inject

class WebsocketService @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    private var websocket: WebSocket? = null

    fun connect(url: String) {
        val request = okhttp3.Request.Builder().url(url).build()
        websocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebsocketService", "onOpen")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d("WebsocketService", "onMessage: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d("WebsocketService", "onMessage: $bytes")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d("WebsocketService", "onClosing: $code, $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d("WebsocketService", "onClosed: $code, $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.d("WebsocketService", "onFailure: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        websocket?.send(message)
    }
}