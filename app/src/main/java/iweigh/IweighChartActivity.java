package iweigh;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import constantsP.*;
import customviews.*;
import database.*;
import model.*;
import test.bpl.com.bplscreens.*;

public class IweighChartActivity extends FragmentActivity {

    public List<String> dates;
    public  List<String> weight;
    public  List<String> bmi;

    private Button btn_weight,btn_bmi;

    WeightChartView wt_chart;
    BmiChartView bmi_chart;

    private TextView  txt_bmi_chart,txt_wt_chart;
    GlobalClass globalVariable;

    public  List<RecordDetailWeighMachine> UserMeasuredWeightList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iweigh_chart);

        final ImageView back;
        final TextView header_title;
        globalVariable = (GlobalClass) getApplicationContext();
        header_title= findViewById(R.id.base_header_title);



        back=  findViewById(R.id.imgBackKey);

        btn_weight= findViewById(R.id.btn_weight_chart);
        btn_bmi=  findViewById(R.id.btn_bmi_chart);


        txt_bmi_chart=  findViewById(R.id.txt_bmi_chart);
        txt_wt_chart=  findViewById(R.id.txt_wt_chart);

        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_bmi.setAlpha(0.8f);
                btn_weight.setAlpha(1f);
                bmi_chart.setVisibility(View.GONE);
                wt_chart.setVisibility(View.VISIBLE);
                txt_bmi_chart.setVisibility(View.GONE);
                txt_wt_chart.setVisibility(View.VISIBLE);
            }
        });


        btn_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_weight.setAlpha(0.8f);
                btn_bmi.setAlpha(1f);
                bmi_chart.setVisibility(View.VISIBLE);
                wt_chart.setVisibility(View.GONE);
                txt_wt_chart.setVisibility(View.GONE);
                txt_bmi_chart.setVisibility(View.VISIBLE);
            }
        });

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
            Toast.makeText(IweighChartActivity.this,"Sorry No records to display",Toast.LENGTH_SHORT).show();
        }

        bmi_chart.setList(dates,bmi);
        wt_chart.setList(dates,weight);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


            }
        });

    }
}
