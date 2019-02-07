package test.bpl.com.bplscreens.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import customviews.WeightChartView;
import logger.Level;
import logger.Logger;
import test.bpl.com.bplscreens.R;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class WeighingMachinePlotChartFragment extends Fragment{

    WeightChartView weight_chart;

    public WeighingMachinePlotChartFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.wt_chart,container,false);
        weight_chart= (WeightChartView) view.findViewById(R.id.weight_chart_);

        if(getArguments().getStringArrayList("DATES")!=null)
        {
            Logger.log(Level.DEBUG,TAG,"Get arguments");
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }





}
