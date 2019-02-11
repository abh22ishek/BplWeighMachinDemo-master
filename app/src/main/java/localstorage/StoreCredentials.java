package localstorage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bluetoothlibrary.entity.Constant;

import constantsP.Constants;
import logger.Level;
import logger.Logger;



public class StoreCredentials {

    public static SharedPreferences save_signupcredentials(Context context,String user_name,String TAG,String userType)
    {

        SharedPreferences signup_credentials;
        //=SignUpActivity.this.getPreferences(Context.MODE_PRIVATE) ;
        signup_credentials=context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = signup_credentials.edit();
        editor.putString(Constants.USER_NAME,user_name);
        editor.putString(Constants.USE_TYPE,userType);
       // editor.putString(ConstantsP.PASSWORD, password.getText().toString());
        editor.apply();

        Logger.log(Level.DEBUG, TAG, "shared preference s file gets stored");

       // globalVariable.setUsername(email_id.getText().toString());
        return  signup_credentials;
    }


    // logged out

    public static void loggedout(Context context)
    {
        SharedPreferences signup_credentials;
        //=SignUpActivity.this.getPreferences(Context.MODE_PRIVATE) ;
        signup_credentials=context.getSharedPreferences(Constants.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = signup_credentials.edit();
        editor.putString(Constants.USER_NAME,null);
        editor.putString(Constants.PASSWORD,null);
        editor.apply();
        Logger.log(Level.DEBUG, context.getClass().getSimpleName(), "shared preference s file all values is set to null");
    }



// store the image uri or image of user

    public static SharedPreferences store_profile_image(Context context,String image,String TAG,String key_username)
    {

        SharedPreferences profile_image;
        //=SignUpActivity.this.getPreferences(Context.MODE_PRIVATE) ;
        profile_image=context.getSharedPreferences(Constants.PREFERENCE_PROFILE_IMAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = profile_image.edit();
        editor.putString(key_username,image);
        editor.apply();


        Logger.log(Level.DEBUG, TAG, "shared preference s file gets stored"+image);
        // globalVariable.setUsername(email_id.getText().toString());
        return  profile_image;
    }


}
