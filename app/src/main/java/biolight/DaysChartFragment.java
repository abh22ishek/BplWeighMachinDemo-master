package biolight;


import android.*;
import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.graphics.*;
import android.net.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v4.content.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import com.example.bluetoothlibrary.entity.*;
import com.tom_roush.harmony.awt.*;
import com.tom_roush.pdfbox.pdmodel.*;
import com.tom_roush.pdfbox.pdmodel.common.*;
import com.tom_roush.pdfbox.pdmodel.font.*;
import com.tom_roush.pdfbox.pdmodel.graphics.image.*;
import com.tom_roush.pdfbox.util.*;

import java.io.*;
import java.lang.reflect.*;
import java.text.*;
import java.util.*;

import constantsP.*;
import logger.*;
import test.bpl.com.bplscreens.*;
import test.bpl.com.bplscreens.R;

public class DaysChartFragment extends Fragment{


    private final String TAG=DaysChartFragment.class.getSimpleName();
    String selectedDate;
    List<BPMeasurementModel> mRecordDetail;

    CustomBPDayChart custom_day_chart;
    PulseDayChart pulseDayChart;
    TextView date,showVal,showVal2;

    CustomBPDayChartYaxis customBPDayChartYaxis;

    ImageView pdf;

    String userName,age,gender;
    List<String> commentsList;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.days_chart_frag,container,false);
        date=  view.findViewById(R.id.date);

        custom_day_chart= view.findViewById(R.id.custom_day_chart);
        pulseDayChart=  view.findViewById(R.id.pulse_day_chart);
        customBPDayChartYaxis=  view.findViewById(R.id.custom_day_chartYaxis);
        showVal=  view.findViewById(R.id.showVal);
        pdf= view.findViewById(R.id.pdf);
        showVal2= view.findViewById(R.id.showVal2);
        return view;
    }

    String globalUserName;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(getArguments().getSerializable(Constants.CHART)!=null){

           mRecordDetail= (List<BPMeasurementModel>) getArguments().getSerializable(Constants.CHART);
            Logger.log(Level.WARNING,TAG,"Get serializable list="+mRecordDetail.size());
            commentsList=new ArrayList<>();
        }



        if(getArguments().getString("user")!=null){
            userName=getArguments().getString("user");
        }


        if(getArguments().getString("global_user")!=null){
            globalUserName=getArguments().getString("global_user");
        }

        if(getArguments().getString("age")!=null){
            age=getArguments().getString("age");
        }

        if(getArguments().getString("gender")!=null){
            gender=getArguments().getString("gender");
        }


        if(getArguments().getString(Constants.DATE)!=null){
            selectedDate=getArguments().getString(Constants.DATE);
        }


        date.setText(selectedDate.substring(0,10));



        custom_day_chart.setHorizontalLabels(sameDateRecords(mRecordDetail,selectedDate));
        customBPDayChartYaxis.getRecords(mRecordDetail);
        pulseDayChart.setHorizontalLabel(sameDateRecords(mRecordDetail,selectedDate));


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isStoragePermissionGranted())
                {
                    Toast.makeText(getActivity(),"Storage Permission is necessary to generate PDF ",Toast.LENGTH_SHORT).show();

                }

            }
        });

        showVal.setText(R.string.sho);
        showVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isshowVal){
                    custom_day_chart.showValues(true);
                    pulseDayChart.showValues(true);
                    isshowVal=true;
                    showVal.setText(R.string.hide);
                    showVal.setBackground(getResources().getDrawable(R.drawable.switch_btn_bg));

                    showVal2.setText(R.string.hide);
                    showVal2.setBackground(getResources().getDrawable(R.drawable.switch_btn_bg));

                }else{
                    isshowVal=false;

                    showVal.setBackground(getResources().getDrawable(R.drawable.savebutton));
                    showVal.setText(R.string.sho);

                    custom_day_chart.showValues(false);
                    pulseDayChart.showValues(false);

                    showVal2.setBackground(getResources().getDrawable(R.drawable.savebutton));
                    showVal2.setText(R.string.sho);

                }


            }
        });


        showVal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isshowVal){
                    custom_day_chart.showValues(true);
                    pulseDayChart.showValues(true);
                    isshowVal=true;
                    showVal2.setText(R.string.hide);
                    showVal2.setBackground(getResources().getDrawable(R.drawable.switch_btn_bg));

                    showVal.setText(R.string.hide);
                    showVal.setBackground(getResources().getDrawable(R.drawable.switch_btn_bg));

                }else{
                    isshowVal=false;

                    showVal2.setBackground(getResources().getDrawable(R.drawable.savebutton));
                    showVal2.setText(R.string.sho);

                    custom_day_chart.showValues(false);
                    pulseDayChart.showValues(false);

                    showVal.setBackground(getResources().getDrawable(R.drawable.savebutton));
                    showVal.setText(R.string.sho);

                }
            }
        });

    }



    boolean isshowVal;
    private List<BPMeasurementModel> sameDateRecords(List<BPMeasurementModel> mRecordDetailList,String selectedDate)
    {
        final List<BPMeasurementModel> listToReturn = new ArrayList<>();


        for(BPMeasurementModel b:mRecordDetailList){
            if(selectedDate.equals(b.getMeasurementTime().substring(0,10))){
                listToReturn.add(b);
            }
        }

        Logger.log(Level.WARNING,TAG,"****Record with same date****"+listToReturn.size());
        return listToReturn;
    }











    @SuppressLint("StaticFieldLeak")
    class EcgPdf extends AsyncTask<String,String,String> {




        ProgressDialog progressDialog;

        String isloaded = "false";


        @Override
        protected String doInBackground(String... strings) {

            try {
                createPdf();
                isloaded = "true";
            } catch (Exception e) {
                isloaded = "false";
                e.printStackTrace();
            }


            return isloaded;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait ......");

                progressDialog.setCancelable(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

            }


        }


        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                if (isloaded.equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(),
                            "PDF  Successfully created", Toast.LENGTH_SHORT).show();


                    onCreateDialog();
                }

            }
        }
    }
    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("PDF File Created. Choose Any option");

