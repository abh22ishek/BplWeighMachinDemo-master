package testreport;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import constantsP.Constants;
import constantsP.DateTime;
import constantsP.Utility;
import jjoe64.graphview.GraphView;
import jjoe64.graphview.GridLabelRenderer;
import jjoe64.graphview.Viewport;
import jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import jjoe64.graphview.series.DataPoint;
import jjoe64.graphview.series.LineGraphSeries;
import logger.Level;
import logger.Logger;
import model.GrowthMonthModel;
import model.RecordsDetail;
import test.bpl.com.bplscreens.R;


public class TestReportMonthFragment extends Fragment {

    GraphView hr_graph;
    GraphView spo2_graph;

    TextView time_hr;
    TextView time_spo2;

    String day_time="";
    String spo2_str="";
    String hr_str="";
    String month="";
    List<String> dates_list ;
    List<RecordsDetail> same_month_date_list;
    List<RecordsDetail> uniqe_dates_list;
    List<RecordsDetail> final_input_month_lists;
    List<GrowthMonthModel> final_output_month_lists;

    final String TAG=TestReportMonthFragment.class.getSimpleName();

     int month_spo2=0;
    public static int x_month_spo2=0;
    int month_pr=0;


    public TestReportMonthFragment()  {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.log(Level.DEBUG, TestReportMonthFragment.class.getSimpleName(), "On onActivityCreated() method gets called ++");
        init();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chart_month, container, false);
        Logger.log(Level.DEBUG, TestReportMonthFragment.class.getSimpleName(), " Utility.mRecord_detail_list.size(); ++"
                + Utility.mRecord_detail_list.size());


        same_month_date_list=new ArrayList<RecordsDetail>();


        hr_graph=  view.findViewById(R.id.graph_heart_rate);
        spo2_graph=  view.findViewById(R.id.graph_spo2);
        time_hr=  view.findViewById(R.id.time_hr);
        time_spo2=  view.findViewById(R.id.time_spo2);

        dates_list=new ArrayList<>();
        if(null!=getArguments().getString(Constants.TESTING_TIME_CONSTANTS))
        {
            day_time=getArguments().getString(Constants.TESTING_TIME_CONSTANTS,"no time");
            spo2_str= getArguments().getString(Constants.SPO2_CONSTANTS,"");
            hr_str=getArguments().getString(Constants.PR_CONSTANTS,"");
            month=getArguments().getString(Constants.MONTH,"00");

        }


        // To check if record is of particular month
        for(int i=0;i<Utility.mRecord_detail_list.size();i++)
        {
            if(Utility.mRecord_detail_list.get(i).getTesting_time().substring(3,5).equals(month))
            {
                same_month_date_list.add(Utility.mRecord_detail_list.get(i));
            }
        }

        Logger.log(Level.DEBUG,TestReportMonthFragment.class.getSimpleName(),"---****---"+same_month_date_list.size()+""+same_month_date_list);





