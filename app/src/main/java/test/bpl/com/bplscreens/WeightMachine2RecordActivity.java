package test.bpl.com.bplscreens;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.*;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import bluetooth.ViewListner;
import constantsP.Constants;
import constantsP.GlobalClass;
import customviews.BmiChartView;
import customviews.WeightChartView;
import database.DatabaseManager;
import model.RecordDetailWeighMachine;
import test.bpl.com.bplscreens.fragments.WeighingMachineLocalRecordFragment;


/**
 * Created by abhishek.raj on 05-02-2018.
 */

public class WeightMachine2RecordActivity extends FragmentActivity implements ViewListner,View.OnClickListener{

    public  List<String> dates;
    public  List<String> weight;
    public  List<String> bmi;

    private Button btn_weight,btn_bmi;

    WeightChartView wt_chart;
    BmiChartView bmi_chart;

    private TextView  txt_bmi_chart,txt_wt_chart;
    GlobalClass globalVariable;

    public  List<RecordDetailWeighMachine> UserMeasuredWeightList;

    final String TAG=WeighMachineRecordActivity.class.getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weighing_machine_record);
        final ImageView back;
        final TextView header_title;
        globalVariable = (GlobalClass) getApplicationContext();
        header_title= findViewById(R.id.base_header_title);



        back=  findViewById(R.id.imgBackKey);

        btn_weight= findViewById(R.id.btn_weight_chart);
        btn_bmi=  findViewById(R.id.btn_bmi_chart);


        txt_bmi_chart=  findViewById(R.id.txt_bmi_chart);
        txt_wt_chart=  findViewById(R.id.txt_wt_chart);

        btn_weight.setOnClickListener(this);
        btn_bmi.setOnClickListener(this);

        dates=new ArrayList<>();
        weight=new ArrayList<>();
        bmi=new ArrayList<>();


        final String mUserName=getIntent().getExtras().getString(Constants.USER_NAME);

        int density =this.getResources().getDisplayMetrics().densityDpi;
        if(DisplayMetrics.DENSITY_XXHIGH==density)
        {
            header_title.setTextSize(25);
        }else{
            header_title.setTextSize(22);
        }
        if(mUserName.length()>12)
        {
            String mx= mUserName.substring(0,10)+"..";
            header_title.setText(new StringBuilder().append(mx).append(" 's ").append(getString(R.string.record)).toString());

        }else{
            header_title.setText(new StringBuilder().append(mUserName).append(" 's").append(getString(R.string.record)).toString());

        }


        if (mUserName!= null) {
            DatabaseManager.getInstance().openDatabase();
            UserMeasuredWeightList = new ArrayList<>(DatabaseManager.getInstance().get_measuredWeightMachineB(mUserName));

        }


        wt_chart= findViewById(R.id.weight_chart_);
        bmi_chart=  findViewById(R.id.bmi_chart_);



        wt_chart.set_XY_points(UserMeasuredWeightList);
        bmi_chart.set_XY_points(UserMeasuredWeightList);


        if(UserMeasuredWeightList!=null && UserMeasuredWeightList.size()>=1)
        {
            for(RecordDetailWeighMachine r:UserMeasuredWeightList)
            {
                dates.add(r.getDate());
                weight.add(String.valueOf(r.getWeight()));
                bmi.add(String.valueOf(r.getBmi()));
            }


            btn_bmi.setAlpha(0.8f);
            bmi_chart.setVisibility(View.GONE);
            wt_chart.setVisibility(View.VISIBLE);
            txt_bmi_chart.setVisibility(View.GONE);
            btn_weight.setAlpha(1f);

        }else{

            bmi_chart.setVisibility(View.GONE);
            wt_chart.setVisibility(View.GONE);
            Toast.makeText(WeightMachine2RecordActivity.this,"Sorry No records to display",Toast.LENGTH_SHORT).show();
        }

        bmi_chart.setList(dates,bmi);
        wt_chart.setList(dates,weight);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();

        WeighingMachineLocalRecordFragment fragment = new WeighingMachineLocalRecordFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constants.RECORDS_WEIGH_MACHINE, Constants.RECORDS_WEIGH_MACHINE2);
        bundle.putString("user",mUserName);

        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        //  fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });


    }

    @Override
    public void onClick(View view) {
        if(view==btn_weight)
        {
            btn_bmi.setAlpha(0.8f);
            btn_weight.setAlpha(1f);
            bmi_chart.setVisibility(View.GONE);
            wt_chart.setVisibility(View.VISIBLE);
            txt_bmi_chart.setVisibility(View.GONE);
            txt_wt_chart.setVisibility(View.VISIBLE);

        }else if(view==btn_bmi)
        {
            btn_weight.setAlpha(0.8f);
            btn_bmi.setAlpha(1f);
            bmi_chart.setVisibility(View.VISIBLE);
            wt_chart.setVisibility(View.GONE);
            txt_wt_chart.setVisibility(View.GONE);
            txt_bmi_chart.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void updateViewWeighMachine(String TAG) {

        if(TAG.equalsIgnoreCase(Constants.RECORDS_WEIGH_MACHINE1))
        {
            return;
        }
            List<RecordDetailWeighMachine> weighMachineArrayList = null;
          List<String> dates=new ArrayList<>();
          List<String> weight=new ArrayList<>();
          List<String> bmi=new ArrayList<>();

       if (globalVariable.getUsername() != null) {
            DatabaseManager.getInstance().openDatabase();
            weighMachineArrayList = new ArrayList<>(DatabaseManager.getInstance().get_measuredWeightMachineB(globalVariable.getUsername()));

        }



        if(weighMachineArrayList!=null && weighMachineArrayList.size()>=1)
        {
            for(RecordDetailWeighMachine r:weighMachineArrayList)
            {
                dates.add(r.getDate());
                weight.add(String.valueOf(r.getWeight()));
                bmi.add(String.valueOf(r.getBmi()));
            }


            btn_bmi.setAlpha(0.8f);
            bmi_chart.setVisibility(View.GONE);
            wt_chart.setVisibility(View.VISIBLE);
            txt_bmi_chart.setVisibility(View.GONE);
            btn_weight.setAlpha(1f);

        }else{

            bmi_chart.setVisibility(View.GONE);
            wt_chart.setVisibility(View.GONE);
            Toast.makeText(WeightMachine2RecordActivity.this,"Sorry No records to display",Toast.LENGTH_SHORT).show();
        }

        bmi_chart.setList(dates,bmi);
        wt_chart.setList(dates,weight);



            wt_chart.set_XY_points(weighMachineArrayList);
            bmi_chart.set_XY_points(weighMachineArrayList);

            wt_chart.requestLayout();
            wt_chart.invalidate();
            bmi_chart.requestLayout();
            bmi_chart.invalidate();



    }


}
