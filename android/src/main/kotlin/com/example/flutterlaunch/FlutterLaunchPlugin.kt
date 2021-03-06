package com.example.flutterlaunch

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.PluginRegistry.Registrar
import android.content.pm.PackageManager
import android.net.Uri
import java.net.URLEncoder


class FlutterLaunchPlugin(val mRegistrar: Registrar): MethodCallHandler {

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar): Unit {
            val channel = MethodChannel(registrar.messenger(), "flutter_launch")
            channel.setMethodCallHandler(FlutterLaunchPlugin(registrar))
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result): Unit {
        try {
            val context: Context = mRegistrar.context()
            val pm: PackageManager = context.packageManager

            if (call.method.equals("launchWathsApp")) {

                val phone: String = call.argument("phone")//"5534992016100"
                val message: String = call.argument("message")//"Olá, thyago."

                val url: String = "https://api.whatsapp.com/send?phone=$phone&text=${URLEncoder.encode(message, "UTF-8")}"

                if (whatsappInstalledOrNot("com.whatsapp")) {
                    val intent: Intent = Intent(Intent.ACTION_VIEW)
                    intent.setPackage("com.whatsapp")
                    intent.setData(Uri.parse(url))

                    if (intent.resolveActivity(pm) != null) {
                        context.startActivity(intent)
                    }
                }
            }

        } catch (e: PackageManager.NameNotFoundException) {
            result.error("Name not found", e.message, null)
        }
    }

    private fun whatsappInstalledOrNot(uri: String) : Boolean {
        val context: Context = mRegistrar.context();
        val pm: PackageManager = context.packageManager
        var appInstalled: Boolean

        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            appInstalled = true
        } catch (e: PackageManager.NameNotFoundException) {
            appInstalled = false
        }

        return appInstalled
    }
}
