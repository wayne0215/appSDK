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
	    	// �ռ��쳣��Ϣ ���ҷ��͵�������  
	    	sendCrashReport(ex);  
	    	// �ȴ�����  
	    	try {  
	    		Thread.sleep(500);  
	    	} catch (InterruptedException e) {  
	    	}  
	    	// �����쳣  
	     	  handleException();  
	    }  
	    
	    private void sendCrashReport(Throwable ex) {  
	       StringBuffer exceptionStr = new StringBuffer();  
	       exceptionStr.append(ex.getMessage());  
	       StackTraceElement[] elements = ex.getStackTrace();  
	       for (int i = 0; i < elements.length; i++) {  
	           exceptionStr.append(elements[i].toString());  
	       }  
	       
	       //�����ռ�����Crash��Ϣ��������  
	       String result = exceptionStr.toString().replaceAll(":", "/");
	       Log.i("appSdk", "exception="+result);
	    }  
	  
	    private void handleException() {  
	    	System.exit(0);
	    }  
	}
	

}
