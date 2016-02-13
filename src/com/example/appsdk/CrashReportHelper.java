package com.example.appsdk;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class CrashReportHelper {

	public static void set(Application app){
		UncaughtExceptionHandler handler = new DefaultExceptionHandler(app.getApplicationContext());
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}
	
	
	public static class DefaultExceptionHandler implements UncaughtExceptionHandler {  
	    private Context act = null;  
	    public DefaultExceptionHandler(Context act) {  
	       this.act = act;  
	    }  
	  
	    @Override  
	    public void uncaughtException(Thread thread, Throwable ex) {  
	    	ex.printStackTrace();
	    	// 收集异常信息 并且发送到服务器  
	    	sendCrashReport(ex);  
	    	// 等待半秒  
	    	try {  
	    		Thread.sleep(500);  
	    	} catch (InterruptedException e) {  
	    	}  
	    	// 处理异常  
	     	  handleException();  
	    }  
	    
	    private void sendCrashReport(Throwable ex) {  
	       StringBuffer exceptionStr = new StringBuffer();  
	       exceptionStr.append(ex.getMessage());  
	       StackTraceElement[] elements = ex.getStackTrace();  
	       for (int i = 0; i < elements.length; i++) {  
	           exceptionStr.append(elements[i].toString());  
	       }  
	       
	       //发送收集到的Crash信息到服务器  
	       String result = exceptionStr.toString().replaceAll(":", "/");
	       Log.i("appSdk", "exception="+result);
	    }  
	  
	    private void handleException() {  
	    	System.exit(0);
	    }  
	}
	

}
