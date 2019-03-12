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

import com.tom_roush.harmony.awt.*;
import com.tom_roush.pdfbox.pdmodel.*;
import com.tom_roush.pdfbox.pdmodel.common.*;
import com.tom_roush.pdfbox.pdmodel.font.*;
import com.tom_roush.pdfbox.pdmodel.graphics.image.*;
import com.tom_roush.pdfbox.util.*;

import java.io.*;
import java.text.*;
import java.text.ParseException;
import java.util.*;

import constantsP.*;
import logger.*;
import test.bpl.com.bplscreens.*;
import test.bpl.com.bplscreens.R;

import static android.widget.Toast.LENGTH_SHORT;

public class WeekChartFragment extends Fragment{


    PulseWeekChart pulseWeekChart;
    private String selectedDate;
    private List<String> dates_list;
    private final String TAG = WeekChartFragment.class.getSimpleName();
    private List<BPMeasurementModel> sameMonthOfYearWithDuplicates;
    int noOfDays=7;
    List<String> week1List,plotPoint1List,systolicPoint1List,diabolicPoint1List;
    List<String> week2List,plotPoint2List,systolicPoint2List,diabolicPoint2List;
    List<String> week3List,plotPoint3List,systolicPoint3List,diabolicPoint3List;
    List<String> week4List,plotPoint4List,systolicPoint4List,diabolicPoint4List;
    List<String> week5List,plotPoint5List,systolicPoint5List,diabolicPoint5List;

    CustomBPDayChartYaxis customBPDayChartYaxis;

    CustomBPWeekChart bpChartWeek;
    TextView weekDate;
    private List<BPMeasurementModel> mRecordDetail;
    private String monthOFYear;

    private Map<String,Integer> pulseValue;



    private Map<String, Integer> SystolicValue;
    private Map<String, Integer> DiabolicValue;
    private List<String> systolicList;
    private List<String> diabolicList;

    List<String> pulsePlotPoints;

