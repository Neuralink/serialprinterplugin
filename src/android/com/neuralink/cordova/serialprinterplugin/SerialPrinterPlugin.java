package com.neuralink.cordova.serialprinterplugin;

import hdx.pwm.PWMControl;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.SystemClock;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hdx.lib.printer.*;
import com.hdx.lib.serial.SerialParam;
import com.hdx.lib.serial.SerialPortOperaion;
import com.hdx.lib.serial.SerialPortOperaion.SerialReadData;


/**
 * Cordova plugin to communicate with the android serial port
 * @author Xavier Seignard <xavier.seignard@gmail.com>
 */
public class SerialPrinterPlugin extends CordovaPlugin {

	ExecutorService pool = Executors.newSingleThreadExecutor(); 
	SerialPrinter mSerialPrinter=SerialPrinter.GetSerialPrinter();

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
			mSerialPrinter.OpenPrinter(new SerialParam(115200,"/dev/ttyS2",0),new SerialDataHandler());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		if(action.equals("BUZZ")){
			PWMControl.EnableBuzze(1);
			SystemClock.sleep(500);
			PWMControl.EnableBuzze(0);
		};

		if(action.equals("LIGHTSON")){
			PWMControl.SetCameraBacklightness(50);
		};
		
		if(action.equals("printString")){
			try {
				PWMControl.PrinterEnable(1);
				SystemClock.sleep(500);
				mSerialPrinter.printString(args.get(0).toString());
				mSerialPrinter.sendLineFeed();
				//PWMControl.PrinterEnable(0);
			} finally {
				PWMControl.EnableBuzze(1);
				SystemClock.sleep(100);
				PWMControl.EnableBuzze(0);
				PWMControl.EnableBuzze(1);
				SystemClock.sleep(100);
				PWMControl.EnableBuzze(0);
	
				callbackContext.success("Printing Done!" + args.get(0).toString());
			}
		};
/*
		if(action.equals("enlargeFontSize")){
			mSerialPrinter.enlargeFontSize(
				Integer.parseInt(args.get(0).toString())
				,Integer.parseInt(args.get(1).toString())
			);
		};
*/
		if(action.equals("sendLineFeed")){
			mSerialPrinter.sendLineFeed();
		};
	
		if(action.equals("printIMG")){

			try {
				PWMControl.PrinterEnable(1);
				SystemClock.sleep(500);
				FileInputStream is = new FileInputStream("/mnt/sdcard/logo.bmp");
				BitmapDrawable bmpDraw = new BitmapDrawable(is);
				Bitmap bmp = bmpDraw.getBitmap();
				mSerialPrinter.PrintBmp(100,bmp);					
				is.close();
			} catch (Exception e) {
			  e.printStackTrace();
			} finally {
				PWMControl.EnableBuzze(1);
				SystemClock.sleep(100);
				PWMControl.EnableBuzze(0);
				PWMControl.EnableBuzze(1);
				SystemClock.sleep(100);
				PWMControl.EnableBuzze(0);
			}
		};

		if(action.equals("printCode")){
			try {
				SystemClock.sleep(500);
				mSerialPrinter.printBarCode(0,new byte[]{1,2,3,4,5,6,7,8,9,6,6});
			} catch (Exception e) {
			  e.printStackTrace();
			} finally {
				PWMControl.EnableBuzze(1);
				SystemClock.sleep(100);
				PWMControl.EnableBuzze(0);
				PWMControl.EnableBuzze(1);
				SystemClock.sleep(100);
				PWMControl.EnableBuzze(0);
			}
				
		};
				
        return true;
    }    

	private class SerialDataHandler extends Handler{
        public void handleMessage(Message msg) {
			switch (msg.what) {
                case SerialPortOperaion.SERIAL_RECEIVED_DATA_MSG:
                	SerialReadData data = (SerialReadData)msg.obj;
                	StringBuilder sb=new StringBuilder();
                	for(int x=0;x<data.size;x++)
						sb.append(String.format("%02x", data.data[x]));
                	             	
            }
        }
	}	
	
}


