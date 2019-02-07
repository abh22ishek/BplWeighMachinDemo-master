package iweigh;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;

import test.bpl.com.bplscreens.*;

public class BodyFatScreenFragment extends Fragment {
    IweighNavigation iweighNavigation;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iweighNavigation= (IweighNavigation) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.iweigh_body_fat,container,false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        iweighNavigation.navigationPass("I-weigh");
    }
}
