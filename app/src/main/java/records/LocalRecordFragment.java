package records;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import constantsP.Constants;
import constantsP.Utility;
import logger.Level;
import logger.Logger;
import model.RecordsDetail;
import test.bpl.com.bplscreens.R;



public class LocalRecordFragment extends Fragment {


    RecyclerView recyclerview;
    List<RecordsDetail> recordsDetailList;
    String userName="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.local_record_frag,container,false);
        if(null!=getArguments().getParcelableArrayList(Constants.RECORDS_DETAIL))
        {
            recordsDetailList=getArguments().getParcelableArrayList(Constants.RECORDS_DETAIL);
            Utility.mRecord_detail_list=getArguments().getParcelableArrayList(Constants.RECORDS_DETAIL);


        }


        userName=getArguments().getString(Constants.USER_NAME);

        recyclerview= view.findViewById(R.id.recycler_view);
        Logger.log(Level.INFO,"LOCAL RECORD FRAGMENT"," Utility.mRecord_detail_list.size="+ Utility.mRecord_detail_list.size());
        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(null!=recordsDetailList)
        {
            Logger.log(Level.INFO,"LOCAL RECORD FRAGMENT","recordsDetailList is not null");

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);

        MyRecyclerViewAdapter recyclerViewAdapter=new MyRecyclerViewAdapter(recordsDetailList,getActivity(),userName);
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(recyclerViewAdapter);

    }
}
