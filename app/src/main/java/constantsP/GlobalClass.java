
package constantsP;

import android.util.DisplayMetrics;

import com.example.bluetoothlibrary.*;

import database.BplOximterdbHelper;
import database.DatabaseManager;
import logger.Level;
import logger.Logger;

import static android.content.ContentValues.TAG;


public class GlobalClass extends Config {

    String username;

    String userType;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        get_density();
        // Thread.setDefaultUncaughtExceptionHandler(new ForceCloseHandlerException(getApplicationContext()));
        DatabaseManager.initializeInstance(new BplOximterdbHelper(getApplicationContext()));
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.log(Level.ERROR,"***Application Class***","App got crashed due to low memory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Logger.log(Level.ERROR, "***Application Class***", "Application class terminated");
    }

    public  boolean isActivityVisible() {
        return activityVisible;
    }

    public  void activityResumed() {
        activityVisible = true;
    }

    public  void activityPaused() {
        activityVisible = false;
    }

    public  boolean activityVisible;


    // get Screen density of phones

    private void get_density()
    {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int density= metrics.densityDpi;
        Logger.log(Level.DEBUG,TAG,"***Get Screen density***="+density);
    }

}
