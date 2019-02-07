package logger;

import android.util.Log;


public class Logger {

    private static final boolean isLoggingenbaled=true;


    public static void log(int level, String className, String message) {
        if (!isLoggingenbaled)
            return;



        if (message == null || className == null) {
            return;
        }

        if (level == Level.DEBUG) {
            Log.d(className, message);
        } else if (level == Level.INFO) {
            Log.i(className, message);
        } else if (level == Level.ERROR) {
            Log.e(className, message);
        } else if (level == Level.WARNING) {
            Log.w(className, message);
        }else if(level==Level.TRACE){
            Log.wtf(className,message);
        }





    }
}
