package cannyscale;

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
    public  List<String> bmi;

    private Button btn_weight,btn_bmi;

    IWeighChartView wt_chart;
    IweighBmiView bmi_chart;

    private TextView  txt_bmi_chart,txt_wt_chart;
    GlobalClass globalVariable;

    public  List<RecordDetailWeighMachine> UserMeasuredWeightList;

    LinearLayout layoutWeight,layoutBmi;

    private TextView DateTime;
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


        wt_chart= view.findViewById(R.id.weight_chart_);
       bmi_chart= view. findViewById(R.id.bmi_chart_);

        txt_bmi_chart=  view.findViewById(R.id.txt_bmi_chart);
        txt_wt_chart= view. findViewById(R.id.txt_wt_chart);
        layoutWeight=view.findViewById(R.id.layoutWeight);
        layoutBmi=view.findViewById(R.id.layoutBmi);
        DateTime=view.findViewById(R.id.DateTime);
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

        dates=new ArrayList<>();
        weight=new ArrayList<>();
        bmi=new ArrayList<>();


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

        DateTime.setText(new StringBuilder().append("SELECTED DATE : ").
                append(dates.get(0).substring(0, 10)).toString());
    }
}
