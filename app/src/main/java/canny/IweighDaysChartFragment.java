package canny;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import constantsP.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.*;

import static com.tom_roush.fontbox.ttf.OS2WindowsMetricsTable.*;

public class IweighDaysChartFragment extends Fragment {


    public List<String> dates;
    public  List<String> weight;
    public List<String> metabolism;
    public  List<String> bmi;
    public  List<String> bodyWater;
    public  List<String> visceralFat;

    public  List<String> muscleMass;
    public  List<String> bodyFat;
    public  List<String> boneMass;


    private Button btn_weight,btn_bmi,btn_metabolism,btn_bdy_water,btn_mus_mass,btn_bdy_fat,btn_bone_mass,btn_visc_fat;

    IWeighChartView wt_chart;
    IweighBmiView bmi_chart;

    IweighMetabolismView metabolismView;
    IweighBodyWaterView iweighBodyWaterView;

    IweighVisceralFatView iweighVisceralFatView;
    IweighMuscleMasView iweighMuscleMasView;
    IweighBodyFatView iweighBodyFatView;

    IweighBoneMassView iweighBoneMassView;

    LinearLayout chart;
    private TextView  txt_bmi_chart,txt_wt_chart;
    GlobalClass globalVariable;

    public  List<RecordDetailWeighMachine> UserMeasuredWeightList;

    LinearLayout layoutWeight,layoutBmi;

