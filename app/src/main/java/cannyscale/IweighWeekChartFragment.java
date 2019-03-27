package cannyscale;

import android.annotation.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import java.text.*;
import java.util.*;

import constantsP.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.*;

import static com.tom_roush.fontbox.ttf.OS2WindowsMetricsTable.TAG;

public class IweighWeekChartFragment extends Fragment {


    private String selectedDate;
    private List<String> dates_list;
    private final String TAG = IweighWeekChartFragment.class.getSimpleName();

    private List<RecordDetailWeighMachine> sameMonthOfYearWithDuplicates;
    int noOfDays=7;

    List<String> week1List,weight11List,bmi11List,plotPoint1List;
    List<String> week2List,weight2List,bmi2List,plotPoint2List;
    List<String> week3List,weight3List,bmi33List,plotPoint3List;
    List<String> week4List,weight4List,bmi44List,plotPoint4List;
    List<String> week5List,weight5List,bmi5List,plotPoint5List;


    TextView weekDate;


    private List<RecordDetailWeighMachine> mRecordDetail;
    private String monthOFYear;





    private Map<String, Integer> WeightValue;
    private Map<String, Integer> BmiValue;
    private List<String> weightList;
    private List<String> bmiList;



    //-----------

    public List<String> dates;
    public  List<String> weight;
    public  List<String> bmi;

    private Button btn_weight,btn_bmi;

    IweighChartViewWeek wt_chart;
    IweighBmiViewWeek bmi_chart;

    private TextView  txt_bmi_chart,txt_wt_chart;
    GlobalClass globalVariable;

    public  List<RecordDetailWeighMachine> UserMeasuredWeightList;