    ImageView pdf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.week_chart_frag,container,false);
        pulseWeekChart=  view.findViewById(R.id.pulseWeekChart);
        bpChartWeek=  view.findViewById(R.id.bpChartWeek);
        showVal=  view.findViewById(R.id.showVal);
        showVal2= view.findViewById(R.id.showVal2);
        weekDate=  view.findViewById(R.id.weekDate);
        pdf= view.findViewById(R.id.pdf);
        customBPDayChartYaxis=  view.findViewById(R.id.custom_day_chartYaxis);
        return view;
    }



    boolean isshowVal;
    TextView showVal,showVal2;

    String userName,age,gender,comments;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        week1List=new ArrayList<>();
        week2List=new ArrayList<>();
        week3List=new ArrayList<>();
        week4List=new ArrayList<>();
        week5List=new ArrayList<>();

        if(getArguments().getString("user")!=null){
            userName=getArguments().getString("user");
        }


        if(getArguments().getString("age")!=null){
            age=getArguments().getString("age");
        }

        if(getArguments().getString("gender")!=null){
            gender=getArguments().getString("gender");
        }


        if(getArguments().getString("comments")!=null){
            comments=getArguments().getString("comments");
        }


        showVal.setText(R.string.sho);
        showVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isshowVal){
                    bpChartWeek.showValues(true);
                    pulseWeekChart.showValues(true);
                    isshowVal=true;
                    showVal.setText(R.string.hide);
                    showVal.setBackground(getResources().getDrawable(R.drawable.switch_btn_bg));

                    showVal2.setText(R.string.hide);
                    showVal2.setBackground(getResources().getDrawable(R.drawable.switch_btn_bg));

                }else{
                    isshowVal=false;

                    showVal.setBackground(getResources().getDrawable(R.drawable.savebutton));
                    showVal.setText(R.string.sho);

                    bpChartWeek.showValues(false);
                    pulseWeekChart.showValues(false);

                    showVal2.setBackground(getResources().getDrawable(R.drawable.savebutton));
                    showVal2.setText(R.string.sho);

                }


            }
        });


        showVal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isshowVal){
                    bpChartWeek.showValues(true);
                    pulseWeekChart.showValues(true);
                    isshowVal=true;
                    showVal2.setText(R.string.hide);
                    showVal2.setBackground(getResources().getDrawable(R.drawable.switch_btn_bg));

                    showVal.setText(R.string.hide);
                    showVal.setBackground(getResources().getDrawable(R.drawable.switch_btn_bg));

                }else{
                    isshowVal=false;

                    showVal2.setBackground(getResources().getDrawable(R.drawable.savebutton));
                    showVal2.setText(R.string.sho);

                    bpChartWeek.showValues(false);
                    pulseWeekChart.showValues(false);

                    showVal.setBackground(getResources().getDrawable(R.drawable.savebutton));
                    showVal.setText(R.string.sho);

                }
            }
        });



        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isStoragePermissionGranted())
                {
                    Toast.makeText(getActivity(),"Storage Permission is necessary to generate PDF ",Toast.LENGTH_SHORT).show();

                }
            }
        });


        sameMonthOfYearWithDuplicates=new ArrayList<>();
        pulsePlotPoints=new ArrayList<>();

        systolicList=new ArrayList<>();
        diabolicList=new ArrayList<>();


        if (getArguments().getString(Constants.DATE) != null) {
            selectedDate = getArguments().getString(Constants.DATE);
            dates_list = getDaysBetweenDates(get_starting_date_month(selectedDate),
                    get_last_date_month(selectedDate));
            monthOFYear = selectedDate.substring(3, 5);

        }
        if (getArguments().getSerializable(Constants.CHART) != null) {

            if (mRecordDetail == null) {
                mRecordDetail = (List<BPMeasurementModel>) getArguments().getSerializable(Constants.CHART);
                Logger.log(Level.WARNING, TAG, "Get serializable list=" + mRecordDetail.size());
            }


        }


        for (BPMeasurementModel b : mRecordDetail) {
            if (b.getMeasurementTime().substring(3, 5).equals(monthOFYear) &&
                    b.getMeasurementTime().substring(6, 10).equals(selectedDate.substring(6, 10))) {
                sameMonthOfYearWithDuplicates.add(b);
            }

        }


        Logger.log(Level.WARNING, TAG, "Get selected list of days in month with duplicates" +
                "=" + sameMonthOfYearWithDuplicates.size());


        pulseValue = new TreeMap<>();
        SystolicValue=new TreeMap<>();
        DiabolicValue=new TreeMap<>();


        // first pass

        Map<String, List<Integer>> firstPass = new TreeMap<>();
        Map<String, List<Integer>> firstPassSystolic = new TreeMap<>();
        Map<String, List<Integer>> firstPassDiabolic = new TreeMap<>();

        for (BPMeasurementModel bp : sameMonthOfYearWithDuplicates) {
            String name = bp.getMeasurementTime().substring(0,10);
            if (firstPass.containsKey(name)) {
                firstPass.get(name).add((int) bp.getPulsePerMin());
                firstPassSystolic.get(name).add((int) bp.getSysPressure());
                firstPassDiabolic.get(name).add((int) bp.getDiabolicPressure());

            } else {
                List<Integer> pulseVal = new ArrayList<>();
                List<Integer> sysVal = new ArrayList<>();
                List<Integer> diabolicVal = new ArrayList<>();


                pulseVal.add((int) bp.getPulsePerMin());
                firstPass.put(name,pulseVal);


                sysVal.add((int) bp.getSysPressure());
                firstPassSystolic.put(name,sysVal);

                diabolicVal.add((int) bp.getDiabolicPressure());
                firstPassDiabolic.put(name,diabolicVal);

            }
        }


        for (int i = 0; i < dates_list.size(); i++) {

            pulseValue.put(dates_list.get(i),0);
            SystolicValue.put(dates_list.get(i),0);
            DiabolicValue.put(dates_list.get(i),0);
        }


        // Second pass for systolic

        for (Map.Entry<String, List<Integer>> entry : firstPassSystolic.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            SystolicValue.put(entry.getKey(), average);

            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }

        // Second pass for diabolic

        for (Map.Entry<String, List<Integer>> entry : firstPassDiabolic.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            DiabolicValue.put(entry.getKey(), average);

            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }



        // Second pass for pulse

        for (Map.Entry<String, List<Integer>> entry : firstPass.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            pulseValue.put(entry.getKey(), average);

            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }



        // for systolic
        for (String key : SystolicValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + SystolicValue.get(key));
            systolicList.add("" +  SystolicValue.get(key));
        }



        // for diabolic

        for (String key : DiabolicValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + DiabolicValue.get(key));
            diabolicList.add("" +  DiabolicValue.get(key));
        }


            //for pulse

        for (String key : pulseValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + pulseValue.get(key));
            pulsePlotPoints.add("" +  pulseValue.get(key));
        }



        storeWeekList();


        weekDate.setText(firstToLastDayOfWeek(checkSelectedDateWeeks(selectedDate)));
        pulseWeekChart.setHorizontalLabel(checkSelectedDateWeeks(selectedDate));
        pulseWeekChart.setPlotPoints(plotPulseValue(selectedDate));

        bpChartWeek.setHorizontalLabelsBP(checkSelectedDateWeeks(selectedDate));
        bpChartWeek.setSystolicPlotPoints(plotSystolicValue(selectedDate));
        bpChartWeek.setDiabolicPlotPoints(plotDiabolicValue(selectedDate));
        customBPDayChartYaxis.getRecords(mRecordDetail);

    }

    private double calcAverage(List<Integer> values) {
        double result = 0;
        for (Integer value : values) {
            result += value;
        }
        return result / values.size();
    }




    private List<String> checkSelectedDateWeeks(String selectedDate)
    {


        if(week1List.contains(selectedDate)){

           plotPoint1List=pulsePlotPoints.subList(0,7);
            return week1List;
        }


        else if(week2List.contains(selectedDate)){

            plotPoint2List=pulsePlotPoints.subList(7,14);
            return  week2List;
        }



        else if(week3List.contains(selectedDate)){
            plotPoint3List=pulsePlotPoints.subList(14,21);
            return week3List;
        }


        else if(week4List.contains(selectedDate)){
            plotPoint4List=pulsePlotPoints.subList(21,28);
            return week4List;
        }


        else{
            plotPoint4List=pulsePlotPoints.subList(28,dates_list.size());
            return week5List;
        }


    }

    private List<String> plotPulseValue(String selectedDate)
    {

        if(week1List.contains(selectedDate)){

            plotPoint1List=pulsePlotPoints.subList(0,7);
            return plotPoint1List;
        }


        else if(week2List.contains(selectedDate)){

            plotPoint2List=pulsePlotPoints.subList(7,14);
            return  plotPoint2List;
        }



        else if(week3List.contains(selectedDate)){
            plotPoint3List=pulsePlotPoints.subList(14,21);
            return plotPoint3List;
        }


        else if(week4List.contains(selectedDate)){
            plotPoint4List=pulsePlotPoints.subList(21,28);
            return plotPoint4List;
        }


        else{
            plotPoint5List=pulsePlotPoints.subList(28,dates_list.size());
            return plotPoint5List;
        }


    }




    private List<String> plotSystolicValue(String selectedDate)
    {

        if(week1List.contains(selectedDate)){

            systolicPoint1List=systolicList.subList(0,7);
            return systolicPoint1List;
        }


        else if(week2List.contains(selectedDate)){

            systolicPoint2List=systolicList.subList(7,14);
            return  systolicPoint2List;
        }



        else if(week3List.contains(selectedDate)){
            systolicPoint3List=systolicList.subList(14,21);
            return systolicPoint3List;
        }


        else if(week4List.contains(selectedDate)){
            systolicPoint4List=systolicList.subList(21,28);
            return systolicPoint4List;
        }


        else{
            systolicPoint5List=systolicList.subList(28,dates_list.size());
            return systolicPoint5List;
        }


    }

    private List<String> plotDiabolicValue(String selectedDate)
    {

        if(week1List.contains(selectedDate)){

            diabolicPoint1List=diabolicList.subList(0,7);
            return diabolicPoint1List;
        }


        else if(week2List.contains(selectedDate)){

            diabolicPoint2List=diabolicList.subList(7,14);
            return  diabolicPoint2List;
        }



        else if(week3List.contains(selectedDate)){
            diabolicPoint3List=diabolicList.subList(14,21);
            return diabolicPoint3List;
        }


        else if(week4List.contains(selectedDate)){
            diabolicPoint4List=diabolicList.subList(21,28);
            return diabolicPoint4List;
        }


        else{
            diabolicPoint5List=diabolicList.subList(28,dates_list.size());
            return diabolicPoint5List;
        }


    }

    private String firstToLastDayOfWeek(List<String> weekList){
        return  weekList.get(0)+" to "+ weekList.get(weekList.size()-1);
    }



    private void storeWeekList()
    {
        int k=noOfDays;
        int pX=0;
        int weekCounter=0;

        for(int i=pX; i<dates_list.size() ;i++)
        {
            Logger.log(Level.WARNING,TAG,"//Get Week dates//"+dates_list.get(i));
            if(weekCounter==0){
                week1List.add(dates_list.get(i));

            }

            else if(weekCounter==1)
            {
                week2List.add(dates_list.get(i));

            }

            else if(weekCounter==2){
                week3List.add(dates_list.get(i));

            }
            else if(weekCounter==3){
                week4List.add(dates_list.get(i));

            }

            else if(weekCounter==4){
                week5List.add(dates_list.get(i));

            }


            if(i==k-1){
                Logger.log(Level.WARNING,TAG,"// Week interval //");
                pX=pX+7;
                k=k+7;
                weekCounter++;
            }

        }
    }






    @SuppressLint("SimpleDateFormat")
    private String get_starting_date_month(String date)
    {
        String start_date="";
        Date d=null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;
        Calendar c = Calendar.getInstance();
        DateFormat df=new SimpleDateFormat("dd-MM-yyyy");

        try {
            convertedDate = dateFormat.parse(date);
            c.setTime(convertedDate);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            d=c.getTime();
            start_date=df.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Logger.log(Level.WARNING,TAG,"STarting date="+start_date);

        return start_date;
    }

    @SuppressLint("SimpleDateFormat")
    private String get_last_date_month(String date)
    {
        String end_date="";
        Date d=null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = null;
        Calendar c = Calendar.getInstance();
        DateFormat df=new SimpleDateFormat("dd-MM-yyyy");

        try {
            convertedDate = dateFormat.parse(date);
            c.setTime(convertedDate);
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
            d=c.getTime();
            end_date=df.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Logger.log(Level.WARNING,TAG,"Ending  date="+end_date);
        return end_date;
    }


    // get no.of list of dates
    @SuppressLint("SimpleDateFormat")
    public List<String> getDaysBetweenDates(String  start_date, String end_date)
    {

        List<String> dates_list_=new ArrayList<String>();
        Date startdate=null;
        Date enddate=null;

        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");

        try {
            startdate=sdf.parse(start_date);
            enddate=sdf.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar start = Calendar.getInstance();
        start.setTime(startdate);
        Calendar end = Calendar.getInstance();
        end.setTime(enddate);
        end.add(Calendar.DAY_OF_YEAR, 1); //Add 1 day to endDate to make sure endDate is included into the final list
        DateFormat df=new SimpleDateFormat("dd-MM-yyyy");
     SimpleDateFormat dfx=new SimpleDateFormat("dd-MM-yyyy");
        while (start.before(end)) {
            String str=df.format(start.getTime());
            dates_list_.add(str);
            Date d=null;
            Logger.log(Level.DEBUG,TAG,"str="+str);
            try {
                d=dfx.parse(str);
                start.add(Calendar.DAY_OF_YEAR, 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        Logger.log(Level.WARNING,TAG,"List of dates="+dates_list_);
        return dates_list_;
    }


    @Override
    public void onResume() {
        super.onResume();

      // EcgPdf task=new EcgPdf();
      //  task.execute(new String[0]);
    }












    private void drawTables2( PDPageContentStream contentStream )
    {
        float unit_per_cm = 28.34f;
        float graph_width = unit_per_cm * 5.5f;
        float graph_height = unit_per_cm * 8f;

        float startX=2.5f*unit_per_cm;
        float startY=10*unit_per_cm;

        try {
            contentStream.addRect(startX, startY, graph_width, graph_height);
            contentStream.setStrokingColor(AWTColor.BLACK);
            contentStream.setLineWidth(1.4f);
            contentStream.stroke();


        } catch (IOException e) {
            e.printStackTrace();
        }




    }




    @SuppressLint("StaticFieldLeak")
    class EcgPdf extends AsyncTask<String,String,String> {




        ProgressDialog progressDialog;

        String isloaded = "false";


        @Override
        protected String doInBackground(String... strings) {

            try {
                createPdf_();
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
                            "PDF  Successfully created", LENGTH_SHORT).show();
                    onCreateDialog();


                }

            }
        }
    }


    private void createPdf_(){


        PDDocument document = new PDDocument();
        PDPage page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));



        float page_height = page.getMediaBox().getHeight();
        float page_width = page.getMediaBox().getWidth();


        PDFBoxResourceLoader.init(getActivity());

        String fileNameDirectory = "BPL_BE_WELL_REPORT";

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileNameDirectory);

        if (!file.exists()) {
            file.mkdir();

        }


        // String path = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
        //        + "/Documents/"+userName+"_"+System.currentTimeMillis()+"_LPP_ACT.pdf";

        String path = "";
        try {
            path = file + "/" + "BPL" + "_" + "_USER_BP_WEEK_.pdf";
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

            if(age==null)
            {
                age="-NA-";
            }


            contentStream.showText("User Name : " +userName + "   "
                    + "Age : " + age + "   " + "Gender : " +
                    gender + "  " + "Clinic Name : " + "Bpl Med Tech");

            contentStream.endText();

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 11);
            contentStream.newLineAtOffset(120f, marginUpperLine - 12);
            contentStream.showText("Comments :" + "--");

            contentStream.endText();

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 10);
            contentStream.newLineAtOffset(1.9f * unit_per_cm, page_height-2.8f*unit_per_cm);
            contentStream.showText("(BPL BE WELL I-PRESSURE WEEKLY REPORT) " );
            contentStream.endText();


            // Add app version
            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(font, 9);
            contentStream.newLineAtOffset(page_width - 120, marginUpperLine + 2f);
            contentStream.showText("Date : "+getDateTime());
            contentStream.endText();

            // Add text



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
            drawPulseChartWeekly(contentStream, plotPulseValue(selectedDate),checkSelectedDateWeeks(selectedDate));
            drawTables2(contentStream,    checkSelectedDateWeeks(selectedDate), plotSystolicValue(selectedDate),plotDiabolicValue(selectedDate));
            drawBPChartWeekly(contentStream, plotSystolicValue(selectedDate),plotDiabolicValue(selectedDate),
                    checkSelectedDateWeeks(selectedDate));

            drawDottedLines(contentStream);



            contentStream.close();
            document.save(path);
            document.close();


        } catch (IOException e) {
            e.printStackTrace();

        }


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



    private void drawBPChartWeekly(  PDPageContentStream contentStream ,List<String>systolicList,List<String> diabolicList
                                     , List<String> dates_list){
        float unit_per_cm = 28.34f;
        float graph_width = unit_per_cm * 16f;
        float graph_height = unit_per_cm * 8f;

        float startX=12.5f*unit_per_cm;
        float startY=10*unit_per_cm;

        List<String> coorlist=  bpChartWeek.getColor();
        Logger.log(Level.DEBUG,"--colors on pdf --",""+ coorlist);
        String []strArrVertical= new String[]{"40", "60", "80", "100", "120", "140", "160", "180","200"};


        float x1 = 0,x2=0,y1=0,y2=0;

        float headerY=0;

        PDFont font = PDType1Font.HELVETICA;
        PDFont fontSeries = PDType1Font.HELVETICA_BOLD;

        try {
           /* contentStream.addRect(startX, startY, graph_width, graph_height);
            contentStream.setStrokingColor(AWTColor.BLACK);
            contentStream.setLineWidth(1.4f);
            contentStream.stroke();*/



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
            for(int i = 0; i<dates_list.size();i++){

//                contentStream.moveTo(startX, startY);
//                contentStream.lineTo(xoffset, startY+graph_height);
//                contentStream.stroke();



                contentStream.setNonStrokingColor(0, 0, 0); //black text
                contentStream.beginText();
                contentStream.setFont(font, 8);
                contentStream.newLineAtOffset((startX-10)+unit_per_cm, startY-10);
                contentStream.showText(checkSelectedDateWeeks(selectedDate).get(i).substring(0,5));
                contentStream.endText();

                xoffset=  xoffset+2*unit_per_cm;
                startX=xoffset;
            }





            // draw thick line

            // ...............[[[[[[]]]]]]]]]]
            startY=10*unit_per_cm;
            startX=12.5f*unit_per_cm;

            Logger.log(Level.DEBUG,"--dia list on pdf --",""+ diabolicList);

            for(int i=0;i<diabolicList.size();i++){
                y1=  Integer.parseInt(systolicList.get(i));
                y2=  Integer.parseInt(diabolicList.get(i));

                if(y1!=0 && y2!=0){
                    y1=(((0.05f*y1)*unit_per_cm)+startY) -(2*unit_per_cm);
                    y2=(((0.05f*y2)*unit_per_cm)+startY) -(2*unit_per_cm);





                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(fontSeries, 6);
                    contentStream.newLineAtOffset((startX-8f)+unit_per_cm, y1+2);
                    contentStream.showText(systolicList.get(i));
                    contentStream.endText();



                    contentStream.setNonStrokingColor(0, 0, 0); //black text
                    contentStream.beginText();
                    contentStream.setFont(fontSeries, 6);
                    contentStream.newLineAtOffset((startX-5f)+unit_per_cm, y2-8f);
                    contentStream.showText(diabolicList.get(i));
                    contentStream.endText();



                    if(bpChartWeek.getColor().get(i).equalsIgnoreCase("red")){
                        contentStream.setStrokingColor(AWTColor.RED);
                    }else if(bpChartWeek.getColor().get(i).equalsIgnoreCase("green")){
                        contentStream.setStrokingColor(AWTColor.GREEN);
                    }else if(bpChartWeek.getColor().get(i).equalsIgnoreCase("blue")) {
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

                startX=startX+2*unit_per_cm;
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


    private void drawTables2( PDPageContentStream contentStream ,List<String> dates_list,
                              List<String> systolicList,List<String> diabolicList) {
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


            for(int i=30;i>=0 ;i--){

                        if(i<7){
                            contentStream.setNonStrokingColor(0, 0, 0); //black text
                            contentStream.beginText();
                            contentStream.setFont(font, 8);
                            contentStream.newLineAtOffset(xoffset, yoffset);
                            contentStream.showText(dates_list.get(i));
                            contentStream.endText();

                            contentStream.setNonStrokingColor(0, 0, 0); //black text
                            contentStream.beginText();
                            contentStream.setFont(font, 8);
                            contentStream.newLineAtOffset(xoffset+2*unit_per_cm, yoffset);

                            if(systolicList.get(i).equals("0"))
                                contentStream.showText("-");
                            else
                                contentStream.showText(systolicList.get(i));

                            contentStream.endText();



                            contentStream.setNonStrokingColor(0, 0, 0); //black text
                            contentStream.beginText();
                            contentStream.setFont(font, 8);
                            contentStream.newLineAtOffset(xoffset+3*unit_per_cm, yoffset);

                            if(diabolicList.get(i).equals("0"))
                                contentStream.showText("-");
                            else
                                contentStream.showText(diabolicList.get(i));


                            contentStream.endText();

                            contentStream.setNonStrokingColor(0, 0, 0); //black text
                            contentStream.beginText();
                            contentStream.setFont(font, 7);
                            contentStream.newLineAtOffset(xoffset+4*unit_per_cm, yoffset);

                            if(systolicList.get(i).equals("0") || diabolicList.get(i).equals("0"))
                                contentStream.showText("-");
                            else{
                                contentStream.showText( Utility.validateTypeBP((systolicList.get(i)),
                                        (diabolicList.get(i))));

                            }

                            contentStream.endText();


                            // comments section
                            contentStream.setNonStrokingColor(0, 0, 0); //black text
                            contentStream.beginText();
                            contentStream.setFont(font, 7);
                            contentStream.newLineAtOffset(xoffset+7*unit_per_cm, yoffset);
                            contentStream.showText("-NA-");
                            contentStream.endText();

                        }





                yoffset=yoffset+unit_per_cm/2;

                        }


            // Add rect bar


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
            contentStream.setLineWidth(1.2f);
            contentStream.setStrokingColor(AWTColor.BLACK);

            contentStream.setNonStrokingColor(0, 0, 0); //black text
            contentStream.beginText();
            contentStream.setFont(fontH, 9);
            contentStream.newLineAtOffset(xoffset, yoffset+4);
            contentStream.showText("Date");
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
            contentStream.setFont(fontH, 8);
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
            contentStream.showText(firstToLastDayOfWeek(checkSelectedDateWeeks(selectedDate))+"  (SYS/DIA) mmHg");
            contentStream.endText();

            // border in tables2 upper region
            contentStream.setLineWidth(0.8f);
            contentStream.setStrokingColor(AWTColor.GRAY);
            contentStream.moveTo(startX, yoffset+14f);
            contentStream.lineTo(graph_width+startX, yoffset+14);
            contentStream.stroke();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void drawPulseChartWeekly(  PDPageContentStream contentStream,
                                        List<String> pulsePlotPoints,
                                        List<String> dates_ ){


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

            // horizontal grids
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


           for(int i = 0; i<dates_.size();i++){

               // vertical grids
               /* contentStream.moveTo(startX, startY);
                contentStream.lineTo(xoffset, startY+graph_height);
                contentStream.stroke();
*/


                //Add dates
                contentStream.setNonStrokingColor(0, 0, 0); //black text
                contentStream.beginText();
                contentStream.setFont(font, 8);
                contentStream.newLineAtOffset((startX-10)+unit_per_cm, startY-10);
                contentStream.showText(dates_.get(i).substring(0,5));
                contentStream.endText();

                xoffset= xoffset+2*unit_per_cm;
                startX=xoffset;
            }



            startX=12.5f*unit_per_cm;
            startY=1.2f*unit_per_cm;


            float oldXval=0f;
            float newXval=0f;


            for(int i=0;i<pulsePlotPoints.size();i++){
                float y=  Integer.parseInt(pulsePlotPoints.get(i));



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
                    contentStream.showText("["+pulsePlotPoints.get(i)+"]");
                    contentStream.endText();
                }



                startX=startX+2*unit_per_cm;


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


    @SuppressLint("SimpleDateFormat")
    public String getDateTime()
    {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return  df.format(date);
    }


    float oldYval=0;
    float newYval=0;

    List<String> mDarawList;
    List<Float> oldXvalList;
    List<Float> newXvalList;


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
                if (which != -1) {
                    // Write code for share
                    if (which == 0) {
                        // PDF Viewer
                        File mFile;
                        mFile = new File(Environment.getExternalStorageDirectory() +
                                "/BPL_BE_WELL_REPORT" + "/BPL__USER_BP_WEEK_.pdf");


                        Intent target = new Intent(Intent.ACTION_VIEW);
                        target.setDataAndType(Uri.fromFile(mFile), "application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                        Intent intent = Intent.createChooser(target, "Open File");
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // Instruct the user to install a PDF reader here, or something
                            e.printStackTrace();
                            Logger.log(Level.DEBUG, "---", "Install any PDF viewer");
                        }
                    } else if (which == 1) {
                        // Share
                        File mFile = new File(Environment.getExternalStorageDirectory() + "/BPL_BE_WELL_REPORT" + "/BPL__USER_BP_WEEK_.pdf");


                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        Uri contentUri = FileProvider.getUriForFile(getActivity(),
                                getActivity().getPackageName() + ".provider", mFile);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        shareIntent.setType("application/pdf");
                        //shareIntent.setType("image/pdf");
                        startActivity(Intent.createChooser(shareIntent, "Share Via"));
                    } else {

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