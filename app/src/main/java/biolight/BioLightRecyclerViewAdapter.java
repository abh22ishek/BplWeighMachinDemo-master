package biolight;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import constantsP.*;
import customviews.*;
import database.*;
import logger.*;
import test.bpl.com.bplscreens.*;


public class BioLightRecyclerViewAdapter extends  RecyclerView.Adapter<BioLightRecyclerViewAdapter.CustomViewHolder>{

    private List<BPMeasurementModel> recordsDetailList;
    private List<BPMeasurementModel> forDayRecordList;
    private Context context;
    private Dialog dialog;
    HomeActivityListner homeActivityListner ;

    private String TAG= BioLightRecyclerViewAdapter.class.getSimpleName();
    String userName,age,gender;

    BioLightListner bioLightListner;

    public BioLightRecyclerViewAdapter(Context context,BioLightListner bioLightListner, List<BPMeasurementModel> recordsDetailList,
                                       HomeActivityListner listner,String name,String age,String gender) {
        this.context = context;
        this.recordsDetailList = recordsDetailList;
        this.homeActivityListner=listner;
        forDayRecordList=new ArrayList<>();
        this.userName=name;
        this.age=age;
        this.gender=gender;
        this.bioLightListner=bioLightListner;

    }



    @Override
    public BioLightRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bp_card_view,parent,
                false);
        BioLightRecyclerViewAdapter.CustomViewHolder viewHolder = new BioLightRecyclerViewAdapter.CustomViewHolder(view);
        Logger.log(Level.INFO,TAG, "recordsDetailList List size="+ recordsDetailList.size());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {


        Logger.log(Level.DEBUG,TAG," sysPressure="+recordsDetailList.get(position).
                getSysPressure());
        Logger.log(Level.DEBUG,TAG," diaPressure="+recordsDetailList.get(position).
                getDiabolicPressure());
        Logger.log(Level.DEBUG,TAG," pulPerMin="+recordsDetailList.get(position).
                getPulsePerMin());
        Logger.log(Level.DEBUG,TAG," testTime="+recordsDetailList.get(position).
                getComments());

        Logger.log(Level.DEBUG,TAG," testTime="+recordsDetailList.get(position).getTypeBP());



        holder.sysPressure.setText(String.valueOf(recordsDetailList.get(position).
                getSysPressure()));
        holder.diaPressure.setText(String.valueOf(recordsDetailList.get(position).
                getDiabolicPressure()));
        holder.pulsePerMin.setText(String.valueOf(recordsDetailList.get(position).
                getPulsePerMin()));
        holder.bpResult.setText(String.valueOf(recordsDetailList.get(position).getTypeBP()));
        holder.comment.setText(recordsDetailList.get(position).getComments());


        holder.testingTime.setText(recordsDetailList.get(position).getMeasurementTime());


        try {
            validateTypeBP((int) recordsDetailList.get(position).getSysPressure(),
                    (int) recordsDetailList.get(position).getDiabolicPressure());

            if(typeBP.equalsIgnoreCase(Constants.DESIRABLE))
            {
                holder.bpIndicator.setInnerColor(Color.CYAN);
            }else if(typeBP.equalsIgnoreCase(Constants.NORMAL))
            {
                holder.bpIndicator.setInnerColor(Color.GREEN);
            }else if(typeBP.equalsIgnoreCase(Constants.PRE_HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.YELLOW);
            }else if(typeBP.equalsIgnoreCase(Constants.HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.RED);
            }else if(typeBP.equalsIgnoreCase(Constants.MILD_HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.RED);
            }else if(typeBP.equalsIgnoreCase(Constants.SEVERE_HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.RED);
            }
            else if(typeBP.equalsIgnoreCase(Constants.MODERATE_HYPERTENSION))
            {
                holder.bpIndicator.setInnerColor(Color.RED);
            }
            else if(typeBP.equalsIgnoreCase(Constants.LOW_))
            {
                holder.bpIndicator.setInnerColor(Color.MAGENTA);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }



    @Override
    public int getItemCount() {
        return (null!=recordsDetailList)?recordsDetailList.size():0;
    }



    private String typeBP="";
    private String validateTypeBP(int systolic, int diabolic)
    {

        if(systolic<90)
        {
            if(diabolic<=60)
            {
                typeBP=Constants.LOW_;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.DESIRABLE;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.NORMAL;
            }
            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }
            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }
        else if(systolic <=120 && systolic>=90)
        {
            typeBP= Constants.DESIRABLE;

            if(diabolic<=60)
            {
                typeBP=Constants.DESIRABLE;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.DESIRABLE;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.NORMAL;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }else if(systolic >120 && systolic <=129)
        {
            typeBP= Constants.NORMAL;
            if(diabolic<=60)
            {
                typeBP=Constants.NORMAL;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.NORMAL;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.NORMAL;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }

        }else if(systolic >=130 && systolic <=139)
        {
            typeBP= Constants.PRE_HYPERTENSION;
            if(diabolic<=60)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.PRE_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }else if(systolic >=140 && systolic <=159)
        {
            typeBP= Constants.MILD_HYPERTENSION;

            if(diabolic<=60)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MILD_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }else if(systolic >=160 && systolic <=179)
        {
            typeBP= Constants.HYPERTENSION;
            if(diabolic<=60)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }else if(diabolic>60 && diabolic <80)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }else if(diabolic >=80 && diabolic <85)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }

            else if(diabolic >=85 && diabolic <90)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }

            else if(diabolic >=90 && diabolic <100)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }

            else if(diabolic >=100 && diabolic <110)
            {
                typeBP=Constants.MODERATE_HYPERTENSION;
            }
            else{
                typeBP=Constants.SEVERE_HYPERTENSION;
            }
        }else if(systolic >=180)
        {

            typeBP= Constants.SEVERE_HYPERTENSION;

        }

        return  typeBP;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView sysPressure;
        private TextView diaPressure;
        private TextView pulsePerMin;
        private ImageView delete,chart,report,share;
        private TextView testingTime;
        private TextView bpResult;
        private CustomViewBPCategory bpIndicator;
        private TextView comment;


        Context ctx;

        private CustomViewHolder(View itemView) {
            super(itemView);

            this.sysPressure=itemView.findViewById(R.id.txtSys);
            this.diaPressure= itemView.findViewById(R.id.txtDI);

            this.pulsePerMin=itemView.findViewById(R.id.txtPulse);
            this.testingTime= itemView.findViewById(R.id.txtTestingTime);
            this.delete= itemView.findViewById(R.id.iconDelete);
            this.bpResult=  itemView.findViewById(R.id.bpResult);
            this.bpIndicator= itemView.findViewById(R.id.bpIndicator);
            this.chart=  itemView.findViewById(R.id.chart);
            this.report=  itemView.findViewById(R.id.report);
            this.comment=  itemView.findViewById(R.id.comment);
            this.share=itemView.findViewById(R.id.iconShare);

            delete.setOnClickListener(this);
            chart.setOnClickListener(this);
            report.setOnClickListener(this);
            share.setOnClickListener(this);

            this.ctx=itemView.getContext();



        }

        @Override
        public void onClick(View v) {

            if(v==delete)
            {

                dialog = new Dialog(context);
                if(dialog.getWindow()!=null)
                {
                    dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.customdialog_security);
                }

                final TextView content= dialog.findViewById(R.id.content);
                final TextView header=  dialog.findViewById(R.id.header);
                final Button save=  dialog.findViewById(R.id.save);
                final Button cancel=  dialog.findViewById(R.id.cancel);

                cancel.setText(context.getResources().getString(R.string.no));
                header.setText(context.getResources().getString(R.string.del_rec));
                content.setText(context.getResources().getString(R.string.delete_rec));

                save.setText(context.getResources().getString(R.string.yes));
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                            DatabaseManager.getInstance().openDatabase();
                            DatabaseManager.getInstance().deleteBioLightBPRecords(Constants.LOGGED_User_ID,
                                   recordsDetailList.get(getAdapterPosition()).getMeasurementTime(),Constants.SELECTED_USER_TYPE);

                            recordsDetailList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());


                            dialog.dismiss();
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.log(Level.INFO,TAG,recordsDetailList.get(getAdapterPosition()).getComments());
                        dialog.dismiss();

                    }
                });
                dialog.show();


            }else if(v==chart){


                Intent intent=new Intent(context,BioLightChartActivity.class);
                final String date= recordsDetailList.get(getAdapterPosition()).
                        getMeasurementTime().substring(0,10);

                Bundle bundle=new Bundle();
                bundle.putSerializable(Constants.CHART, (Serializable) recordsDetailList);
                bundle.putString(Constants.DATE,date);
                intent.putExtra(Constants.USER_NAME,userName);
                intent.putExtra("age",age);
                intent.putExtra("gender",gender);
                intent.putExtras(bundle);

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }

            else if(v==report)
            {
                // Toast.makeText(context,"Coming Soon",Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(context,BioLightReportActivity.class);
                intent.putExtra("data","");
                intent.putExtra(Constants.systolic,sysPressure.getText().toString());
                intent.putExtra(Constants.diabolic,diaPressure.getText().toString());
                intent.putExtra(Constants.pulse,pulsePerMin.getText().toString());
                intent.putExtra(Constants.comment,comment.getText().toString());
                intent.putExtra(Constants.USER_NAME,userName);
                intent.putExtra(Constants.TESTING_TIME_CONSTANTS,testingTime.getText().toString());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }else if(v==share){

                Intent intent=new Intent(context,BioLightReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("data","share");
                intent.putExtra(Constants.systolic,sysPressure.getText().toString());
                intent.putExtra(Constants.diabolic,diaPressure.getText().toString());
                intent.putExtra(Constants.pulse,pulsePerMin.getText().toString());
                intent.putExtra(Constants.comment,comment.getText().toString());
                intent.putExtra(Constants.USER_NAME,userName);
                intent.putExtra(Constants.TESTING_TIME_CONSTANTS,testingTime.getText().toString());

                context.startActivity(intent);
            }

        }
    }



}

