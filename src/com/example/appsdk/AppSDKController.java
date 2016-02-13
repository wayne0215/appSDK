package com.example.appsdk;

import java.util.List;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class AppSDKController {
	private static Stack<Activity> activityStack;
	private static Application application;
	private static boolean bActive = false;
	@SuppressLint("NewApi")
	public static ActivityLifecycleCallbacks callback = new ActivityLifecycleCallbacks() {
		
		@Override
		public void onActivityStopped(Activity activity) {
			// TODO Auto-generated method stub
			if(!isAppOnForeground()){
				//�е���̨
				bActive = false;
				Log.i("appSdk", "�е���̨");
			}
		}
		
		@Override
		public void onActivityStarted(Activity activity) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onActivityResumed(Activity activity) {
			// TODO Auto-generated method stub
			if(!bActive){
				//�е�ǰ̨
				Log.i("appSdk", "����");
			}
			bActive = true;
		}
		
		@Override
		public void onActivityPaused(Activity activity) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onActivityDestroyed(Activity activity) {
			// TODO Auto-generated method stub
			
			if (activity != null) {
	            activityStack.remove(activity);
	            // activity.finish();//�˴�����finish
	            activity = null;
	            if(activityStack.size() <= 0 && application != null){
	            	//�ر�app
	            	Log.i("appSdk", "�ر�");
					application.unregisterActivityLifecycleCallbacks(callback);
					application = null;
					Runtime.getRuntime().exit(0);
				}
	        }
		}
		
		@Override
		public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			addActivity(activity);
		}
	};
	
	@SuppressLint("NewApi")
	public static void init(Application app){
		if(app != null){
			application = app;
			application.registerActivityLifecycleCallbacks(callback);
			//�쳣�����ʼ��
			CrashReportHelper.set(app);
			bActive = true;
			//��������
			Log.i("appSdk", "����");
		}
	}
	
	/**
     * ���Activity��ջ
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    
    /** �жϳ����Ƿ���ǰ̨���� */
	public static boolean isAppOnForeground() {  
        // Returns a list of application processes that are running on the  
        // device  
           
        ActivityManager activityManager = (ActivityManager) application.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);  
        String packageName = application.getApplicationContext().getPackageName();  

        List<RunningAppProcessInfo> appProcesses = activityManager  
                        .getRunningAppProcesses();  
        if (appProcesses == null)  
                return false;  

        for (RunningAppProcessInfo appProcess : appProcesses) {  
                // The name of the process that this object is associated with.  
                if (appProcess.processName.equals(packageName)  
                                && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {  
                        return true;  
                }  
        }  

        return false;  
	}  
	
	@SuppressLint("NewApi")
	public static void ExitAppSDK(){
		//�ر�app
    	Log.i("appSdk", "�ر�");
		activityStack.clear();
		application.unregisterActivityLifecycleCallbacks(callback);
		application = null;
		//Runtime.getRuntime().exit(0);
	}
}
