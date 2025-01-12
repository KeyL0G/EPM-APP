package com.example.proof_of_concept.Api_Helper

import android.util.Log
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun sendRequestWithAuthorizationAndBody(
    url: String,
    apiKey: String,
    jsonBody: String,
    mediaType: MediaType
): JSONObject {
    return suspendCoroutine { continuation ->
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
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseAsJson = JSONObject(response.body!!.string())
                    continuation.resume(responseAsJson)
                } catch (e: Exception) {
                    Log.e("RequestHandler", "Parsing error: ${e.message}")
                    continuation.resumeWithException(e)
                }
            }
        })
    }
}

suspend fun sendRequestWithBody(
    url: String,
    jsonBody: String,
    mediaType: MediaType
): JSONObject {
    return suspendCoroutine { continuation ->
        val client = OkHttpClient()
        val body = jsonBody.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RequestHandler", "Message: ${e.message}")
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseAsJson = JSONObject(response.body!!.string())
                    continuation.resume(responseAsJson)
                } catch (e: Exception) {
                    Log.e("RequestHandler", "Parsing error: ${e.message}")
                    continuation.resumeWithException(e)
                }
            }
        })
    }
}

suspend fun sendRequest(
    url: String
): JSONObject {
    return suspendCoroutine { continuation ->
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("RequestHandler", "Message: ${e.message}")
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val responseAsJson = JSONObject(response.body!!.string())
                    continuation.resume(responseAsJson)
                } catch (e: Exception) {
                    Log.e("RequestHandler", "Parsing error: ${e.message}")
                    continuation.resumeWithException(e)
                }
            }
        })
    }
}
