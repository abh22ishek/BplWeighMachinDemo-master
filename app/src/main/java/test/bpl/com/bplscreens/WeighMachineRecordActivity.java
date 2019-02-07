package test.bpl.com.bplscreens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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


public class WeighMachineRecordActivity extends FragmentActivity implements ViewListner,View.OnClickListener {

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
        header_title.setText(getString(R.string.records_weigh_a));

        back=  findViewById(R.id.imgBackKey);

        btn_weight= findViewById(R.id.btn_weight_chart);
        btn_bmi=  findViewById(R.id.btn_bmi_chart);


        txt_bmi_chart= findViewById(R.id.txt_bmi_chart);
        txt_wt_chart=  findViewById(R.id.txt_wt_chart);

        btn_weight.setOnClickListener(this);
        btn_bmi.setOnClickListener(this);

        dates=new ArrayList<>();
        weight=new ArrayList<>();
        bmi=new ArrayList<>();


        if (globalVariable.getUsername() != null) {
            DatabaseManager.getInstance().openDatabase();
            UserMeasuredWeightList = new ArrayList<>(DatabaseManager.getInstance().
                    get_measuredWeight(globalVariable.getUsername()));

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
            Toast.makeText(WeighMachineRecordActivity.this,"Sorry No records to display",Toast.LENGTH_SHORT).show();
        }

        bmi_chart.setList(dates,bmi);
        wt_chart.setList(dates,weight);

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        WeighingMachineLocalRecordFragment fragment = new WeighingMachineLocalRecordFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Constants.RECORDS_WEIGH_MACHINE, Constants.RECORDS_WEIGH_MACHINE1);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragment_container,fragment);
      //  fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

               /* android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                Bundle bundle_args=new Bundle();
                bundle_args.putStringArrayList("DATE", (ArrayList<String>) dates);
                android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                WeighingMachinePlotChartFragment wt_machine_chart_fragment = new WeighingMachinePlotChartFragment();
                fragmentTransaction.replace(R.id.fragment_container,wt_machine_chart_fragment);
                wt_machine_chart_fragment.setArguments(bundle_args);
               // fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
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

        if(TAG.equalsIgnoreCase(Constants.RECORDS_WEIGH_MACHINE2))
            return;


        List<RecordDetailWeighMachine> weighMachineArrayList = null;
        List<String> dates=new ArrayList<>();
        List<String> weight=new ArrayList<>();
        List<String> bmi=new ArrayList<>();

        if (globalVariable.getUsername() != null) {
            DatabaseManager.getInstance().openDatabase();
            weighMachineArrayList = new ArrayList<>(DatabaseManager.getInstance().get_measuredWeight(globalVariable.getUsername()));

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
            Toast.makeText(WeighMachineRecordActivity.this,"Sorry No records to display",Toast.LENGTH_SHORT).show();
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
