package iweigh;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.widget.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import bluetooth.*;
import canny.*;
import constantsP.*;
import database.*;
import model.*;
import test.bpl.com.bplscreens.*;

public class IweighRecordsActivity extends FragmentActivity  implements ViewListner{


    RecyclerView recyclerView;





    private TextView txt_bmi_chart, txt_wt_chart;
    GlobalClass globalVariable;

    public List<RecordDetailWeighMachine> UserMeasuredWeightList;

    final String TAG = IweighRecordsActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iweigh_record);

        final ImageView back;
        final TextView header_title;
        globalVariable = (GlobalClass) getApplicationContext();
        header_title = findViewById(R.id.base_header_title);

        recyclerView=findViewById(R.id.recycler_view);

        back = findViewById(R.id.imgBackKey);



        txt_bmi_chart = findViewById(R.id.txt_bmi_chart);
        txt_wt_chart = findViewById(R.id.txt_wt_chart);





        final String mUserName = getIntent().getExtras().getString(Constants.USER_NAME);

        int density = this.getResources().getDisplayMetrics().densityDpi;
        if (DisplayMetrics.DENSITY_XXHIGH == density) {
            header_title.setTextSize(25);
        } else {
            header_title.setTextSize(22);
        }
        if (mUserName.length() > 12) {
            String mx = mUserName.substring(0, 10) + "..";
            header_title.setText(new StringBuilder().append(mx).append(" 's ").append(getString(R.string.record)).toString());

        } else {
            header_title.setText(new StringBuilder().append(mUserName).append(" 's").append(getString(R.string.record)).toString());

        }


        if (mUserName != null) {
            DatabaseManager.getInstance().openDatabase();
            UserMeasuredWeightList = new ArrayList<>(DatabaseManager.getInstance().get_measuredWeightMachineB(mUserName));

        }





        if (UserMeasuredWeightList != null && UserMeasuredWeightList.size() >= 1) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(IweighRecordsActivity.this,
                    LinearLayoutManager.VERTICAL, false);

            recyclerView.setLayoutManager(layoutManager);

            CannyRecyclerView recyclerViewAdapter=new
                    CannyRecyclerView(IweighRecordsActivity.this,UserMeasuredWeightList,mUserName);

            recyclerView.setAdapter(recyclerViewAdapter);


        } else {


            Toast.makeText(IweighRecordsActivity.this, "Sorry No records to display", Toast.LENGTH_SHORT).show();
        }




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();



            }
        });


    }



    @Override
    public void updateViewWeighMachine(String TAG) {


    }



}
