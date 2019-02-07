package records;
import android.app.Dialog;
import android.content.*;
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

import bluetooth.ViewListner;
import constantsP.Constants;
import database.DatabaseManager;
import logger.Level;
import logger.Logger;
import model.RecordDetailWeighMachine;
import test.bpl.com.bplscreens.*;

public class WeighingMachineRecyclerViewAdapter extends RecyclerView.Adapter<WeighingMachineRecyclerViewAdapter.MyViewHolder>{

    private List<RecordDetailWeighMachine> recordsDetailList;
    private Context context;
    private Dialog dialog;
    private String tag;
    ViewListner viewListner;

    String userName;

    public WeighingMachineRecyclerViewAdapter(List<RecordDetailWeighMachine> recordsDetailList, Context context,String tag,ViewListner viewListner,String user) {
        this.recordsDetailList = recordsDetailList;
        this.context = context;
        this.tag=tag;
        this.viewListner=viewListner;
        this.userName=user;

    }

    @Override
    public WeighingMachineRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_detail_wt,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeighingMachineRecyclerViewAdapter.MyViewHolder holder, int position) {

        holder.weight.setText(String.valueOf(recordsDetailList.get(position).getWeight()));
        holder.datetime.setText(recordsDetailList.get(position).getDate());
        holder.bmi.setText(String.valueOf(recordsDetailList.get(position).getBmi()));
    }

    @Override
    public int getItemCount() {
        return (null!=recordsDetailList)? recordsDetailList.size() : 0;
    }

     class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView weight;
        private TextView datetime;
        private TextView bmi;
        private ImageView delete;
        private ImageView report;
        Context ctx;


        private MyViewHolder(View view) {
            super(view);

            this.weight=view.findViewById(R.id.txtWeight);
            this.datetime=  view.findViewById(R.id.txtDateTime);

            this.bmi= view.findViewById(R.id.txtBMI);
            this.delete=  view.findViewById(R.id.imgdelete);
            this.report=view.findViewById(R.id.img_records);

            report.setOnClickListener(this);
            delete.setOnClickListener(this);
            this.ctx=view.getContext();
        }

        @Override
        public void onClick(View view) {
            if(view==delete)
            {

                if(dialog==null)
                {
                    dialog = new Dialog(context);
                }

                if(dialog.getWindow().getAttributes()!=null)
                {
                    dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.customdialog_security);
                    final TextView content= (TextView) dialog.findViewById(R.id.content);
                    final TextView header= (TextView) dialog.findViewById(R.id.header);
                    final Button save= (Button) dialog.findViewById(R.id.save);
                    save.setText(context.getString(R.string.ok));
                    final Button cancel= (Button) dialog.findViewById(R.id.cancel);

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


                            if(Constants.LOGGED_User_ID!="")
                            {
                                try{

                                    if(tag.equals(Constants.RECORDS_WEIGH_MACHINE1)){
                                        DatabaseManager.getInstance().
                                                delete_weighing_machine_records(Constants.LOGGED_User_ID,
                                                        String.valueOf(recordsDetailList.get(getAdapterPosition()).getDate()));

                                        viewListner.updateViewWeighMachine(Constants.RECORDS_WEIGH_MACHINE1);
                                    }else{
                                        DatabaseManager.getInstance().
                                                delete_weighing_machine_records_b(Constants.LOGGED_User_ID,
                                                        String.valueOf(recordsDetailList.get(getAdapterPosition()).getDate()));
                                        viewListner.updateViewWeighMachine(Constants.RECORDS_WEIGH_MACHINE2);
                                    }

                                    recordsDetailList.remove(getAdapterPosition());
                                    notifyItemRemoved(getAdapterPosition());



                                }catch (Exception e)
                                {
                                    e.printStackTrace();
                                    Logger.log(Level.DEBUG,"///****///","Unable to delete records");
                                }
                               finally {

                                    dialog.dismiss();
                                }

                            }


                        }
                    });
                }

                dialog.show();
            }else if(view==report){

                //

                context.startActivity(new Intent(context, WeightMachine2ReportActivity.class).
                        putExtra(Constants.WEIGHT,weight.getText().toString()).
                        putExtra(Constants.BMI,bmi.getText().toString()).
                        putExtra(Constants.TESTING_TIME_CONSTANTS,datetime.getText().toString()).
                        putExtra(Constants.USER_NAME,userName).
                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }



}
