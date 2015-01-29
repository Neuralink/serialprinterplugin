package com.neuralink.cordova.serialprinterplugin;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.util.Base64;

import hdx.pwm.PWMControl;

/**
 * Cordova plugin to communicate with the android serial port
 * @author Xavier Seignard <xavier.seignard@gmail.com>
 */
public class SerialPrinterPlugin extends CordovaPlugin {

	ExecutorService pool = Executors.newSingleThreadExecutor(); 
	SerialPrinter mSerialPrinter=SerialPrinter.GetSerialPrinter();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        callbackContext.success("Hello Worlda!");

		try
		{
			PWMControl.PrinterEnable(1);
			try {
				mSerialPrinter.enlargeFontSize(1,2);
				mSerialPrinter.printString("HELLOW");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}finally{
			PWMControl.PrinterEnable(0);
		}

        return true;
    }    

}


