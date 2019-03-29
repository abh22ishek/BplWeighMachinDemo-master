package canny;

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


public class IweighMonthsFragment extends Fragment {

    public List<String> dates;
    public  List<String> weight;
    public  List<String> bmi;

    private Button btn_weight,btn_bmi;

    IweighChartViewMonth wt_chart;
    IweighBmiViewMonth bmi_chart;

    private TextView  txt_bmi_chart,txt_wt_chart;
    GlobalClass globalVariable;

    public  List<RecordDetailWeighMachine> UserMeasuredWeightList;

    LinearLayout layoutWeight,layoutBmi;



    // ------------------------------------------

    private List<RecordDetailWeighMachine> mRecordDetail;
    private final String TAG = IweighMonthsFragment.class.getSimpleName();
    private String selectedDate;
    private String monthOFYear;
    private List<RecordDetailWeighMachine> sameMonthOfYearWithDuplicates;
    private List<String> dates_list;





    private Map<String, Integer> weightValue;
    private Map<String, Integer> bmiValue;


    private List<String> weightList;
    private List<String> bmiList;


    TextView monthDate,showVal2;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.iweigh_month_chart,container,false);
        btn_weight= view.findViewById(R.id.btn_weight_chart);
        btn_bmi=  view.findViewById(R.id.btn_bmi_chart);


        wt_chart= view.findViewById(R.id.weight_chart_);
        bmi_chart= view. findViewById(R.id.bmi_chart_);

        txt_bmi_chart=  view.findViewById(R.id.txt_bmi_chart);
        txt_wt_chart= view. findViewById(R.id.txt_wt_chart);
        layoutWeight=view.findViewById(R.id.layoutWeight);
        layoutBmi=view.findViewById(R.id.layoutBmi);
        monthDate=view.findViewById(R.id.DateTime);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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


        dates = new ArrayList<>();
        weight = new ArrayList<>();
        bmi = new ArrayList<>();


        final String mUserName = getActivity().getIntent().getExtras().getString(Constants.USER_NAME);

        int density = this.getResources().getDisplayMetrics().densityDpi;

        if (getActivity().getIntent().getExtras().getSerializable(Constants.CHART) != null) {
            UserMeasuredWeightList = (List<RecordDetailWeighMachine>) getActivity().
                    getIntent().getExtras().getSerializable(Constants.CHART);
            mRecordDetail = (List<RecordDetailWeighMachine>) getActivity().getIntent().getExtras().
                    getSerializable(Constants.CHART);
            Logger.log(Level.WARNING, TAG, "Get serializable list=" + mRecordDetail.size());
            Logger.log(Level.WARNING, TAG, "Get serializable list=" + UserMeasuredWeightList.size());
        }


        // this list will retain the no. of months of specified year which was clicked
        sameMonthOfYearWithDuplicates = new ArrayList<>();


        weightList = new ArrayList<>();
        bmiList = new ArrayList<>();


        wt_chart.set_XY_points(UserMeasuredWeightList);
        bmi_chart.set_XY_points(UserMeasuredWeightList);


        if (UserMeasuredWeightList != null && UserMeasuredWeightList.size() >= 1) {
            for (RecordDetailWeighMachine r : UserMeasuredWeightList) {
                dates.add(r.getDate());
                weight.add(String.valueOf(r.getWeight()));
                bmi.add(String.valueOf(r.getBmi()));
            }


            btn_bmi.setAlpha(0.8f);
            layoutBmi.setVisibility(View.GONE);
            layoutWeight.setVisibility(View.VISIBLE);
            txt_bmi_chart.setVisibility(View.GONE);
            btn_weight.setAlpha(1f);

        } else {

            layoutBmi.setVisibility(View.GONE);
            layoutWeight.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Sorry No records to display", Toast.LENGTH_SHORT).show();
        }





        if (getArguments().getString(Constants.DATE) != null) {
            selectedDate = getArguments().getString(Constants.DATE);
            monthDate.setText(new StringBuilder().append("SELECTED MONTH : ").
                    append(dates.get(0).substring(0,10)).toString());
            monthOFYear = selectedDate.substring(3,5);

            dates_list = getDaysBetweenDates(get_starting_date_month(selectedDate),get_last_date_month(selectedDate));
            wt_chart.setHorizontalLabel(dates_list);
            bmi_chart.setHorizontalLabels(dates_list);

        }


        for (RecordDetailWeighMachine b : mRecordDetail) {
            if (b.getDate().substring(3,5).equals(monthOFYear) &&
                    b.getDate().substring(6,10).equals(selectedDate.substring(6, 10))) {
                sameMonthOfYearWithDuplicates.add(b);
            }

        }


        Logger.log(Level.WARNING, TAG, "Get selected list of days in month with duplicates" +
                "=" + sameMonthOfYearWithDuplicates.size());


        weightValue = new TreeMap<>();
        bmiValue = new TreeMap<>();


        // first pass

        Map<String, List<Integer>> firstPassWeight = new TreeMap<>();
        Map<String, List<Integer>> firstPassBmi = new TreeMap<>();

        for (RecordDetailWeighMachine bp : sameMonthOfYearWithDuplicates) {
            String name = bp.getDate().substring(0,10);

            if (firstPassWeight.containsKey(name)) {
                firstPassWeight.get(name).add((int) bp.getWeight());
                firstPassBmi.get(name).add((int) bp.getBmi());
            } else {
                List<Integer> weightVal = new ArrayList<>();
                List<Integer> bmiVal = new ArrayList<>();

                weightVal.add((int) bp.getWeight());
                firstPassWeight.put(name, weightVal);

                bmiVal.add((int) bp.getBmi());
                firstPassBmi.put(name, bmiVal);
            }
        }


        // Second pass
        for (int i = 0; i < dates_list.size(); i++) {

            weightValue.put(dates_list.get(i), 0);
            bmiValue.put(dates_list.get(i), 0);
        }




        // for Weight

        for (Map.Entry<String, List<Integer>> entry : firstPassWeight.entrySet()) {
            int average = (int) calcAverage(entry.getValue());
            weightValue.put(entry.getKey(), average);
            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey() + "  " + average);
        }


        // for bmi

        for (Map.Entry<String, List<Integer>> entry : firstPassBmi.entrySet()) {

            int average = (int) calcAverage(entry.getValue());
            bmiValue.put(entry.getKey(), average);
            Logger.log(Level.WARNING, TAG, "Key and Average=" + entry.getKey() + "  " + average);
        }





        // weight
        for (String key : weightValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map Weight Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map Wt. Value value=" + weightValue.get(key));
            weightList.add("" + weightValue.get(key));
        }


        // diabolic

        for (String key : bmiValue.keySet()) {

            Logger.log(Level.WARNING, TAG, "Final Map BMI Value Key=" + key);
            Logger.log(Level.WARNING, TAG, "Final Map BMI Value value=" + bmiValue.get(key));
            bmiList.add("" + bmiValue.get(key));
        }


        wt_chart.setPlotPoints(weightList);
        bmi_chart.setPlotPoints(bmiList);
    }



    private double calcAverage(List<Integer> values) {
        double result = 0;
        for (Integer value : values) {
            result += value;
        }
        return result / values.size();
    }

    // Get the begining date of month

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
    public  List<String> getDaysBetweenDates(String  start_date, String end_date)
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








