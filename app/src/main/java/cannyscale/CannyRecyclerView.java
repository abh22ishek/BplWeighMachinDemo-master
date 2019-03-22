package cannyscale;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.support.annotation.*;
import android.support.v7.widget.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import constantsP.*;
import database.*;
import iweigh.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.*;


public class CannyRecyclerView extends  RecyclerView.Adapter<CannyRecyclerView.CustomViewHolder>{

    private List<RecordDetailWeighMachine> recordsDetailList;

    private Context context;
    private Dialog dialog;


    private String TAG= biolight.BioLightRecyclerViewAdapter.class.getSimpleName();

    private String mUserName;


    public CannyRecyclerView(Context context, List<RecordDetailWeighMachine> recordsDetailList,String mUserName) {
        this.context = context;
        this.recordsDetailList = recordsDetailList;
        this.mUserName=mUserName;


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


        private ImageView report,share,delete,chart;


        Context ctx;

        private CustomViewHolder(View itemView) {
            super(itemView);

            this.bmi=itemView.findViewById(R.id.bmi);
            this.weight= itemView.findViewById(R.id.wt);

            this.testingTime=itemView.findViewById(R.id.txtTestingTime);
            this.delete= itemView.findViewById(R.id.iconDelete);

            this.report=itemView.findViewById(R.id.report);

            this.share=itemView.findViewById(R.id.iconShare);
            this.chart=itemView.findViewById(R.id.chart);

            this.ctx=itemView.getContext();

            report.setOnClickListener(this);
            share.setOnClickListener(this);
            delete.setOnClickListener(this);
            chart.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {

            if(v==report){

                Intent intent=new Intent(context, IweighReportActivity.class);
                intent.putExtra(Constants.USER_NAME,mUserName);
                intent.putExtra("data","");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            else if(v==share){
                Intent intent=new Intent(context,IweighReportActivity.class);
                intent.putExtra(Constants.USER_NAME,mUserName);
                intent.putExtra("data","share");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }


            else if(v==delete){
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


//                        DatabaseManager.getInstance().openDatabase();
//                        DatabaseManager.getInstance().deleteBioLightBPRecords(Constants.LOGGED_User_ID,
//                                recordsDetailList.get(getAdapterPosition()).getMeasurementTime(),Constants.SELECTED_USER_TYPE);
//
//                        recordsDetailList.remove(getAdapterPosition());
//                        notifyItemRemoved(getAdapterPosition());


                        dialog.dismiss();
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //  Logger.log(Level.INFO,TAG,recordsDetailList.get(getAdapterPosition()).getComments());
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }

            else if(v==chart){
                Intent intent=new Intent(context,IweighChartActivity.class);
                intent.putExtra(Constants.USER_NAME,mUserName);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }

        }
    }



}

