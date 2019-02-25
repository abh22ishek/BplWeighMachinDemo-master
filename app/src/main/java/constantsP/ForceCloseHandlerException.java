package constantsP;

import android.content.*;
import android.os.*;
import android.text.format.*;

import java.io.*;

import test.bpl.com.bplscreens.*;

public class ForceCloseHandlerException implements Thread.UncaughtExceptionHandler {
    private final Context mContext;
    private final String LINE_SEPARATOR = "\n";
    File sLogFile;
    OutputStreamWriter sFOutWriter;

    public ForceCloseHandlerException(Context context) {
        mContext = context;
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

        StringWriter stackTrace = new StringWriter();
        ex.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        errorReport.append(DateFormat.format("dd-MM-yyyy::hh:mm:ss",
                System.currentTimeMillis()));

        errorReport.append("\r\n");

       /* try {

            if (sFOutWriter != null) {
                sFOutWriter.append(errorReport.toString());
                sFOutWriter.flush();
                // fOutWriter.close();
                // fOut.close();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        Intent intent = new Intent(mContext, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("error", errorReport.toString());
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);

    }


    // store error report into a file
  /*  public  boolean init() {
         sLogFile = new File(Environment.getExternalStorageDirectory().getPath()
                + "/BPl Be Well_userlog.txt");

        if (!sLogFile.exists()) {
            try {
                boolean ok = sLogFile.createNewFile();
                if (ok) {
                    Log.i("Log", "file is created");
                } else {
                    Log.i("Log", "file creation failed");
                    return false;
                }
                // sLogFile.setWritable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // Creting in append mode
            FileOutputStream fOut = new FileOutputStream(sLogFile, true);
            sFOutWriter = new OutputStreamWriter(fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }*/
}
