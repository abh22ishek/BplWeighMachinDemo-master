package testreport;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import constantsP.Constants;
import constantsP.Utility;
import jjoe64.graphview.DefaultLabelFormatter;
import jjoe64.graphview.GraphView;
import jjoe64.graphview.GridLabelRenderer;
import jjoe64.graphview.Viewport;
import jjoe64.graphview.series.DataPoint;
import jjoe64.graphview.series.LineGraphSeries;
import logger.Level;
import logger.Logger;
import model.RecordsDetail;
import test.bpl.com.bplscreens.R;



public class TestReportYearFragment extends Fragment {

    GraphView hr_graph;
    GraphView spo2_graph;

    TextView time_hr;
    TextView time_spo2;

    String day_time="";
    String spo2_str="";
    String hr_str="";
    String year="";

    List<RecordsDetail> same_year_date_list;
    List<RecordsDetail>uniqe_month_list;
    List<String> duplictae_list;

    final String TAG=TestReportYearFragment.class.getSimpleName();
    List<Integer> mSpo2_list_year;
    List<Integer> mHr_list_year;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.chart_year,container,false);
        hr_graph=  view.findViewById(R.id.graph_heart_rate);
        spo2_graph= view.findViewById(R.id.graph_spo2);
        time_hr=  view.findViewById(R.id.time_hr);
        time_spo2= view.findViewById(R.id.time_spo2);
        Logger.log(Level.DEBUG, TestReportYearFragment.class.getSimpleName(), "On onCreateView() method gets called ++");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.log(Level.DEBUG, TestReportYearFragment.class.getSimpleName(), "On onActivityCreated() method gets called ++");

        if(null!=getArguments().getString(Constants.TESTING_TIME_CONSTANTS))
        {
            day_time=getArguments().getString(Constants.TESTING_TIME_CONSTANTS,"no time");
            year=getArguments().getString(Constants.YEAR,"2015");

        }

        Logger.log(Level.DEBUG,TAG,"++(Utility.mRecord_detail_list.size="+ Utility.mRecord_detail_list.size());

        same_year_date_list=new ArrayList<RecordsDetail>();
        for(int i=0;i<Utility.mRecord_detail_list.size();i++)
        {
            if(Utility.mRecord_detail_list.get(i).getTesting_time().substring(6,10).equals(year))
            {
                same_year_date_list.add(Utility.mRecord_detail_list.get(i));
            }
        }
        Logger.log(Level.DEBUG,TAG,"---****---"+same_year_date_list.size()+""+same_year_date_list);
        hasDuplicates(same_year_date_list);
        Logger.log(Level.DEBUG, TAG, "**Uniqe dates list**=" + uniqe_month_list.size());
        for(RecordsDetail r:uniqe_month_list)
        {
            Logger.log(Level.DEBUG,TAG,"**Uniqe dates list**="+r.getTesting_time());
            Logger.log(Level.DEBUG,TAG,"**Uniqe dates list**="+r.getSpo2());
        }

        calculate_year_readings_spo2(uniqe_month_list);
        calculate_year_readings_hr(uniqe_month_list);
        init();




    }

    private void init()
    {

        time_hr.setText("Year :" + day_time.substring(6, 10));
        time_hr.setTextColor(Utility.getColorWrapper(getActivity(), R.color.header_itltle_color));
        time_spo2.setText("Year:" + day_time.substring(6, 10));
        time_spo2.setTextColor(Utility.getColorWrapper(getActivity(), R.color.colorAccent));
        List<String> spo2_list=new ArrayList<String>(Arrays.asList(spo2_str.split(",")));
        List<String> hr_list=new ArrayList<String>(Arrays.asList(hr_str.split(",")));
        Logger.log(Level.DEBUG, TestReportDayFragment.class.getSimpleName(), "Get SPO2list=" + spo2_list);



    }



    private void plot_spo2_day_graph(List<Integer> mspo2_list) {



        DataPoint[] dataPoints = new DataPoint[mspo2_list.size()];


        for (int i =0; i <dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(i,mspo2_list.get(i));

        }

        LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<>(dataPoints);
        Viewport viewport = spo2_graph.getViewport();
        // viewport.setBackgroundColor(getResources().getColor(R.color.separator));
        mSeries.setThickness(3);
        //  spo2_graph.setTitle("PR");


        mSeries.setColor(Utility.getColorWrapper(getActivity(), R.color.colorAccent));
        // mSeries.setDrawBackground(true);
        // mSeries.setBackgroundColor(Color.BLACK);
        spo2_graph.getGridLabelRenderer().setTextSize(23f);
        spo2_graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        spo2_graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        // graphview_spo2.getGridLabelRenderer().set
        spo2_graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        //  graphview_pr.getGridLabelRenderer().setTextSize(25f);
        spo2_graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        spo2_graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        spo2_graph.getGridLabelRenderer().setHighlightZeroLines(false);
        spo2_graph.setBackgroundColor(Color.TRANSPARENT);
        spo2_graph.getGridLabelRenderer().setGridColor(Color.CYAN);
        spo2_graph.getGridLabelRenderer().reloadStyles();
        mSeries.setDrawDataPoints(true);

        mSeries.setDataPointsRadius(5);
        spo2_graph.addSeries(mSeries);
     //   spo2_graph.getGridLabelRenderer().setNumHorizontalLabels(12);
        spo2_graph.getViewport().setXAxisBoundsManual(true);
        spo2_graph.getViewport().setMinX(1);
        spo2_graph.getViewport().setMaxX(5);


        spo2_graph.getGridLabelRenderer().setNumVerticalLabels(7);

      //  NumberFormat nf = NumberFormat.getInstance();
       // nf.setMinimumFractionDigits(0);
       // nf.setMinimumIntegerDigits(2);

       // spo2_graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));
        //graph2.getViewport().setBackgroundColor();
        spo2_graph.getViewport().setYAxisBoundsManual(true);
        spo2_graph.getViewport().setMinY(0);  // set the min value of the viewport of y axis
        spo2_graph.getViewport().setMaxY(120);
        spo2_graph.getGridLabelRenderer().setLabelsSpace(4);

      //  spo2_graph.getViewport().setScalable(true);
        spo2_graph.getViewport().setScrollable(true);


        spo2_graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {

                    if(String.valueOf(value).length()<2)
                    {

                    }
                    return super.formatLabel(value+1, isValueX)+"/12";
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

    }



    private void plot_hr_day_graph(List<Integer> mHrlist)
    {

        DataPoint[] dataPoints = new DataPoint[mHrlist.size()];


        for (int i =0; i <dataPoints.length; i++) {
            dataPoints[i] = new DataPoint(i,mHrlist.get(i));

        }

        LineGraphSeries<DataPoint> mSeries = new LineGraphSeries<DataPoint>(dataPoints);
        Viewport viewport = hr_graph.getViewport();
        // viewport.setBackgroundColor(getResources().getColor(R.color.separator));
        mSeries.setThickness(3);
        //  spo2_graph.setTitle("PR");
        hr_graph.getGridLabelRenderer().setTextSize(23f);

        mSeries.setColor(Utility.getColorWrapper(getActivity(), R.color.header_itltle_color));
        // mSeries.setDrawBackground(true);
        // mSeries.setBackgroundColor(Color.BLACK);

        hr_graph.getGridLabelRenderer().setHorizontalLabelsVisible(true);// remove horizontal x labels and line
        hr_graph.getGridLabelRenderer().setVerticalLabelsVisible(true);
        // graphview_spo2.getGridLabelRenderer().set
        hr_graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        //  graphview_pr.getGridLabelRenderer().setTextSize(25f);
        hr_graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        hr_graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        hr_graph.getGridLabelRenderer().setHighlightZeroLines(false);
        hr_graph.setBackgroundColor(Color.TRANSPARENT);
        hr_graph.getGridLabelRenderer().setGridColor(Color.CYAN);
        hr_graph.getGridLabelRenderer().reloadStyles();
        mSeries.setDrawDataPoints(true);

        mSeries.setDataPointsRadius(5);
        hr_graph.addSeries(mSeries);
        //   spo2_graph.getGridLabelRenderer().setNumHorizontalLabels(12);
        hr_graph.getViewport().setXAxisBoundsManual(true);
        hr_graph.getViewport().setMinX(1);
        hr_graph.getViewport().setMaxX(5);


        hr_graph.getGridLabelRenderer().setNumVerticalLabels(10);

        //  NumberFormat nf = NumberFormat.getInstance();
        // nf.setMinimumFractionDigits(0);
        // nf.setMinimumIntegerDigits(2);

        // spo2_graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));
        //graph2.getViewport().setBackgroundColor();
        hr_graph.getViewport().setYAxisBoundsManual(true);
        hr_graph.getViewport().setMinY(0);  // set the min value of the viewport of y axis
        hr_graph.getViewport().setMaxY(180);
        hr_graph.getGridLabelRenderer().setLabelsSpace(4);


        hr_graph.getViewport().setScrollable(true);
       // hr_graph.getViewport().setScalable(true);


        hr_graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {

                    return super.formatLabel(value+1, isValueX)+"/12";
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

    }



   /* public  int daysOfMonth(int year, int month) {
        org.joda.time.DateTime dateTime = new org.joda.time.DateTime(year, month, 14, 12, 0, 0, 000);
        return dateTime.dayOfMonth().getMaximumValue();
    }*/


    public  void hasDuplicates(List<RecordsDetail> m_Lists) {
       uniqe_month_list = new ArrayList<RecordsDetail>();
        duplictae_list = new ArrayList<String>();
        for (RecordsDetail gr :m_Lists) {
            final String []date = gr.getTesting_time().split(" ");
            Logger.log(Level.DEBUG,TAG,"**String []date**="+date[0]);

            if (duplictae_list.contains(date[0])) {

                if(duplictae_list.contains(date[0]) )
                return ;

            }

            RecordsDetail rd=new RecordsDetail(gr.getSpo2(),gr.getHeartrate(),gr.getPI(),date[0], "0","");
            uniqe_month_list.add(rd);
            duplictae_list.add(date[0]);

        }



        // call maiden mlists

    }


    public void calculate_year_readings_spo2(List<RecordsDetail>uniqe_month_list)
    {
        mSpo2_list_year=new ArrayList<Integer>();


        int px_jan=0;
        int counter_jan=0;
        int px_feb=0; int counter_feb=0;
        int px_mar=0; int counter_mar=0;
        int px_apr=0; int counter_apr=0;
        int px_may=0; int counter_may=0;
        int px_jun=0; int counter_jun=0;
        int px_jul=0; int counter_jul=0;
        int px_aug=0; int counter_aug=0;
        int px_sep=0; int counter_sep=0;
        int px_oct=0; int counter_oct=0;
        int px_nov=0; int counter_nov=0;
        int px_dec=0; int counter_dec=0;

        for(int x=0;x<uniqe_month_list.size();x++)
        {
            if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("01"))
            {
                counter_jan++;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                int cx_spo2=0;

                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }
                cx_spo2=cx_spo2/px_spo2.length;
                px_jan=px_jan+cx_spo2;
            }

            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("02"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }

                cx_spo2=cx_spo2/px_spo2.length;
                px_feb=px_feb+cx_spo2;
                counter_feb++;
            }else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("03"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }

                cx_spo2=cx_spo2/px_spo2.length;
                px_mar=px_mar+cx_spo2;
                counter_mar++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("04"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }

                cx_spo2=cx_spo2/px_spo2.length;
                px_apr=px_apr+cx_spo2;
                counter_apr++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("05"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }
                cx_spo2=cx_spo2/px_spo2.length;

                px_may=px_may+cx_spo2;
                counter_may++;
            }else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("06"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }

                cx_spo2=cx_spo2/px_spo2.length;
                px_jun=px_jun+cx_spo2;
                counter_jun++;
            }else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("07"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }
                cx_spo2=cx_spo2/px_spo2.length;

                px_jul=px_jul+cx_spo2;
                counter_jul++;
            }else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("08"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }

                cx_spo2=cx_spo2/px_spo2.length;
                px_aug=px_aug+cx_spo2;
                counter_aug++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("09"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }

                cx_spo2=cx_spo2/px_spo2.length;
                px_sep=px_sep+cx_spo2;
                counter_sep++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("10"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }

                cx_spo2=cx_spo2/px_spo2.length;
                px_oct=px_oct+cx_spo2;
                counter_oct++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("11"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }

                cx_spo2=cx_spo2/px_spo2.length;
                px_nov=px_nov+cx_spo2;
                counter_nov++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("12"))
            {
                int cx_spo2=0;
                String [] px_spo2=uniqe_month_list.get(x).getSpo2().split(",");
                for(int i=0;i<px_spo2.length;i++)
                {
                    cx_spo2=cx_spo2+Integer.parseInt(px_spo2[i]);
                }
                cx_spo2=cx_spo2/px_spo2.length;

                px_dec=px_dec+cx_spo2;
                counter_dec++;
            }
        }


        if(counter_jan==0)
        px_jan=px_jan/1;
        else
            px_jan=px_jan/counter_jan;

        if(counter_feb==0)
            px_feb=px_feb/1;
        else
            px_feb=px_feb/counter_feb;

        if(counter_mar==0)
            px_mar=px_mar/1;
        else
            px_mar=px_mar/counter_mar;

        if(counter_apr==0)
            px_apr=px_apr/1;
        else
            px_apr=px_apr/counter_apr;

        if(counter_may==0)
            px_may=px_may/1;
        else
            px_may=px_may/counter_may;


        if(counter_jun==0)
            px_jun=px_jun/1;
        else
            px_jun=px_jun/counter_jun;

        if(counter_jul==0)
            px_jul=px_jul/1;
        else
            px_jul=px_jul/counter_jul;

        if(counter_aug==0)
            px_aug=px_aug/1;
        else
            px_aug=px_aug/counter_aug;


        if(counter_sep==0)
            px_sep=px_sep/1;
        else
            px_sep=px_sep/counter_sep;


        if(counter_oct==0)
            px_oct=px_oct/1;
        else
            px_oct=px_oct/counter_oct;




        if(counter_nov==0)
            px_nov=px_nov/1;
        else
            px_nov=px_nov/counter_nov;

        if(counter_dec==0)
            px_dec=px_dec/1;
        else
            px_dec=px_dec/counter_dec;











        mSpo2_list_year.add(px_jan);
        mSpo2_list_year.add(px_feb);
        mSpo2_list_year.add(px_mar);
        mSpo2_list_year.add(px_apr);
        mSpo2_list_year.add(px_may);
        mSpo2_list_year.add(px_jun);
        mSpo2_list_year.add(px_jul);
        mSpo2_list_year.add(px_aug);
        mSpo2_list_year.add(px_sep);
        mSpo2_list_year.add(px_oct);
        mSpo2_list_year.add(px_nov);
        mSpo2_list_year.add(px_dec);



        // plot the graph
        plot_spo2_day_graph(mSpo2_list_year);

    }


    public void calculate_year_readings_hr(List<RecordsDetail>uniqe_month_list)
    {
        mHr_list_year=new ArrayList<Integer>();


        int px_jan=0;
        int counter_jan=0;
        int px_feb=0; int counter_feb=0;
        int px_mar=0; int counter_mar=0;
        int px_apr=0; int counter_apr=0;
        int px_may=0; int counter_may=0;
        int px_jun=0; int counter_jun=0;
        int px_jul=0; int counter_jul=0;
        int px_aug=0; int counter_aug=0;
        int px_sep=0; int counter_sep=0;
        int px_oct=0; int counter_oct=0;
        int px_nov=0; int counter_nov=0;
        int px_dec=0; int counter_dec=0;

        for(int x=0;x<uniqe_month_list.size();x++)
        {
            if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("01"))
            {
                counter_jan++;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                int cx_hr=0;

                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }
                cx_hr=cx_hr/px_hr.length;
                px_jan=px_jan+cx_hr;
            }

            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("02"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }

                cx_hr=cx_hr/px_hr.length;
                px_feb=px_feb+cx_hr;
                counter_feb++;
            }else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("03"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }

                cx_hr=cx_hr/px_hr.length;
                px_mar=px_mar+cx_hr;
                counter_mar++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("04"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }

                cx_hr=cx_hr/px_hr.length;
                px_apr=px_apr+cx_hr;
                counter_apr++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("05"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }
                cx_hr=cx_hr/px_hr.length;

                px_may=px_may+cx_hr;
                counter_may++;
            }else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("06"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }

                cx_hr=cx_hr/px_hr.length;
                px_jun=px_jun+cx_hr;
                counter_jun++;
            }else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("07"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }
                cx_hr=cx_hr/px_hr.length;

                px_jul=px_jul+cx_hr;
                counter_jul++;
            }else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("08"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }

                cx_hr=cx_hr/px_hr.length;
                px_aug=px_aug+cx_hr;
                counter_aug++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("09"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }

                cx_hr=cx_hr/px_hr.length;
                px_sep=px_sep+cx_hr;
                counter_sep++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("10"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }

                cx_hr=cx_hr/px_hr.length;
                px_oct=px_oct+cx_hr;
                counter_oct++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("11"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }

                cx_hr=cx_hr/px_hr.length;
                px_nov=px_nov+cx_hr;
                counter_nov++;
            }
            else if(uniqe_month_list.get(x).getTesting_time().substring(3,5).equals("12"))
            {
                int cx_hr=0;
                String [] px_hr=uniqe_month_list.get(x).getHeartrate().split(",");
                for(int i=0;i<px_hr.length;i++)
                {
                    cx_hr=cx_hr+Integer.parseInt(px_hr[i]);
                }
                cx_hr=cx_hr/px_hr.length;

                px_dec=px_dec+cx_hr;
                counter_dec++;
            }
        }


        if(counter_jan==0)
            px_jan=px_jan/1;
        else
            px_jan=px_jan/counter_jan;

        if(counter_feb==0)
            px_feb=px_feb/1;
        else
            px_feb=px_feb/counter_feb;

        if(counter_mar==0)
            px_mar=px_mar/1;
        else
            px_mar=px_mar/counter_mar;

        if(counter_apr==0)
            px_apr=px_apr/1;
        else
            px_apr=px_apr/counter_apr;

        if(counter_may==0)
            px_may=px_may/1;
        else
            px_may=px_may/counter_may;


        if(counter_jun==0)
            px_jun=px_jun/1;
        else
            px_jun=px_jun/counter_jun;

        if(counter_jul==0)
            px_jul=px_jul/1;
        else
            px_jul=px_jul/counter_jul;

        if(counter_aug==0)
            px_aug=px_aug/1;
        else
            px_aug=px_aug/counter_aug;


        if(counter_sep==0)
            px_sep=px_sep/1;
        else
            px_sep=px_sep/counter_sep;


        if(counter_oct==0)
            px_oct=px_oct/1;
        else
            px_oct=px_oct/counter_oct;




        if(counter_nov==0)
            px_nov=px_nov/1;
        else
            px_nov=px_nov/counter_nov;

        if(counter_dec==0)
            px_dec=px_dec/1;
        else
            px_dec=px_dec/counter_dec;











        mHr_list_year.add(px_jan);
        mHr_list_year.add(px_feb);
        mHr_list_year.add(px_mar);
        mHr_list_year.add(px_apr);
        mHr_list_year.add(px_may);
        mHr_list_year.add(px_jun);
        mHr_list_year.add(px_jul);
        mHr_list_year.add(px_aug);
        mHr_list_year.add(px_sep);
        mHr_list_year.add(px_oct);
        mHr_list_year.add(px_nov);
        mHr_list_year.add(px_dec);

        plot_hr_day_graph(mHr_list_year);

    }
}
