package records;

import android.annotation.*;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import constantsP.Constants;
import database.DatabaseManager;
import logger.Level;
import logger.Logger;
import model.RecordsDetail;
import test.bpl.com.bplscreens.R;
import test.bpl.com.bplscreens.UserTestReportActivity;
import test.bpl.com.bplscreens.UserTestReportGraphActivity;

import static android.content.ContentValues.TAG;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {

    List<RecordsDetail> recordsDetailList;
    Context context;
    Dialog dialog;


    String userName;




    public MyRecyclerViewAdapter(List<RecordsDetail> recordsDetailList, Context context,
                                 String userName) {
        this.recordsDetailList = recordsDetailList;
        this.context = context;
        this.userName=userName;


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.records_detail,parent,false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        DatabaseManager.getInstance().openDatabase();
        Logger.log(Level.INFO, MyRecyclerViewAdapter.this.getClass().getSimpleName(),"recordsDetailList List size="+recordsDetailList.size());
        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

       // holder.spo2.setText(recordsDetailList.get(position).getSpo2().toString());
        int avg_spo2=0;
        int avg_hrr=0;
        float avg_pi=0;


        int avg_spo2_res=0;
        int avg_hrr_res=0;
        float avg_pi_res=0;

        String []spo2_prod=recordsDetailList.get(position).getSpo2().toString().split(",");
        for(String s:spo2_prod)
        {
            avg_spo2=avg_spo2+Integer.parseInt(s);
        }

        String [] pr_prod=recordsDetailList.get(position).getHeartrate().toString().split(",");
        for(String s:pr_prod)
        {
            avg_hrr=avg_hrr+Integer.parseInt(s);
        }

        String [] pi_prod=recordsDetailList.get(position).getPI().toString().split(",");


        float[] parsed_pi = new float[pi_prod.length];
        for (int i = 0; i<parsed_pi.length; i++)
        {
           parsed_pi[i] = Float.parseFloat((pi_prod[i]));
           avg_pi=avg_pi+parsed_pi[i];
        }

        Logger.log(Level.DEBUG,TAG," avg_spo2="+avg_spo2);
        Logger.log(Level.DEBUG,TAG," avg_hrr="+avg_hrr);
        Logger.log(Level.DEBUG,TAG," avg_pi="+avg_pi);


        avg_spo2_res= avg_spo2/spo2_prod.length;
        avg_hrr_res=avg_hrr/pr_prod.length;
        avg_pi_res= avg_pi/pi_prod.length;


        Logger.log(Level.DEBUG,TAG," spo2="+recordsDetailList.get(position).getSpo2().toString()+"length="+spo2_prod.length);
        Logger.log(Level.DEBUG,TAG," heart_rate="+recordsDetailList.get(position).getHeartrate().toString()+"length="+spo2_prod.length);
        Logger.log(Level.DEBUG,TAG," Pi="+recordsDetailList.get(position).getPI().toString()+
                "length="+spo2_prod.length);

        holder.spo2.setText(new StringBuilder().append(avg_spo2_res).toString());
        holder.heratrate.setText(new StringBuilder().append(avg_hrr_res).toString());

        holder.PI.setText(String.format("%.1f",avg_pi_res));
        holder.datetime.setText(recordsDetailList.get(position).getTesting_time());



    }

    @Override
    public int getItemCount() {
        return (null!=recordsDetailList)?recordsDetailList.size():0;
    }


    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private TextView spo2;
        private TextView datetime;
        private TextView heratrate;
        private ImageView delete,test_rport,graph_chart;
        private TextView PI;
        Context ctx;

        public CustomViewHolder(View view) {
            super(view);

            this.spo2=view.findViewById(R.id.txtSpo2);
            this.datetime=  view.findViewById(R.id.txtDateTime);

            this.heratrate=view.findViewById(R.id.txtHeartRate);
            this.PI= view.findViewById(R.id.txtPI);
            this.delete=  view.findViewById(R.id.imgdelete);
            this.test_rport=  view.findViewById(R.id.imgreport);
            this.graph_chart= view.findViewById(R.id.imgchart);

            delete.setOnClickListener(this);
            test_rport.setOnClickListener(this);
            graph_chart.setOnClickListener(this);
            this.ctx=view.getContext();

        }

        @Override
        public void onClick(View v) {

            if(v==delete)
            {

                if(dialog==null)
                {
                    dialog = new Dialog(context);
                }

                dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setContentView(R.layout.customdialog_security);

                final TextView content=  dialog.findViewById(R.id.content);
                final TextView header=  dialog.findViewById(R.id.header);
                final Button save=  dialog.findViewById(R.id.save);
                save.setText(context.getString(R.string.ok));
                final Button cancel= dialog.findViewById(R.id.cancel);

                cancel.setText("No");
                header.setText("Delete Record");
                content.setText(context.getResources().getString(R.string.delete_rec));

                save.setText("Yes");
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Logger.log(Level.INFO, MyRecyclerViewAdapter.this.getClass().getSimpleName(),recordsDetailList.get(getAdapterPosition()).getTesting_time());
                        if(Constants.LOGGED_User_ID!="")
                        {
                            DatabaseManager.getInstance().delete_records(Constants.LOGGED_User_ID, recordsDetailList.get(getAdapterPosition()).getTesting_time());
                            recordsDetailList.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();

            }else if(v==test_rport)
            {
                List<RecordsDetail> mRecorddetaillist;
                if(Constants.LOGGED_User_ID!="")
                {
                    Logger.log(Level.INFO, MyRecyclerViewAdapter.this.getClass().getSimpleName(),recordsDetailList.get(getAdapterPosition()).getTesting_time());
                    mRecorddetaillist=DatabaseManager.getInstance().get_Records_Test_Report(Constants.LOGGED_User_ID,recordsDetailList.get(getAdapterPosition()).getTesting_time());
                    context.startActivity(new Intent(context, UserTestReportActivity.class).
                                    putExtra(Constants.SPO2_CONSTANTS,mRecorddetaillist.get(0).getSpo2().toString()).
                            putExtra(Constants.PR_CONSTANTS,mRecorddetaillist.get(0).getHeartrate().toString()).
                            putExtra(Constants.PI_CONSTATNTS,mRecorddetaillist.get(0).getPI().toString())
                    . putExtra(Constants.TESTING_TIME_CONSTANTS,mRecorddetaillist.get(0).getTesting_time().toString())
                    . putExtra(Constants.DURATION_CONSTANTS,mRecorddetaillist.get(0).getDuration().toString())
                            .putExtra(Constants.DEVICE_MACID,mRecorddetaillist.get(0).getDevice_mac_id().toString()).
                                    putExtra(Constants.USER_NAME,userName).
                                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));



                            //putParcelableArrayListExtra(ConstantsP.RECORDS_DETAIL, (ArrayList<? extends Parcelable>) mRecorddetaillist));

                }

            }else if(v==graph_chart)
            {
                List<RecordsDetail> mRecorddetaillist;
                if(Constants.LOGGED_User_ID!="") {
                    mRecorddetaillist=DatabaseManager.getInstance().get_Records_Test_Report(Constants.LOGGED_User_ID,recordsDetailList.get(getAdapterPosition()).getTesting_time());
                    context.startActivity(new Intent(context, UserTestReportGraphActivity.class).
                            putExtra(Constants.SPO2_CONSTANTS, mRecorddetaillist.get(0).getSpo2()).
                            putExtra(Constants.PR_CONSTANTS, mRecorddetaillist.get(0).getHeartrate()).
                            putExtra(Constants.TESTING_TIME_CONSTANTS, mRecorddetaillist.get(0).getTesting_time())
                            .putExtra(Constants.MONTH, mRecorddetaillist.get(0).getTesting_time().substring(3, 5))
                            .putExtra(Constants.YEAR, mRecorddetaillist.get(0).getTesting_time().substring(6,10))
                            .putExtra(Constants.DURATION_CONSTANTS,mRecorddetaillist.get(0).getDuration())
                           .putExtra(Constants.USER_NAME,userName)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }

        }
    }





}

