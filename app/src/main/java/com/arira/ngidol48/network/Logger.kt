package com.arira.ngidol48.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

class Logger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "Logger"
        if (message.startsWith("{") || message.startsWith("[")) {
            try {

                val prettyPrintJson = GsonBuilder().setPrettyPrinting()
                    .create().toJson(JsonParser().parse(message))

                prettyPrintJson.lines().forEach { Log.d(logName, it) }
//                Log.d(logName, "\n"+prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                Log.d(logName, message)
            }
        } else {
            Log.d(logName, message)
            return
        }
    }
}