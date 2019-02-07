package testreport;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import constantsP.Constants;
import constantsP.DateTime;
import constantsP.Utility;
import customviews.MyCustomChartHRDay;
import customviews.MyCustomChartSpo2day;
import jjoe64.graphview.DefaultLabelFormatter;
import jjoe64.graphview.GraphView;
import jjoe64.graphview.GridLabelRenderer;
import jjoe64.graphview.Viewport;
import jjoe64.graphview.series.DataPoint;
import jjoe64.graphview.series.LineGraphSeries;
import logger.Level;
import logger.Logger;
import test.bpl.com.bplscreens.R;


public class TestReportDayFragment extends Fragment {

    TextView time_hr;
    TextView time_spo2;
    public static String day_time="";
    String spo2_str="";
    String hr_str="";
    GraphView hr_graph;
    GraphView spo2_graph;

    MyCustomChartHRDay custom_day_chart;
    MyCustomChartSpo2day custom_day_spo2;

   public static String []mpr_list;
    public static String []mspo2_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.chart_day,container,false);

        hr_graph=  view.findViewById(R.id.graph_heart_rate);
        spo2_graph=  view.findViewById(R.id.graph_spo2);
        time_hr=  view.findViewById(R.id.time_hr);
        time_spo2= view.findViewById(R.id.time_spo2);

        if(null!=getArguments().getString(Constants.TESTING_TIME_CONSTANTS))
        {
            day_time=getArguments().getString(Constants.TESTING_TIME_CONSTANTS,"no time");
            spo2_str= getArguments().getString(Constants.SPO2_CONSTANTS,"");
            hr_str=getArguments().getString(Constants.PR_CONSTANTS,"");

        }

        custom_day_chart=  view.findViewById(R.id.custom_day_chart);
        custom_day_spo2= view.findViewById(R.id.custom_spo2_chart);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }


    private void init()
    {

        time_hr.setText("Date :" + day_time);
        time_hr.setTextColor(Utility.getColorWrapper(getActivity(), R.color.header_itltle_color));
        time_spo2.setText("Date :" + day_time);
        time_spo2.setTextColor(Utility.getColorWrapper(getActivity(), R.color.colorAccent));

       List<String> spo2_list=new ArrayList<String>(Arrays.asList(spo2_str.split(",")));

        List<String> hr_list=new ArrayList<String>(Arrays.asList(hr_str.split(",")));
        Logger.log(Level.DEBUG, TestReportDayFragment.class.getSimpleName(), "Get SPO2list=" + spo2_list);


       remove_decimal_and_after();


      //  plot_hr_day_graph(hr_str.split(","));
        mpr_list=hr_str.split(",");
        mspo2_list=spo2_str.split(",");


       // plot_spo2_day_graph(spo2_str.split(","));

    }


    private String remove_decimal_and_after()
    {
        String output="120456.43";
        String str=output.substring(0,2);
        String str2=output.substring(2,4);
        String str3=output.substring(4,6);


       int hh= Integer.parseInt(str);
        int mm=Integer.parseInt(str2);
        int ss=Integer.parseInt(str3);

        if(ss>60)
        {
            mm=mm+1;
        }

        if(mm>60)
        {
            hh=hh+1;
        }




        Logger.log(Level.DEBUG, TestReportDayFragment.class.getSimpleName(), "substring of a string+++"+str
        +"\n"+str2+
        "\n"+str3);
        return str;
    }


    private void plot_hr_day_graph(String [] hr_list)
    {
        String time[]=day_time.split(" ");
        String time_str=time[1];// hh:mm:ss

        final int[] hour_array=new int[hr_list.length];

        Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(),"time_str="+time_str.replace(":",""));
        DataPoint[] dataPoints = new DataPoint[hr_list.length];
        double x_val= DateTime.time_to_seconds(time_str);// sec


        Logger.log(Level.DEBUG, TestReportDayFragment.class.getSimpleName(), "x_val=" + x_val);
       // dataPoints[0]=new DataPoint(Double.parseDouble(red),Double.parseDouble(hr_list[0]));
        int tx= Integer.parseInt(time_str.replace(":", ""));
        int x_px=Integer.parseInt(time_str.replace(":", ""));
        Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "tx=" +tx);

        for(int i=0;i<hr_list.length;i++)
        {


            hour_array[i] = Integer.parseInt(Integer.toString(tx).substring(0, 1));
            dataPoints[i]= new DataPoint(tx, Double.parseDouble(hr_list[i]));
            tx=tx+1;
            Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "tx=" +tx);

        }


        LineGraphSeries<DataPoint> mSeries=new LineGraphSeries<DataPoint>(dataPoints);
        Viewport viewport = hr_graph.getViewport();
        mSeries.setThickness(5);


        hr_graph.setTitleColor(Color.BLACK);
        hr_graph.getGridLabelRenderer().setTextSize(22f);
        mSeries.setDrawDataPoints(true);

        mSeries.setDataPointsRadius(5);

        mSeries.setColor(Utility.getColorWrapper(getActivity(), R.color.header_itltle_color));

        hr_graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        hr_graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        hr_graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        hr_graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        hr_graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);
        hr_graph.getGridLabelRenderer().setHighlightZeroLines(false);
        hr_graph.setBackgroundColor(Color.TRANSPARENT);
        hr_graph.getGridLabelRenderer().setGridColor(Color.CYAN);
        hr_graph .getGridLabelRenderer().reloadStyles();

        hr_graph.addSeries(mSeries);
        hr_graph.getViewport().setXAxisBoundsManual(true);
        hr_graph.getViewport().setMinX(x_px);
        hr_graph.getViewport().setMaxX(x_px+20);
         hr_graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        hr_graph.getGridLabelRenderer().setNumVerticalLabels(10);

        hr_graph.getViewport().setYAxisBoundsManual(true);
        hr_graph.getViewport().setMinY(20);  // set the min value of the viewport of y axis
        hr_graph.getViewport().setMaxY(200);
        hr_graph.getGridLabelRenderer().setLabelsSpace(7);// add padding



        hr_graph.onDataChanged(true,true);
        hr_graph.getViewport().setScrollable(true);
        hr_graph.getViewport().setScalable(true);

        hr_graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    //
                    //Logger.log(Level.DEBUG, TestReportDayFragment.class.getSimpleName(), "**value**" + value);

                  Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName()," sec to  hh:mm:ss++"+DateTime.secondsTominutesHours((long) (value * 1000)));

                    String str_pax1=String.valueOf(value).substring(0,2);
                    String str_pax2=String.valueOf(value).substring(2,4);
                    String str_pax3=String.valueOf(value).substring(4,6);

                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "pax888=" +str_pax1);
                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "pax2=" +str_pax2);
                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "pax3=" +str_pax3);

                    int hh_=Integer.parseInt(String.valueOf(value).substring(0,2));// hh
                    int mm_=Integer.parseInt(String.valueOf(value).substring(2,4));// mm
                    int ss_=Integer.parseInt(String.valueOf(value).substring(4,6));// ss



                 if(ss_>60)
                 {
                     ss_=ss_-60;
                     mm_=mm_+1;

                 }

                    if(mm_>=60)
                    {
                        mm_=mm_-60;
                        hh_=hh_+1;
                    }

                    String mm_str="";
                    String ss_str="";
                    if(mm_<10)
                        mm_str="0"+mm_;
                    else
                        mm_str=""+mm_;
                    if(ss_<10)
                        ss_str="0"+ss_;
                    else
                    ss_str=""+ss_;

                    return super.formatLabel(hh_,isValueX)+":"+mm_str+":"+ss_str;
                } else {
                    // show  for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });


    }




    private void plot_spo2_day_graph(String []spo2_list)
    {
        String time[]=day_time.split(" ");
        String time_str=time[1];// hh:mm:ss

        final int[] hour_array=new int[spo2_list.length];

        Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(),"time_str="+time_str.replace(":",""));
        DataPoint[] dataPoints = new DataPoint[spo2_list.length];
        double x_val=DateTime.time_to_seconds(time_str);// sec


        Logger.log(Level.DEBUG, TestReportDayFragment.class.getSimpleName(), "x_val=" + x_val);
        int tx= Integer.parseInt(time_str.replace(":", ""));
        int x_px=Integer.parseInt(time_str.replace(":", ""));
        Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "tx=" +tx);



        for(int i=0;i<spo2_list.length;i++)
        {

            hour_array[i] = Integer.parseInt(Integer.toString(tx).substring(0, 1));
            dataPoints[i]= new DataPoint(tx, Double.parseDouble(spo2_list[i]));
            tx=tx+1;
            Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "tx=" +tx);

        }


        LineGraphSeries<DataPoint> mSeries=new LineGraphSeries<DataPoint>(dataPoints);
        Viewport viewport = hr_graph.getViewport();
        // viewport.setBackgroundColor(getResources().getColor(R.color.separator));
        mSeries.setThickness(5);


       // spo2_graph.setTitleColor(Color.BLACK);
        spo2_graph.getGridLabelRenderer().setTextSize(22f);
        mSeries.setDrawDataPoints(true);

        mSeries.setDataPointsRadius(5);

        mSeries.setColor(Utility.getColorWrapper(getActivity(), R.color.colorAccent));
        // mSeries.setDrawBackground(true);
        // mSeries.setBackgroundColor(Color.BLACK);

        spo2_graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        spo2_graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        spo2_graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        spo2_graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        spo2_graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.BOTH);

        spo2_graph.getGridLabelRenderer().setHighlightZeroLines(false);
        spo2_graph.setBackgroundColor(Color.TRANSPARENT);
        spo2_graph.getGridLabelRenderer().setGridColor(Color.CYAN);
        spo2_graph .getGridLabelRenderer().reloadStyles();

        spo2_graph.addSeries(mSeries);
        spo2_graph.getViewport().setXAxisBoundsManual(true);
        spo2_graph.getViewport().setMinX(x_px);
        spo2_graph.getViewport().setMaxX(x_px+20);
        spo2_graph.getGridLabelRenderer().setNumHorizontalLabels(5);
        spo2_graph.getGridLabelRenderer().setNumVerticalLabels(5);


        spo2_graph.getViewport().setYAxisBoundsManual(true);
        spo2_graph.getViewport().setMinY(80);  // set the min value of the viewport of y axis
        spo2_graph.getViewport().setMaxY(100);
       spo2_graph.getGridLabelRenderer().setLabelsSpace(7);// add padding



        spo2_graph.onDataChanged(true,true);
        spo2_graph.getViewport().setScrollable(true);
        spo2_graph.getViewport().setScalable(true);

        spo2_graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values

                    Logger.log(Level.DEBUG, TestReportDayFragment.class.getSimpleName(), "**value**" + value);
                    String str_pax1=String.valueOf(value).substring(0,2);
                    String str_pax2=String.valueOf(value).substring(2,4);
                    String str_pax3=String.valueOf(value).substring(4,6);

                   /* int pax1=Integer.parseInt(String.valueOf(value).substring(0,2));
                    int pax2=Integer.parseInt(String.valueOf(value).substring(2,4));
                    int pax3=Integer.parseInt(String.valueOf(value).substring(4,6));
*/
                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "pax888=" +str_pax1);
                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "pax2=" +str_pax2);
                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "pax3=" +str_pax3);




                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "str_pax1=" +str_pax1);
                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "str_pax2=" +str_pax2);
                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(), "str_pax3=" +str_pax3);


                    final Calendar calendar = Calendar.getInstance();

                    calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(str_pax1));
                    calendar.set(Calendar.MINUTE,Integer.parseInt(str_pax2));
                    calendar.set(Calendar.SECOND, Integer.parseInt(str_pax3));
                    String time_label=new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
                    calendar.add(Calendar.SECOND, 1);

                    Logger.log(Level.DEBUG,TestReportDayFragment.class.getSimpleName(),
                            "**calendar counter**"+new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()));



                    int pax1=Integer.parseInt(String.valueOf(value).substring(0,2));
                    int pax2=Integer.parseInt(String.valueOf(value).substring(2,4));
                    int pax3=Integer.parseInt(String.valueOf(value).substring(4,6));


                    if(pax3>60)
                    {
                        pax3=pax3-60;
                        pax2=pax2+1;

                    }

                    if(pax2>=60)
                    {
                        pax2=pax2-60;
                        pax1=pax1+1;
                    }

                    String pax2_str="";
                    String pax3_str="";
                    if(pax2<10)
                        pax2_str="0"+pax2;
                    else
                        pax2_str=""+pax2;
                    if(pax3<10)
                        pax3_str="0"+pax3;
                    else
                        pax3_str=""+pax3;

                    return super.formatLabel(0,isValueX)+time_label;
                } else {
                    // show  for y values
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }



}