        return view;
    }



    private void init()
    {
        String time[]=day_time.split(" ");
        Logger.log(Level.DEBUG, TestReportMonthFragment.class.getSimpleName(), "**Date+++="+time[0]);


        time_hr.setText("Date :" +time[0]);
        time_hr.setTextColor(Utility.getColorWrapper(getActivity(), R.color.header_itltle_color));
        time_spo2.setText("Date :" +(time[0]));
        time_spo2.setTextColor(Utility.getColorWrapper(getActivity(), R.color.colorAccent));

       // List<String> spo2_list=new ArrayList<String>(Arrays.asList(spo2_str.split(",")));

     //   List<String> hr_list=new ArrayList<String>(Arrays.asList(hr_str.split(",")));
        Logger.log(Level.DEBUG, TestReportMonthFragment.class.getSimpleName(), "Get SPO2list=");

       // String month=com.bploximeter.constants.DateTime.get_month("2016-05-11");
        //com.bploximeter.constants.DateTime.convert_string_to_date("11-05-2016");
       // get_last_date_month("11-05-2016");





        dates_list=getDaysBetweenDates(get_starting_date_month(time[0]), get_last_date_month(time[0]));

        Logger.log(Level.DEBUG, TestReportMonthFragment.class.getSimpleName(), "Dates_list++" + dates_list);


        hasDuplicates(same_month_date_list);

       /* for(int i=0;i<uniqe_dates_list.size();i++)
        {
           month_spo2=month_spo2+Integer.parseInt(uniqe_dates_list.get(i).getSpo2());
        }

        x_month_spo2= month_spo2/uniqe_dates_list.size();// get average of current  month*/


        get_avg_spo2_hr(uniqe_dates_list);

        for(int i=0;i<dates_list.size();i++)
        {
            GrowthMonthModel m=new GrowthMonthModel();
            m.setDate(dates_list.get(i));
            m.setDuration("0");
            m.setSpo2("0");
            m.setPr("0");

            final_output_month_lists.add(m);

        }
        Logger.log(Level.DEBUG, TestReportMonthFragment.class.getSimpleName(), "After inserting fron dateslist++=" + final_output_month_lists.size());
        compare_lists(final_output_month_lists,final_input_month_lists);

        plot_hr_month_graph(final_output_month_lists,getActivity());
        plot_spo2_month_graph(final_output_month_lists,getActivity());
    }






    /*public static int daysOfMonth(int year, int month) {

        DateTime dateTime = new DateTime(year, month, 14, 12, 0, 0, 000);

        Logger.log(Level.DEBUG,TestReportMonthFragment.class.getSimpleName(),"++No. of days in month="+dateTime.dayOfMonth().getMaximumValue());
        dateTime.dayOfMonth();

        return dateTime.dayOfMonth().getMaximumValue();
    }*/

    public  String standard_date_format(String date)
    {
        String output="";
        Date d=null;
        DateFormat sdf_input=new SimpleDateFormat("dd-MM-yyyy");
        try {
            d=sdf_input.parse(date);
            DateFormat sdf_output=new SimpleDateFormat("yyyy-MM-dd");
            output=sdf_output.format(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Logger.log(Level.DEBUG,TestReportMonthFragment.class.getSimpleName(),"get date time in yyyy/MM/dd="+output);
        return output;
    }





    // Get the begining date of month


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


        Logger.log(Level.DEBUG,TestReportMonthFragment.class.getSimpleName(),"STarting date="+start_date);

        return start_date;
    }


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

        Logger.log(Level.DEBUG,TestReportMonthFragment.class.getSimpleName(),"Ending  date="+end_date);
        return end_date;
    }




    // get no.of list of dates

        public  List<String> getDaysBetweenDates(String  start_date, String end_date)
        {

            List<String> dates_list_=new ArrayList<>();
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
                Logger.log(Level.DEBUG, TestReportMonthFragment.class.getSimpleName(),"str="+str);
                try {
                    d=dfx.parse(str);
                    start.add(Calendar.DAY_OF_YEAR, 1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(),"List of dates="+dates_list_);
            return dates_list_;
        }



    private void plot_hr_month_graph(List<GrowthMonthModel> dateslist,Context context)
    {



        int hr_list[] =new int[dateslist.size()];

        for(int i=0;i<hr_list.length;i++)
        {
            hr_list[i]=Integer.parseInt(dateslist.get(i).getPr());
        }

        DataPoint[] dataPoints = new DataPoint[dateslist.size()];


        for (int i =0; i <dataPoints.length; i++) {
           dataPoints[i] = new DataPoint(DateTime.convert_string_to_date(dateslist.get(i).getDate()),hr_list[i]);

        }

        LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<DataPoint>(dataPoints);
        Viewport viewport = hr_graph.getViewport();

        mSeries.setThickness(3);


        hr_graph.setTitleColor(Color.BLACK);
        hr_graph.getGridLabelRenderer().setTextSize(22f);
        mSeries.setDrawDataPoints(true);

        mSeries.setDataPointsRadius(3);

        mSeries.setColor(Utility.getColorWrapper(getActivity(), R.color.header_itltle_color));
        // mSeries.setDrawBackground(true);
        // mSeries.setBackgroundColor(Color.BLACK);

        hr_graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        hr_graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        hr_graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
        // graphview_spo2.getGridLabelRenderer().set
        hr_graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        //  graphview_pr.getGridLabelRenderer().setTextSize(25f);
        hr_graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        hr_graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        hr_graph.getGridLabelRenderer().setHighlightZeroLines(false);
        hr_graph.setBackgroundColor(Color.TRANSPARENT);
        hr_graph.getGridLabelRenderer().setGridColor(Color.CYAN);


        hr_graph.addSeries(mSeries);
        hr_graph.getViewport().setXAxisBoundsManual(true);
        //hr_graph.getViewport().setMinX(Double.parseDouble(dateslist.get(0).getDate().toString()));
        //hr_graph.getViewport().setMaxX(Double.parseDouble(dateslist.get(5).getDate().toString()));
        hr_graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        hr_graph.getGridLabelRenderer().setNumVerticalLabels(8);

        // StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(hr_graph);
        // staticLabelsFormatter.setHorizontalLabels(x_label_arr);

        // hr_graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        hr_graph.getViewport().setYAxisBoundsManual(true);
        hr_graph.getViewport().setMinY(0);  // set the min value of the viewport of y axis
        hr_graph.getViewport().setMaxY(140);
        hr_graph.getGridLabelRenderer().setLabelsSpace(0);// add padding
        hr_graph .getGridLabelRenderer().reloadStyles();


        hr_graph.getViewport().setScrollable(true);
        hr_graph.getViewport().setScalable(true);

    }



    private void plot_spo2_month_graph(List<GrowthMonthModel> dateslist,Context context)
    {

        int spo2_list[] =new int[dateslist.size()];
            String []x_label_arr=new String[dateslist.size()];

            for(int i=0;i<spo2_list.length;i++)
            {
                spo2_list[i]=Integer.parseInt(dateslist.get(i).getSpo2());
                x_label_arr[i]=dateslist.get(i).getDate();
            }

            DataPoint[] dataPoints = new DataPoint[dateslist.size()];


            for (int i =0; i <dataPoints.length; i++) {
                dataPoints[i] = new DataPoint(DateTime.convert_string_to_date(dateslist.get(i).getDate()),spo2_list[i]);

                Logger.log(Level.DEBUG, TAG, "** X axis as Date**=" +DateTime.convert_string_to_date(dateslist.get(i).getDate()));
            }

            LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<DataPoint>(dataPoints);
            Viewport viewport = hr_graph.getViewport();

            mSeries.setThickness(3);


            spo2_graph.setTitleColor(Color.BLACK);
            spo2_graph.getGridLabelRenderer().setTextSize(22f);
            mSeries.setDrawDataPoints(true);
            mSeries.setDataPointsRadius(3);
            mSeries.setColor(Utility.getColorWrapper(getActivity(), R.color.colorAccent));
            // mSeries.setDrawBackground(true);
            // mSeries.setBackgroundColor(Color.BLACK);

        spo2_graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        spo2_graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
       spo2_graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));

        spo2_graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
            //  graphview_pr.getGridLabelRenderer().setTextSize(25f);
        spo2_graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        spo2_graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        spo2_graph.getGridLabelRenderer().setHighlightZeroLines(false);
        spo2_graph.setBackgroundColor(Color.TRANSPARENT);
        spo2_graph.getGridLabelRenderer().setGridColor(Color.CYAN);


        spo2_graph.addSeries(mSeries);

        //spo2_graph.getViewport().setMinX(com.bploximeter.constants.DateTime.convert_string_to_date(dateslist.get(0).getDate()).getTime());
       // spo2_graph.getViewport().setMaxX(com.bploximeter.constants.DateTime.convert_string_to_date(dateslist.get(3).getDate()).getTime());
            //hr_graph.getGridLabelRenderer().setNumHorizontalLabels(6);

        // hr_graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);


        spo2_graph.getViewport().setYAxisBoundsManual(true);
        spo2_graph.getViewport().setMinY(0);  // set the min value of the viewport of y axis
        spo2_graph.getViewport().setMaxY(100);
        spo2_graph.getGridLabelRenderer().setNumHorizontalLabels(3);
        spo2_graph.getGridLabelRenderer().setNumVerticalLabels(6);
        spo2_graph.getGridLabelRenderer().setLabelsSpace(0);// add padding
        spo2_graph .getGridLabelRenderer().reloadStyles();
        spo2_graph.getViewport().setXAxisBoundsManual(true);


        spo2_graph.onDataChanged(true,true);
        spo2_graph.getViewport().setScrollable(true);
        spo2_graph.getViewport().setScalable(true);


    }



    public  void hasDuplicates(List<RecordsDetail> m_Lists) {
         uniqe_dates_list = new ArrayList<>();
        final List<String> duplictae = new ArrayList<String>();
        for (RecordsDetail gr :m_Lists) {
            final String []date = gr.getTesting_time().split(" ");
            Logger.log(Level.DEBUG,TAG,"**String []date**="+date[0]);

            if (duplictae.contains(date[0])) {
                return;
            }

            RecordsDetail rd=new RecordsDetail(gr.getSpo2(),gr.getHeartrate(),gr.getPI(),date[0], "0",gr.getDevice_mac_id());
            uniqe_dates_list.add(rd);
            duplictae.add(date[0]);
        }

        Logger.log(Level.DEBUG,TAG,"**Uniqe dates list**="+uniqe_dates_list.size());
        for(RecordsDetail r:uniqe_dates_list)
        {
            Logger.log(Level.DEBUG,TAG,"**Uniqe dates list**="+r.getTesting_time());
            Logger.log(Level.DEBUG,TAG,"**Uniqe dates list**="+r.getSpo2());
        }


        // call maiden mlists

    }



    public List<RecordsDetail> get_avg_spo2_hr(List<RecordsDetail>uniqe_dates_list)
    {
       final_input_month_lists=new ArrayList<>();
        final_output_month_lists=new ArrayList<>();
        int px_spo2=0;
        int px_hr=0;
        for(RecordsDetail m:uniqe_dates_list)
        {
           String []spo2= m.getSpo2().split(",");
            String []hr=m.getHeartrate().split(",");
           for(int i=0;i<spo2.length;i++)
           {
               px_spo2=px_spo2+Integer.parseInt(spo2[i]);
           }
            for(int i=0;i<hr.length;i++)
            {
                px_hr=px_hr+Integer.parseInt(hr[i]);
            }

            RecordsDetail r=new RecordsDetail(String.valueOf(px_spo2/spo2.length),String.valueOf(px_hr/hr.length),"0",m.getTesting_time(),"0","");

            final_input_month_lists.add(r);
        }
        Logger.log(Level.DEBUG,TAG,"**final_input_month_lists**="+final_input_month_lists.size());
        return final_input_month_lists;
    }



    private List<GrowthMonthModel> compare_lists(List<GrowthMonthModel> final_output_month_lists,List<RecordsDetail> final_input_month_lists)
    {

        for(int i=0;i<final_output_month_lists.size();i++)
        {
            for(int j=0;j<final_input_month_lists.size();j++)
            {
                if(final_output_month_lists.get(i).getDate().equals(final_input_month_lists.get(j).getTesting_time()))
                {
                    GrowthMonthModel g=new GrowthMonthModel();
                    g.setDate(final_input_month_lists.get(j).getTesting_time());
                    g.setDuration("0");
                    g.setSpo2(final_input_month_lists.get(j).getSpo2());
                    g.setPr(final_input_month_lists.get(j).getHeartrate());
                    final_output_month_lists.set(i,g);
                }
            }
        }

        Logger.log(Level.DEBUG,TAG,"***final_output_month_lists="+final_output_month_lists.size());
        for(int i=0;i<final_output_month_lists.size();i++)
        {
            Logger.log(Level.DEBUG,TAG,"***final_output_month_lists"+final_output_month_lists.get(i).getDate());
            Logger.log(Level.DEBUG,TAG,"***final_output_month_lists"+final_output_month_lists.get(i).getSpo2());
            Logger.log(Level.DEBUG,TAG,"***final_output_month_lists"+final_output_month_lists.get(i).getPr());
        }


        return final_output_month_lists;

    }
}
