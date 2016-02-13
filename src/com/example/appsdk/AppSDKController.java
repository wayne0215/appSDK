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
				//切到后台
				bActive = false;
				Log.i("appSdk", "切到后台");
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
				//切到前台
				Log.i("appSdk", "唤醒");
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
	            // activity.finish();//此处不用finish
	            activity = null;
	            if(activityStack.size() <= 0 && application != null){
	            	//关闭app
	            	Log.i("appSdk", "关闭");
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
			//异常捕获初始化
			CrashReportHelper.set(app);
			bActive = true;
			//程序启动
			Log.i("appSdk", "启动");
		}
	}
	
	/**
     * 添加Activity到栈
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }
    
    /** 判断程序是否在前台运行 */
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
		//关闭app
    	Log.i("appSdk", "关闭");
		activityStack.clear();
		application.unregisterActivityLifecycleCallbacks(callback);
		application = null;
		//Runtime.getRuntime().exit(0);
	}
}
