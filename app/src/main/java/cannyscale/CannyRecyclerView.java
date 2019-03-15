package cannyscale;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import biolight.*;
import constantsP.*;
import customviews.*;
import database.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.*;


public class CannyRecyclerView extends  RecyclerView.Adapter<CannyRecyclerView.CustomViewHolder>{

    private List<RecordDetailWeighMachine> recordsDetailList;

    private Context context;
    private Dialog dialog;


    private String TAG= biolight.BioLightRecyclerViewAdapter.class.getSimpleName();


    public CannyRecyclerView(Context context, List<RecordDetailWeighMachine> recordsDetailList) {
        this.context = context;
        this.recordsDetailList = recordsDetailList;


    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.iweigh_card_bg,viewGroup, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        Logger.log(Level.INFO,TAG, "recordsDetailList List size="+ recordsDetailList.size());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {

        customViewHolder.weight.setText(String.valueOf(recordsDetailList.get(position).getWeight()));
        customViewHolder.bmi.setText(String.valueOf(recordsDetailList.get(position).getBmi()));
        customViewHolder.testingTime.setText(String.valueOf(recordsDetailList.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return (null!=recordsDetailList)?recordsDetailList.size():0;
    }







    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView bmi;
        private TextView weight;
        private TextView testingTime;




        Context ctx;

        private CustomViewHolder(View itemView) {
            super(itemView);

            this.bmi=itemView.findViewById(R.id.bmi);
            this.weight= itemView.findViewById(R.id.wt);

            this.testingTime=itemView.findViewById(R.id.txtTestingTime);





            this.ctx=itemView.getContext();



        }

        @Override
        public void onClick(View v) {




        }
    }



}