    LinearLayout layoutWeight,layoutBmi;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.iweigh_week_chart,container,false);
        btn_weight= view.findViewById(R.id.btn_weight_chart);
        btn_bmi=  view.findViewById(R.id.btn_bmi_chart);


        wt_chart= view.findViewById(R.id.weight_chart_);
        bmi_chart= view. findViewById(R.id.bmi_chart_);

        txt_bmi_chart=  view.findViewById(R.id.txt_bmi_chart);
        txt_wt_chart= view. findViewById(R.id.txt_wt_chart);
        layoutWeight=view.findViewById(R.id.layoutWeight);
        layoutBmi=view.findViewById(R.id.layoutBmi);
        weekDate=view.findViewById(R.id.DateTime);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        week1List=new ArrayList<>();
        week2List=new ArrayList<>();
        week3List=new ArrayList<>();
        week4List=new ArrayList<>();
        week5List=new ArrayList<>();


        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_bmi.setAlpha(0.8f);
                btn_weight.setAlpha(1f);

                layoutBmi.setVisibility(View.GONE);
                layoutWeight.setVisibility(View.VISIBLE);

                txt_wt_chart.setVisibility(View.VISIBLE);
                txt_bmi_chart.setVisibility(View.GONE);
            }
        });


        btn_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_weight.setAlpha(0.8f);
                btn_bmi.setAlpha(1f);


                layoutBmi.setVisibility(View.VISIBLE);
                layoutWeight.setVisibility(View.GONE);

                txt_bmi_chart.setVisibility(View.VISIBLE);
                txt_wt_chart.setVisibility(View.GONE);
            }
        });

        dates=new ArrayList<>();
        weight=new ArrayList<>();
        bmi=new ArrayList<>();


        final String mUserName=getActivity().getIntent().getExtras().getString(Constants.USER_NAME);

        int density =this.getResources().getDisplayMetrics().densityDpi;

        if(getActivity().getIntent().getExtras().getSerializable(Constants.CHART)!=null){
            UserMeasuredWeightList= (List<RecordDetailWeighMachine>) getActivity().
                    getIntent().getExtras().getSerializable(Constants.CHART);

            mRecordDetail = (List<RecordDetailWeighMachine>) getActivity().getIntent().getExtras()
                    .getSerializable(Constants.CHART);
            Logger.log(Level.WARNING, TAG, "Get serializable list=" + mRecordDetail.size());
            Logger.log(Level.WARNING,TAG,"Get serializable list="+UserMeasuredWeightList.size());


            btn_bmi.setAlpha(0.8f);
            layoutBmi.setVisibility(View.GONE);
            layoutWeight.setVisibility(View.VISIBLE);
            txt_bmi_chart.setVisibility(View.GONE);
            btn_weight.setAlpha(1f);


        }

        wt_chart.set_XY_points(mRecordDetail);
        bmi_chart.set_XY_points(mRecordDetail);


        sameMonthOfYearWithDuplicates=new ArrayList<>();


        weightList=new ArrayList<>();
        bmiList=new ArrayList<>();


        if (getArguments().getString(Constants.DATE) != null) {
            selectedDate = getArguments().getString(Constants.DATE);
            dates_list = getDaysBetweenDates(get_starting_date_month(selectedDate),
                    get_last_date_month(selectedDate));
            monthOFYear = selectedDate.substring(3, 5);

        }



        for (RecordDetailWeighMachine b : mRecordDetail) {
            if (b.getDate().substring(3, 5).equals(monthOFYear) &&
                    b.getDate().substring(6, 10).equals(selectedDate.substring(6, 10))) {
                sameMonthOfYearWithDuplicates.add(b);
            }

        }


        Logger.log(Level.WARNING, TAG, "Get selected list of days in month with duplicates" +
                "=" + sameMonthOfYearWithDuplicates.size());



        WeightValue=new TreeMap<>();
        BmiValue=new TreeMap<>();


        // first pass

        Map<String, List<Integer>> firstPassWeight = new TreeMap<>();
        Map<String, List<Integer>> firstPassBmi = new TreeMap<>();

        for (RecordDetailWeighMachine bp : sameMonthOfYearWithDuplicates) {
            String name = bp.getDate().substring(0,10);
            if (firstPassWeight.containsKey(name)) {

                firstPassWeight.get(name).add((int) bp.getWeight());
                firstPassBmi.get(name).add((int) bp.getBmi());

            } else {
                List<Integer> wtVal = new ArrayList<>();
                List<Integer> bmiVal = new ArrayList<>();





                wtVal.add((int) bp.getWeight());
                firstPassWeight.put(name,wtVal);

                bmiVal.add((int) bp.getBmi());
                firstPassBmi.put(name,bmiVal);

            }
        }


        for (int i = 0; i < dates_list.size(); i++) {

            WeightValue.put(dates_list.get(i),0);
            BmiValue.put(dates_list.get(i),0);
        }


        // Second pass for systolic

        for (Map.Entry<String, List<Integer>> entry : firstPassWeight.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            WeightValue.put(entry.getKey(), average);

            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }

        // Second pass for diabolic

        for (Map.Entry<String, List<Integer>> entry : firstPassBmi.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            BmiValue.put(entry.getKey(), average);

            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey()+"  "+average);
        }






        // for systolic
        for (String key : WeightValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + WeightValue.get(key));
            weightList.add("" +  WeightValue.get(key));
        }



        // for diabolic

        for (String key : BmiValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Pulse Value value=" + BmiValue.get(key));
            bmiList.add("" +  BmiValue.get(key));
        }




        storeWeekList();



        weekDate.setText(new StringBuilder().append("Selected Week :").append(firstToLastDayOfWeek(checkSelectedDateWeeks(selectedDate))).toString());


        wt_chart.setHorizontalLabel(checkSelectedDateWeeks(selectedDate));
        wt_chart.setPlotPoints(plotWeightValue(selectedDate));

        bmi_chart.setHorizontalLabels(checkSelectedDateWeeks(selectedDate));
        bmi_chart.setPlotPoints(plotBmiValue(selectedDate));



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

            plotPoint1List=weightList.subList(0,7);
            return week1List;
        }


        else if(week2List.contains(selectedDate)){

            plotPoint2List=weightList.subList(7,14);
            return  week2List;
        }



        else if(week3List.contains(selectedDate)){
            plotPoint3List=weightList.subList(14,21);
            return week3List;
        }


        else if(week4List.contains(selectedDate)){
            plotPoint4List=weightList.subList(21,28);
            return week4List;
        }


        else{
            plotPoint4List=weightList.subList(28,dates_list.size());
            return week5List;
        }


    }





    private List<String> plotWeightValue(String selectedDate)
    {

        if(week1List.contains(selectedDate)){

            weight11List=weightList.subList(0,7);
            return weight11List;
        }


        else if(week2List.contains(selectedDate)){

            weight2List=weightList.subList(7,14);
            return  weight2List;
        }



        else if(week3List.contains(selectedDate)){
            weight3List=weightList.subList(14,21);
            return weight3List;
        }


        else if(week4List.contains(selectedDate)){
            weight4List=weightList.subList(21,28);
            return weight4List;
        }


        else{
            weight5List=weightList.subList(28,dates_list.size());
            return weight5List;
        }


    }

    private List<String> plotBmiValue(String selectedDate)
    {

        if(week1List.contains(selectedDate)){

            bmi11List=bmiList.subList(0,7);
            return bmi11List;
        }


        else if(week2List.contains(selectedDate)){

            bmi2List=bmiList.subList(7,14);
            return  bmi2List;
        }



        else if(week3List.contains(selectedDate)){
            bmi33List=bmiList.subList(14,21);
            return bmi33List;
        }


        else if(week4List.contains(selectedDate)){
            bmi44List=bmiList.subList(21,28);
            return bmi44List;
        }


        else{
            bmi5List=bmiList.subList(28,dates_list.size());
            return bmi5List;
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

}
