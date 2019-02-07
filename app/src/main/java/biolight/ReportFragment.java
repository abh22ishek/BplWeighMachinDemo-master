package biolight;


import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import constantsP.*;
import test.bpl.com.bplscreens.*;


public class ReportFragment extends Fragment{

    TextView sysPressure,diaPressure,pulseRateText,comment;
    private CustomBPChart customBPChart;

    HomeActivityListner homeActivityListner;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        homeActivityListner= (HomeActivityListner) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.report_frag,container,false);
        sysPressure=  view.findViewById(R.id.sysPressure);
        diaPressure= view.findViewById(R.id.diaPressure);
        pulseRateText= view.findViewById(R.id.pulseRate);
        comment=  view.findViewById(R.id.comment);
        customBPChart= view.findViewById(R.id.customBPView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        homeActivityListner.onDataPass(ReportFragment.class.getName());

        if(getArguments().getString(Constants.systolic)!=null){
            sysPressure.setText(getArguments().getString(Constants.systolic));
        }

        if(getArguments().getString(Constants.diabolic)!=null){
            diaPressure.setText(getArguments().getString(Constants.diabolic));
        }


        if(getArguments().getString(Constants.pulse)!=null){
            pulseRateText.setText(getArguments().getString(Constants.pulse));
        }

        if(getArguments().getString(Constants.comment)!=null){
            comment.setText(new StringBuilder().append("Comment :").
                    append(getArguments().getString(Constants.comment)).toString());
        }


        float sysF = Float.parseFloat(getArguments().getString(Constants.systolic));
        final int pointX=(int) sysF;

        float diaF = Float.parseFloat(getArguments().getString(Constants.diabolic));
        final int pointY=(int) diaF;


        customBPChart.set_XY_points(pointY,pointX);
        customBPChart.invalidate();
    }





}