// add a radio button list
        final String[] colors = {"View PDF", "Share PDF"};
        final int checkedItem = -1; // cow

        builder.setSingleChoiceItems(colors, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
                if(which!=-1){
                    // Write code for share
                    if(which==0)
                    {
                        // PDF Viewer
                        File mFile;
                        mFile = new File(Environment.getExternalStorageDirectory()+"/"+Constants.BPL_FOLDER+ "/"+globalUserName+"/" +userName
                                +"/"+userName
                                +"_"+DateTime.getDateTimeinMinutes()+"_BPL_iPressure_BT02_DAY_Trend.pdf");

                        Intent target = new Intent(Intent.ACTION_VIEW);
                        target.setDataAndType(Uri.fromFile(mFile),"application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        Intent intent = Intent.createChooser(target, "Open File");
                        try {
                            if(Build.VERSION.SDK_INT>=24){
                                try{
                                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                    m.invoke(null);
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                            startActivity(intent);
                        } catch (Exception e) {
                            // Instruct the user to install a PDF reader here, or something
                            e.printStackTrace();
                            Logger.log(Level.DEBUG,"---","Install any PDF viewer");
                        }
                    }else if(which==1)
                    {
                        // Share
                      File  mFile = new File(Environment.getExternalStorageDirectory()+"/"+Constants.BPL_FOLDER+ "/"+globalUserName+"/" +userName
                                +"/"+userName +"_"+DateTime.getDateTimeinMinutes()+"_BPL_iPressure_BT02_DAY_Trend.pdf");



                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        Uri contentUri = FileProvider.getUriForFile(getActivity(),
                                getActivity().getPackageName() + ".provider", mFile);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.setType("application/pdf");
                        //shareIntent.setType("image/pdf");
                        startActivity(Intent.createChooser(shareIntent, "Share Via"));
                    }else{

                    }


                   // dialog.dismiss();
                }
            }
        });

// add OK and Cancel buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK

                dialog.dismiss();
            }
        });


// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    File file,mFinalFIle;
    String path;


    private void createPdf(){

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));



        float page_height = page.getMediaBox().getHeight();
        float page_width = page.getMediaBox().getWidth();


        PDFBoxResourceLoader.init(getActivity());

        String fileNameDirectory = Constants.BPL_FOLDER;

      file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileNameDirectory);

        if (!file.exists()) {
            file.mkdir();

        }

        path = "";

        File loginFile=new File(file,globalUserName);
        if(!loginFile.exists()){

            loginFile.mkdir();
        }


        File userDir=new File(loginFile,userName);

        if(!userDir.exists()){
            userDir.mkdir();
        }



        try {
            path = userDir.getAbsolutePath() + "/" + userName + "_" +DateTime.getDateTimeinMinutes()+ "_BPL_iPressure_BT02_DAY_Trend.pdf";


        } catch (Exception e) {
            e.printStackTrace();
            Log.i("***Error in File**", "Unable to get File path");
        }


        document.addPage(page);

        float cursorX = 60f;
        float cursorY = 20f;


        PDPageContentStream contentStream = null;


        float unit_per_cm = 28.34f;
        float rect_width = unit_per_cm * 25f;
        float rect_height = unit_per_cm * 18f;

        float marginLowerLine = rect_height + cursorY + 5;
        float marginUpperLine = rect_height + 2 * unit_per_cm;
        float rectangle_X = rect_width + 60;
        float rectangle_Y = marginUpperLine;

        try {
            contentStream = new PDPageContentStream(document, page);
            PDFont font = PDType1Font.HELVETICA_BOLD;
            //contentStream.setNonStrokingColor(200, 200, 200); //gray background

            contentStream.addRect(40f, 16f, rectangle_X, marginUpperLine);
            contentStream.setStrokingColor(AWTColor.BLACK);
            contentStream.setLineWidth(1.4f);

            contentStream.moveTo(40f, marginLowerLine);
            contentStream.lineTo(rectangle_X + 40, marginLowerLine);
            contentStream.stroke();

            //draw text
            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 11);
            contentStream.newLineAtOffset(120f, marginUpperLine + 2);

            if(Constants.SELECTED_USER_TYPE.equalsIgnoreCase("Clinic")){
                contentStream.showText("User Name : " + userName + "   " + "Age : " + age + "   " + "Gender : " +
                        gender+ "  " + "Clinic Name : " + "Bpl Med Tech");
            }else{
                contentStream.showText("User Name : " + userName + "   " + "Age : " + age + "   " + "Gender : " +
                        gender+ "  " );
            }


            contentStream.endText();



            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 11);
            contentStream.newLineAtOffset(120f, marginUpperLine - 12);
            contentStream.showText("Comments :" + "--");
            contentStream.endText();


            contentStream.setLineWidth(0.5f);
            contentStream.setStrokingColor(AWTColor.GRAY);
            contentStream.beginText();
            contentStream.setFont(font, 9);
            contentStream.newLineAtOffset(120f, marginUpperLine - 27);
            contentStream.showText("Mac Id -" + Constants.SELECTED_MAC_ID_BT02+"  "+
                    "Serial No. - "+Constants.SELECTED_SERIAL_NO_ID_BT02+"            "+"App Version -"+BuildConfig.VERSION_NAME);
            contentStream.endText();


            contentStream.setLineWidth(1.4f);
            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 10);
            contentStream.newLineAtOffset(1.9f * unit_per_cm, page_height-2.8f*unit_per_cm);
            contentStream.showText("(BPLBeWell iPressure BT02 Day Report) " );
            contentStream.endText();


            // Add app version
            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 9);
            contentStream.newLineAtOffset(page_width - 120, marginUpperLine + 2f);
            contentStream.showText("Date : "+getDateTime());
            contentStream.endText();






            // add image

            AssetManager assetManager = getActivity().getAssets();
            InputStream is = null;
            InputStream alpha;
            is = assetManager.open("bpl.png");
            //alpha =  assetManager.open("bpl.png");

            // Bitmap b= BitmapFactory.decodeStream(alpha);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            PDImageXObject ximage = LosslessFactory.createFromImage(document, bitmap);
            //  PDImageXObject yimage=LosslessFactory.createFromImage(document,b);


            contentStream.drawImage(ximage, 40 + 5f, marginLowerLine + 0.5f);
            //contentStream.drawImage(yimage,rect_width+cursorX-20,marginLowerLine);


            contentStream.moveTo(100f, marginLowerLine + 2f);
            contentStream.lineTo(100f, marginUpperLine + 15);
            contentStream.stroke();


            addNotficationbar(contentStream);
            drawPulseChartonDay(contentStream,sameDateRecords(mRecordDetail,selectedDate));
            drawTables2(contentStream,sameDateRecords(mRecordDetail,selectedDate));
            drawBPChartonDay(contentStream,sameDateRecords(mRecordDetail,selectedDate));


            drawDottedLines(contentStream);



            contentStream.close();
            document.save(path);
            document.close();


        } catch (IOException e) {
            e.printStackTrace();

        }


    }


    public  static String getDateTime()
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return  df.format(date);
    }
    private void addNotficationbar( PDPageContentStream contentStream)
    {

        float unit_per_cm = 28.34f;
        float graph_width = unit_per_cm * 15f;
        float graph_height = unit_per_cm * 0.7f;

        float startX=12.5f*unit_per_cm;
        float startY=8.7f*unit_per_cm;
        PDFont font = PDType1Font.HELVETICA;

        try {
            contentStream.addRect(startX, startY, graph_width, graph_height);
            contentStream.setStrokingColor(AWTColor.GRAY);
            contentStream.setLineWidth(1f);
            contentStream.stroke();


            contentStream.setStrokingColor(AWTColor.GREEN);
            contentStream.setLineWidth(10f);

            contentStream.moveTo(startX+1.5f*unit_per_cm, startY+4f);
            contentStream.lineTo(startX+1.5f*unit_per_cm, startY+13f);
            contentStream.stroke();

            contentStream.setLineWidth(1.2f);
            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 8f);
            contentStream.newLineAtOffset(startX+(2f*unit_per_cm), startY+5f);
            contentStream.showText("Normal");
            contentStream.endText();


            contentStream.setStrokingColor(AWTColor.YELLOW);
            contentStream.setLineWidth(10f);
            contentStream.moveTo(startX+3.5f*unit_per_cm, startY+4f);
            contentStream.lineTo(startX+3.5f*unit_per_cm, startY+13f);
            contentStream.stroke();

            contentStream.setLineWidth(1.2f);
            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 8f);
            contentStream.newLineAtOffset(startX+(4f*unit_per_cm), startY+5f);
            contentStream.showText("Pre Hypertension");
            contentStream.endText();


            contentStream.setStrokingColor(AWTColor.RED);
            contentStream.setLineWidth(10f);
            contentStream.moveTo(startX+7f*unit_per_cm, startY+4f);
            contentStream.lineTo(startX+7f*unit_per_cm, startY+13f);
            contentStream.stroke();

            contentStream.setLineWidth(1.2f);
            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 8f);
            contentStream.newLineAtOffset(startX+(7.5f*unit_per_cm), startY+5f);
            contentStream.showText("Hypertension");
            contentStream.endText();



        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void drawBPChartonDay(  PDPageContentStream contentStream ,List<BPMeasurementModel> mRecords_){

        float unit_per_cm = 28.34f;
        float graph_width = unit_per_cm * 16f;
        float graph_height = unit_per_cm * 8f;

        float startX=12.5f*unit_per_cm;
        float startY=10*unit_per_cm;

        List<String> coorlist=  custom_day_chart.getColor();
        Logger.log(Level.DEBUG,"--colors on pdf --",""+ coorlist);
        String []strArrVertical= new String[]{"40", "60", "80", "100", "120", "140", "160", "180","200"};
        float x1 = 0,x2=0,y1=0,y2=0;

        float headerY=0;

        PDFont font = PDType1Font.HELVETICA;
        PDFont fontSeries = PDType1Font.HELVETICA_BOLD;
        try {
            /*contentStream.addRect(startX, startY, graph_width, graph_height);
            contentStream.setStrokingColor(AWTColor.BLACK);
            contentStream.setLineWidth(1.4f);
            contentStream.stroke();
*/


            int px=0;
            contentStream.setLineWidth(0.5f);
            contentStream.setStrokingColor(AWTColor.GRAY);
            float xoffset=  startX;


            float Yoffset= startY;

            for(int i=0;i<strArrVertical.length ;i++){

                if(i==0){
                    contentStream.moveTo(startX, startY);
                    contentStream.lineTo(startX, Yoffset+8*unit_per_cm);
                    contentStream.stroke();
                }

                contentStream.moveTo(startX, startY);
                contentStream.lineTo(graph_width+startX, Yoffset);
                contentStream.stroke();
                Yoffset= (Yoffset+unit_per_cm);
                startY=Yoffset;
                headerY=startY;
            }



            startY=10*unit_per_cm;

            // vertical labels

            for (int i=0;i<strArrVertical.length;i++)
            {
                contentStream.setNonStrokingColor(0, 0, 0); //black text
                contentStream.beginText();
                contentStream.setFont(font, 8);
                contentStream.newLineAtOffset(startX-20, startY);
                contentStream.showText(strArrVertical[i]);
                contentStream.endText();

                startY=startY+unit_per_cm;
            }


            startY=10*unit_per_cm;
            for(int i = 0; i<mRecords_.size();i++){

            /*    contentStream.moveTo(startX, startY);
                contentStream.lineTo(xoffset, startY+graph_height);
                contentStream.stroke();*/




                contentStream.setNonStrokingColor(0, 0, 0); //black text
                contentStream.beginText();
                contentStream.setFont(font, 8);
                contentStream.newLineAtOffset((startX-10)+unit_per_cm, startY-10);
                contentStream.showText(mRecords_.get(i).getMeasurementTime().substring(11,16));
                contentStream.endText();

                xoffset=  xoffset+unit_per_cm;
                startX=xoffset;
            }





            // draw thick line

            // ...............[[[[[[]]]]]]]]]]
            startY=10*unit_per_cm;
            startX=12.5f*unit_per_cm;

            Logger.log(Level.DEBUG,"--dia list on pdf --",""+ mRecordDetail);

            for(int i=0;i<mRecords_.size();i++){
                y1=  mRecords_.get(i).getSysPressure();
                y2=  mRecords_.get(i).getDiabolicPressure();

                if(y1!=0 && y2!=0){
                    y1=(((0.05f*y1)*unit_per_cm)+startY) -(2*unit_per_cm);
                    y2=(((0.05f*y2)*unit_per_cm)+startY) -(2*unit_per_cm);





                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(fontSeries, 6);
                    contentStream.newLineAtOffset((startX-8f)+unit_per_cm, y1+2);
                    contentStream.showText(String.valueOf((int)mRecords_.get(i).getSysPressure()));
                    contentStream.endText();



                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(fontSeries, 6);
                    contentStream.newLineAtOffset((startX-5f)+unit_per_cm, y2-8f);
                    contentStream.showText(String.valueOf((int)mRecords_.get(i).getDiabolicPressure()));
                    contentStream.endText();



                    if(custom_day_chart.getColor().get(i).equalsIgnoreCase("red")){
                        contentStream.setStrokingColor(AWTColor.RED);
                    }else if(custom_day_chart.getColor().get(i).equalsIgnoreCase("green")){
                        contentStream.setStrokingColor(AWTColor.GREEN);
                    }else if(custom_day_chart.getColor().get(i).equalsIgnoreCase("blue")) {
                        contentStream.setStrokingColor(AWTColor.BLUE);

                    }else
                    {
                        contentStream.setStrokingColor(AWTColor.YELLOW);
                    }

                    contentStream.setLineWidth(10f);
                    contentStream.moveTo(startX+unit_per_cm, y1);
                    contentStream.lineTo(startX+unit_per_cm, y2);
                    contentStream.stroke();



                }

                startX=startX+unit_per_cm;
            }



            startX=12.5f*unit_per_cm;
            /*contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 10);
            contentStream.newLineAtOffset(startX-32, 403f);
            contentStream.showText("|");
            contentStream.endText();*/

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontSeries, 11);
            contentStream.newLineAtOffset(startX+6 *unit_per_cm, (headerY+3)-unit_per_cm);
            contentStream.showText("BP(mmHg) Vs Date");
            contentStream.endText();



        } catch (IOException e) {
            e.printStackTrace();
        }



    }
    private void drawDottedLines( PDPageContentStream contentStream )
    {
        float unit_per_cm = 28.34f;
        float graph_width = unit_per_cm * 16f;



        float startX;
        float startY;

        float Yoffset;
        try {

            contentStream.setStrokingColor(AWTColor.GRAY);
            contentStream.setLineWidth(1.4f);

            startY=(10*unit_per_cm)+unit_per_cm/2;
            startX=12.5f*unit_per_cm;

            Yoffset= startY;


            for (int i=0;i<8;i++)
            {
                contentStream.setLineDashPattern (new float[]{1,3}, 0);
                contentStream.moveTo(startX, startY);
                contentStream.lineTo(graph_width+startX, Yoffset);
                contentStream.stroke();
                Yoffset= (Yoffset+unit_per_cm);
                startY=Yoffset;
            }



            graph_width=16f *unit_per_cm;

            startY=(1.2f*unit_per_cm)+unit_per_cm/2;
            Yoffset=startY;

            for (int i=0;i<7;i++)
            {
                contentStream.setLineDashPattern (new float[]{1,3}, 0);
                contentStream.moveTo(startX, startY);
                contentStream.lineTo(graph_width+startX, Yoffset);
                contentStream.stroke();
                Yoffset= (Yoffset+unit_per_cm);
                startY=Yoffset;
            }





        } catch (IOException e) {
            e.printStackTrace();
        }


    }




    private void drawTables2( PDPageContentStream contentStream ,List<BPMeasurementModel> mRecords_) {





            float unit_per_cm = 28.34f;
            float graph_width = unit_per_cm * 9.7f;
            float graph_height = unit_per_cm * 16.8f;

            float startX = 40f;
            float startY = 1.2f * unit_per_cm;
            PDFont font = PDType1Font.HELVETICA;
            PDFont fontH = PDType1Font.HELVETICA_BOLD;



            float xoffset=1.9f * unit_per_cm;
            float yoffset =1.4f *unit_per_cm;
        try {
            contentStream.addRect(startX, startY, graph_width, graph_height);
            contentStream.setStrokingColor(AWTColor.BLACK);
            contentStream.setLineWidth(1.4f);
            contentStream.stroke();




            contentStream.setStrokingColor(AWTColor.BLACK);
            for(int i=30;i>=0 ;i--){

                if(mRecords_.size()>i)
                {
                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(font, 8);
                    contentStream.newLineAtOffset(xoffset, yoffset);
                    contentStream.showText(mRecords_.get(i).getMeasurementTime().substring(11,19));
                    contentStream.endText();



                    contentStream.setLineWidth(1.2f);
                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(font, 8);
                    contentStream.newLineAtOffset(xoffset+2*unit_per_cm, yoffset);



                    contentStream.showText(String.valueOf((int)mRecords_.get(i).getSysPressure()));

                    contentStream.endText();



                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(font, 8);
                    contentStream.newLineAtOffset(xoffset+3*unit_per_cm, yoffset);

                    contentStream.showText(String.valueOf((int)mRecords_.get(i).getDiabolicPressure()));


                    contentStream.endText();

                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(font, 7);
                    contentStream.newLineAtOffset(xoffset+4*unit_per_cm, yoffset);

                    contentStream.showText( Utility.validateTypeBP(
                            String.valueOf(mRecords_.get(i).getSysPressure()),String.valueOf(
                                    mRecords_.get(i).getDiabolicPressure())));

                    contentStream.endText();



                    // comments section
                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(font, 7);
                    contentStream.newLineAtOffset(xoffset+7*unit_per_cm, yoffset);
                    if(mRecords_.get(i).getComments().equals("")){
                        contentStream.showText("-NA-");
                    }else{
                        contentStream.showText(mRecords_.get(i).getComments());

                    }
                    contentStream.endText();


                }


                yoffset=yoffset+unit_per_cm/2;


            }


            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 9);
            contentStream.newLineAtOffset(startX+2, graph_height/2-10);
            contentStream.showText("CLINICAL NOTES  ...");
            contentStream.endText();

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 9);
            contentStream.newLineAtOffset(startX+2, graph_height/2-10);
            contentStream.showText("CLINICAL NOTES  ...");
            contentStream.endText();


            contentStream.setLineWidth(0.8f);
            contentStream.setStrokingColor(AWTColor.GRAY);


            contentStream.moveTo(startX, graph_height/2+10);
            contentStream.lineTo(startX+graph_width, graph_height/2+10);
            contentStream.stroke();


            float px=unit_per_cm;
            for(int i=0;i<7;i++){

                contentStream.moveTo(startX+1, graph_height/2-px);
                contentStream.lineTo(startX+graph_width, graph_height/2-px);
                contentStream.stroke();

                px=px+0.8f*unit_per_cm;

            }

            contentStream.moveTo(startX, graph_height/2+10);
            contentStream.lineTo(startX+graph_width, graph_height/2+10);
            contentStream.stroke();



            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 9);
            contentStream.newLineAtOffset(xoffset, yoffset+4);
            contentStream.showText("Time");
            contentStream.endText();

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 9);
            contentStream.newLineAtOffset(xoffset+2f*unit_per_cm, yoffset+4);
            contentStream.showText("Sys");
            contentStream.endText();



            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 9);
            contentStream.newLineAtOffset(xoffset+3f*unit_per_cm, yoffset+4);
            contentStream.showText("Dia");
            contentStream.endText();

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 9);
            contentStream.newLineAtOffset(xoffset+4f*unit_per_cm, yoffset+4);
            contentStream.showText("Result");
            contentStream.endText();

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 8);
            contentStream.newLineAtOffset(xoffset+7f*unit_per_cm, yoffset+4);
            contentStream.showText("Comments");
            contentStream.endText();

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 10);
            contentStream.newLineAtOffset(xoffset, yoffset+19f);
            contentStream.showText(selectedDate+" (SYS/DIA) mmHg");
            contentStream.endText();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void drawPulseChartonDay(  PDPageContentStream contentStream,List<BPMeasurementModel> mRecords_list ){


        mDarawList=new ArrayList<>();
        oldXvalList=new ArrayList<>();
        newXvalList=new ArrayList<>();


        float unit_per_cm = 28.34f;
        float graph_width = unit_per_cm * 16f;
        float graph_height = unit_per_cm * 7f;

        float startX=12.5f*unit_per_cm;
        float startY=1.2f*unit_per_cm;

        String []strArrVertical= new String[]{"0", "20", "40", "60", "80", "100", "120", "140"};
        float headerY=0;
        PDFont font = PDType1Font.HELVETICA;
        PDFont fontSeries = PDType1Font.HELVETICA_BOLD;
        try {
          /*  contentStream.addRect(startX, startY, graph_width, graph_height);
            contentStream.setStrokingColor(AWTColor.BLACK);
            contentStream.setLineWidth(1.4f);
            contentStream.stroke();*/



            int px=0;
            contentStream.setLineWidth(0.5f);
            contentStream.setStrokingColor(AWTColor.GRAY);
            float xoffset= startX;


            float Yoffset=  startY;
            for(int i=0;i<strArrVertical.length;i++){

                if(i==0){
                    contentStream.moveTo(startX, startY);
                    contentStream.lineTo(startX, Yoffset+7*unit_per_cm);
                    contentStream.stroke();
                }
                contentStream.moveTo(startX, startY);
                contentStream.lineTo(graph_width+startX, Yoffset);
                contentStream.stroke();
                Yoffset=  (Yoffset+unit_per_cm);
                startY=Yoffset;
            }


            startY=1.2f*unit_per_cm;

            // vertical labels

            for (int i=0;i<8;i++)
            {
                contentStream.setNonStrokingColor(0, 0, 0); //black text
                contentStream.beginText();
                contentStream.setFont(font, 8);
                contentStream.newLineAtOffset(startX-20, startY);
                contentStream.showText(strArrVertical[i]);
                contentStream.endText();
                startY=startY+unit_per_cm;
                headerY=startY;
            }


            startY=1.2f*unit_per_cm;



            for(int i = 0; i<mRecords_list.size();i++){


                px=2*i;

                //Add dates
                contentStream.setNonStrokingColor(0, 0, 0); //black text
                contentStream.beginText();
                contentStream.setFont(font, 8);
                contentStream.newLineAtOffset((startX-10)+unit_per_cm, startY-10);
                contentStream.showText(mRecords_list.get(i).getMeasurementTime().substring(11,16));
                contentStream.endText();

                xoffset= xoffset+unit_per_cm;
                startX=xoffset;
            }



            startX=12.5f*unit_per_cm;
            startY=1.2f*unit_per_cm;


            float oldXval=0f;
            float newXval=0f;


            for(int i=0;i<mRecords_list.size();i++){
                float y=  mRecords_list.get(i).getPulsePerMin();



                if(y!=0){


                    float yValue=((0.05f*y)*unit_per_cm)+startY;


                    mDarawList.add(String.valueOf(yValue));

                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(font, 22);
                    contentStream.newLineAtOffset(startX+unit_per_cm-4f, yValue);
                    contentStream.showText(".");
                    contentStream.endText();

                    oldXvalList.add(startX+unit_per_cm-3f);
                    newXvalList.add(startX+unit_per_cm-3f);

                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(fontSeries, 6);
                    contentStream.newLineAtOffset((startX-5f)+unit_per_cm, yValue+8f);
                    contentStream.showText("["+(int)mRecords_list.get(i).getPulsePerMin()+"]");
                    contentStream.endText();
                }

                startX=startX+unit_per_cm;


            }

            startY=1.2f*unit_per_cm;


            float mxy= Float.parseFloat(mDarawList.get(0));
            oldYval= mxy;

            oldXval=  oldXvalList.get(0);
            for(int i=1;i<mDarawList.size();i++) {

                newYval=  Float.parseFloat(mDarawList.get(i));

                contentStream.moveTo(oldXval+2f, oldYval);
                contentStream.lineTo(newXvalList.get(i)+2f, newYval);
                contentStream.stroke();


                oldYval = newYval;
                oldXval=newXvalList.get(i);


            }


            startX=12.5f*unit_per_cm;


            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontSeries, 11);
            contentStream.newLineAtOffset(startX+6 *unit_per_cm, (headerY+3)-unit_per_cm);
            contentStream.showText("Pulse per Min Vs Date");
            contentStream.endText();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    float oldYval=0;
    float newYval=0;

    List<String> mDarawList;
    List<Float> oldXvalList;
    List<Float> newXvalList;

    public  boolean isStoragePermissionGranted() {

        if (Build.VERSION.SDK_INT >= 23) {

            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Logger.log(Level.DEBUG,TAG,"Permission is granted");
                EcgPdf task = new EcgPdf();
                task.execute(new String[0]);
                return true;
            }
            else {

                Logger.log(Level.DEBUG,TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            Logger.log(Level.DEBUG,TAG,"Permission is granted");
            EcgPdf task = new EcgPdf();
            task.execute(new String[0]);
            return true;
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Logger.log(Level.DEBUG,TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            Logger.log(Level.DEBUG,TAG,"##Capturing Screen Processed###");

            EcgPdf task = new EcgPdf();
            task.execute(new String[0]);
        }
    }

}
