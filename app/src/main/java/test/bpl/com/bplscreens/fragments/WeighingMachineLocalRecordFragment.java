package test.bpl.com.bplscreens.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bluetooth.ViewListner;
import constantsP.Constants;
import constantsP.GlobalClass;
import database.DatabaseManager;
import logger.Level;
import logger.Logger;
import model.RecordDetailWeighMachine;
import records.WeighingMachineRecyclerViewAdapter;
import test.bpl.com.bplscreens.R;


public class WeighingMachineLocalRecordFragment extends Fragment {

    RecyclerView recyclerView;

    List<RecordDetailWeighMachine> UserMeasuredWeightList;

    final String TAG=WeighingMachineLocalRecordFragment.class.getSimpleName();
    GlobalClass globalVariable;

    ViewListner viewListner;
    String userSelected;

    public WeighingMachineLocalRecordFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewListner= (ViewListner) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.weighing_machine_local_record_frag,container,false);
         globalVariable = (GlobalClass) getActivity().getApplicationContext();
        recyclerView=view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


      if( getArguments().getString("user")!=null){

          userSelected=getArguments().getString("user");
      }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        if (globalVariable.getUsername() != null) {
           try {
               DatabaseManager.getInstance().openDatabase();

               if(null!=getArguments().getString(Constants.RECORDS_WEIGH_MACHINE))
               {
                   if(getArguments().getString(Constants.RECORDS_WEIGH_MACHINE).
                           equals(Constants.RECORDS_WEIGH_MACHINE1)){
                       UserMeasuredWeightList = new ArrayList<>
                               (DatabaseManager.getInstance().get_measuredWeight(userSelected));

                       WeighingMachineRecyclerViewAdapter recyclerViewAdapter=new
                               WeighingMachineRecyclerViewAdapter(UserMeasuredWeightList,getActivity(),Constants.RECORDS_WEIGH_MACHINE1,viewListner,userSelected);

                       recyclerView.setHasFixedSize(true);
                       recyclerView.setAdapter(recyclerViewAdapter);

                   }else{
                       UserMeasuredWeightList = new ArrayList<>
                               (DatabaseManager.getInstance().get_measuredWeightMachineB(userSelected));


                       WeighingMachineRecyclerViewAdapter recyclerViewAdapter=new
                               WeighingMachineRecyclerViewAdapter(UserMeasuredWeightList,getActivity(),Constants.RECORDS_WEIGH_MACHINE2,viewListner,userSelected);

                       recyclerView.setHasFixedSize(true);
                       recyclerView.setAdapter(recyclerViewAdapter);

                   }
               }


           }catch (Exception e){
               e.printStackTrace();
           }

            finally {
               if (UserMeasuredWeightList.size() > 0) {
                   Logger.log(Level.DEBUG, TAG, "Get weight from database=" + UserMeasuredWeightList.get(0).getWeight());

               }
           }



        }






    }
}
