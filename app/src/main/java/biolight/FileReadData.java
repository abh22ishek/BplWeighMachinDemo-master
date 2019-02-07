package biolight;

import android.os.*;

import java.io.*;

import logger.*;

public class FileReadData {

    public static final String TAG=FileReadData.class.getSimpleName();

    public static void readfile(String filedir,String filename)

    {
        File file = new File(Environment.getExternalStorageDirectory(),filedir);
        try{
            File[] dirFiles = file.listFiles();
            StringBuffer sb=new StringBuffer();
            if (dirFiles.length != 0) {
                // loops through the array of files, outputing the name to console
                for (int i = 0; i < dirFiles.length; i++) {
                    if(dirFiles[i].getName().equalsIgnoreCase(filename));
                    {
                        if(file.exists())
                        {
                            try
                            {
                                BufferedReader buf=new BufferedReader(new FileReader(dirFiles[i]));
                                String line;
                                while ((line=buf.readLine())!=null)
                                {
                                    sb.append(line);
                                    sb.append("\n");
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            finally {
                                // ConvertImagetoPdf.covertImagetoPdf(sb.toString(),type);
                            }
                        }
                        else{
                            Logger.log(Level.INFO, TAG, "File doesn't exists");
                        }
                    }

                }

            }


        }catch(Exception e)
        {
            e.printStackTrace();
        }





    }


}
