package com.borntowindevelopers.whitelogics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver
{

//    MainActivity object;
    @Override
    public void onReceive(Context context, Intent intent)
    {
//        object = new MainActivity();
        Log.i("App", "called receiver method");
        try{
            Utils.generateNotification(context);
//            object.setNotification();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

}