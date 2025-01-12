package com.example.proof_of_concept.Api_Helper

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

suspend fun sendRequestWithAuthorizationAndBody(
    url: String,
    apiKey: String,
    jsonBody: String,
    mediaType: MediaType,
    callback: (jsonObject: JSONObject) -> Unit
) {
    val client = OkHttpClient()
    val body = jsonBody.toRequestBody(mediaType)

    val request = Request.Builder()
        .url(url)
        .addHeader("Authorization", apiKey)
        .post(body)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("RequestHandler", "Message: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseAsJson = JSONObject(response.body!!.string())
            callback(responseAsJson)
        }
    })
}

suspend fun sendRequestWithBody(URL: String, query: String, mediaType: MediaType, callback: (jsonObject: JSONObject) -> Unit) {
    val client = OkHttpClient()
    val body = query.toRequestBody(mediaType)

    val request = Request.Builder()
        .url(URL)
        .post(body)
        .build()

    client.newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("RequestHandler", "Message: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseAsJson = JSONObject(response.body!!.string())
            callback(responseAsJson)
        }
    })
}

suspend fun sendRequest(URL: String, callback: (jsonObject: JSONObject) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(URL)
        .build()

    client.newCall(request).enqueue(object: Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.e("RequestHandler", "Message: ${e.message}")
        }

        override fun onResponse(call: Call, response: Response) {
            val responseAsJson = JSONObject(response.body!!.string())
            callback(responseAsJson)
        }
    })
}