    private TextView DateTime;
    private String selectedDate;
    RelativeLayout iweigh_meta_chart,viscfat_chart,musc_mass_chart,water_chart,body_fat_chart,bone_mass_chart;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.iweigh_day_chart,container,false);
        btn_weight= view.findViewById(R.id.btn_weight_chart);
        btn_bmi=  view.findViewById(R.id.btn_bmi_chart);

        iweigh_meta_chart = view.findViewById(R.id.meta_chart);
        wt_chart= view.findViewById(R.id.weight_chart_);
       bmi_chart= view. findViewById(R.id.bmi_chart_);
        metabolismView=  view.findViewById(R.id.metabolism_chart_);

        iweighBodyWaterView=view.findViewById(R.id.iweighWaterView);
        iweighVisceralFatView=view.findViewById(R.id.visceralFat_chart_);

        iweighMuscleMasView=view.findViewById(R.id.iweighMuscleMassView);
        iweighBodyFatView=view.findViewById(R.id.iweighBodyFatView);

        iweighBoneMassView=view.findViewById(R.id.iweighBoneMassView);

        txt_bmi_chart=  view.findViewById(R.id.txt_bmi_chart);
        txt_wt_chart= view. findViewById(R.id.txt_wt_chart);
        layoutWeight=view.findViewById(R.id.layoutWeight);
        layoutBmi=view.findViewById(R.id.layoutBmi);
        DateTime=view.findViewById(R.id.DateTime);
        btn_metabolism=view.findViewById(R.id.metabolism_chart);

        viscfat_chart=view.findViewById(R.id.viscfat_chart);
        musc_mass_chart=view.findViewById(R.id.musc_mass_chart);

        water_chart=view.findViewById(R.id.water_chart);

        body_fat_chart=view.findViewById(R.id.body_fat_chart);
        chart=view.findViewById(R.id.chart);

        btn_visc_fat=view.findViewById(R.id.btn_vfat_chart);

        btn_bdy_water=view.findViewById(R.id.btn_bdy_water_chart);
        btn_mus_mass=view.findViewById(R.id.btn_muscle_chart);
        btn_bdy_fat=view.findViewById(R.id.btn_bdyFat_chart);
        btn_bone_mass=view.findViewById(R.id.btn_bone_mass_chart);

        bone_mass_chart=view.findViewById(R.id.bone_mass_chart);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        iweigh_meta_chart.setVisibility(View.GONE);
        musc_mass_chart.setVisibility(View.GONE);
        viscfat_chart.setVisibility(View.GONE);
        body_fat_chart.setVisibility(View.GONE);
        musc_mass_chart.setVisibility(View.GONE);
        water_chart.setVisibility(View.GONE);
        bone_mass_chart.setVisibility(View.GONE);

        if(getArguments().getString(Constants.DATE)!=null){
            selectedDate=getArguments().getString(Constants.DATE);
        }

        btn_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_bmi.setAlpha(0.8f);
                btn_weight.setAlpha(1f);

                chart.setVisibility(View.VISIBLE);
                layoutBmi.setVisibility(View.GONE);
                layoutWeight.setVisibility(View.VISIBLE);

                txt_wt_chart.setVisibility(View.VISIBLE);
                txt_bmi_chart.setVisibility(View.GONE);
                iweigh_meta_chart.setVisibility(View.GONE);
                viscfat_chart.setVisibility(View.GONE);

                water_chart.setVisibility(View.GONE);
                musc_mass_chart.setVisibility(View.GONE);
                body_fat_chart.setVisibility(View.GONE);
                bone_mass_chart.setVisibility(View.GONE);
            }
        });


        btn_bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_weight.setAlpha(0.8f);
                btn_bmi.setAlpha(1f);
                chart.setVisibility(View.VISIBLE);


                layoutBmi.setVisibility(View.VISIBLE);
                layoutWeight.setVisibility(View.GONE);

                txt_bmi_chart.setVisibility(View.VISIBLE);
                txt_wt_chart.setVisibility(View.GONE);
                iweigh_meta_chart.setVisibility(View.GONE);
                viscfat_chart.setVisibility(View.GONE);
                water_chart.setVisibility(View.GONE);
                musc_mass_chart.setVisibility(View.GONE);
                body_fat_chart.setVisibility(View.GONE);
                bone_mass_chart.setVisibility(View.GONE);


            }
        });


        btn_metabolism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                iweigh_meta_chart.setVisibility(View.VISIBLE);
                chart.setVisibility(View.GONE);
                viscfat_chart.setVisibility(View.GONE);
                water_chart.setVisibility(View.GONE);
                musc_mass_chart.setVisibility(View.GONE);
                body_fat_chart.setVisibility(View.GONE);
                bone_mass_chart.setVisibility(View.GONE);
            }
        });


        btn_visc_fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.GONE);
                viscfat_chart.setVisibility(View.VISIBLE);
                iweigh_meta_chart.setVisibility(View.GONE);
                water_chart.setVisibility(View.GONE);
                musc_mass_chart.setVisibility(View.GONE);
                body_fat_chart.setVisibility(View.GONE);
                bone_mass_chart.setVisibility(View.GONE);
            }
        });

        btn_mus_mass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.GONE);
                viscfat_chart.setVisibility(View.GONE);
                iweigh_meta_chart.setVisibility(View.GONE);
                musc_mass_chart.setVisibility(View.VISIBLE);
                water_chart.setVisibility(View.GONE);
                body_fat_chart.setVisibility(View.GONE);
                bone_mass_chart.setVisibility(View.GONE);
            }
        });


        btn_bdy_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.GONE);
                viscfat_chart.setVisibility(View.VISIBLE);
                iweigh_meta_chart.setVisibility(View.GONE);
                musc_mass_chart.setVisibility(View.GONE);
                water_chart.setVisibility(View.VISIBLE);
                body_fat_chart.setVisibility(View.GONE);
                bone_mass_chart.setVisibility(View.GONE);
            }
        });


        btn_bdy_fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chart.setVisibility(View.GONE);
                viscfat_chart.setVisibility(View.GONE);
                iweigh_meta_chart.setVisibility(View.GONE);
                musc_mass_chart.setVisibility(View.GONE);
                water_chart.setVisibility(View.GONE);
                body_fat_chart.setVisibility(View.VISIBLE);
                bone_mass_chart.setVisibility(View.GONE);
            }
        });


        btn_bone_mass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bone_mass_chart.setVisibility(View.VISIBLE);
                body_fat_chart.setVisibility(View.GONE);
                chart.setVisibility(View.GONE);
                viscfat_chart.setVisibility(View.GONE);
                iweigh_meta_chart.setVisibility(View.GONE);
                musc_mass_chart.setVisibility(View.GONE);
                water_chart.setVisibility(View.GONE);
                body_fat_chart.setVisibility(View.GONE);
            }
        });

        dates=new ArrayList<>();
        weight=new ArrayList<>();
        bmi=new ArrayList<>();
        metabolism=new ArrayList<>();
        bodyWater=new ArrayList<>();


        visceralFat=new ArrayList<>();
        muscleMass=new ArrayList<>();
        bodyFat=new ArrayList<>();
        boneMass=new ArrayList<>();


        final String mUserName=getActivity().getIntent().getExtras().getString(Constants.USER_NAME);

        int density =this.getResources().getDisplayMetrics().densityDpi;

        if(getActivity().getIntent().getExtras().getSerializable(Constants.CHART)!=null){
            UserMeasuredWeightList= (List<RecordDetailWeighMachine>) getActivity().
                    getIntent().getExtras().getSerializable(Constants.CHART);
            Logger.log(Level.WARNING,TAG,"Get serializable list="+UserMeasuredWeightList.size());
        }



      /*  if (mUserName!= null) {
            DatabaseManager.getInstance().openDatabase();
            UserMeasuredWeightList = new ArrayList<>(DatabaseManager.getInstance().get_measuredWeightMachineB(mUserName));

        }*/




        wt_chart.set_XY_points(sameDateRecords(UserMeasuredWeightList,selectedDate));
       bmi_chart.set_XY_points(sameDateRecords(UserMeasuredWeightList,selectedDate));

       metabolismView.set_XY_points(sameDateRecords(UserMeasuredWeightList,selectedDate));
       iweighBodyWaterView.set_XY_points(sameDateRecords(UserMeasuredWeightList,selectedDate));


        iweighVisceralFatView.set_XY_points(sameDateRecords(UserMeasuredWeightList,selectedDate));

        iweighMuscleMasView.set_XY_points(sameDateRecords(UserMeasuredWeightList,selectedDate));
      iweighBodyFatView.set_XY_points(sameDateRecords(UserMeasuredWeightList,selectedDate));
      iweighBoneMassView.set_XY_points(sameDateRecords(UserMeasuredWeightList,selectedDate));


        if(UserMeasuredWeightList!=null && UserMeasuredWeightList.size()>=1)
        {
            for(RecordDetailWeighMachine r:UserMeasuredWeightList)
            {
                dates.add(r.getDate());
                weight.add(String.valueOf(r.getWeight()));
                bmi.add(String.valueOf(r.getBmi()));
                metabolism.add(String.valueOf(r.getMetabolism()));
                bodyWater.add(String.valueOf(r.getBodyWater()));
                visceralFat.add(String.valueOf(r.getVisceralFat()));
                muscleMass.add(String.valueOf(r.getMuscleMass()));
                bodyFat.add(String.valueOf(r.getBodyFat()));
                boneMass.add(String.valueOf(r.getBoneMass()));

            }

            btn_bmi.setAlpha(0.8f);
            layoutBmi.setVisibility(View.GONE);
            layoutWeight.setVisibility(View.VISIBLE);
            txt_bmi_chart.setVisibility(View.GONE);
            btn_weight.setAlpha(1f);

        }else{

           layoutBmi.setVisibility(View.GONE);
            layoutWeight.setVisibility(View.GONE);
            Toast.makeText(getActivity(),"Sorry No records to display",Toast.LENGTH_SHORT).show();
        }

       bmi_chart.setList(dates,bmi);
        wt_chart.setList(dates,weight);
        metabolismView.setList(dates,metabolism);
        iweighBodyWaterView.setList(dates,bodyWater);
        iweighVisceralFatView.setList(dates,visceralFat);
        iweighMuscleMasView.setList(dates,muscleMass);
        iweighBodyFatView.setList(dates,bodyFat);
        iweighBoneMassView.setList(dates,boneMass);

        DateTime.setText(new StringBuilder().append("SELECTED DATE : ").append(selectedDate));
    }




    private List<RecordDetailWeighMachine> sameDateRecords(List<RecordDetailWeighMachine> mRecordDetailList,String selectedDate)
    {
        final List<RecordDetailWeighMachine> listToReturn = new ArrayList<>();


        for(RecordDetailWeighMachine b:mRecordDetailList){
            if(selectedDate.equals(b.getDate().substring(0,10))){
                listToReturn.add(b);
            }
        }

        Logger.log(Level.WARNING,TAG,"****Record with same date****"+listToReturn.size());
        return listToReturn;
    }
}
