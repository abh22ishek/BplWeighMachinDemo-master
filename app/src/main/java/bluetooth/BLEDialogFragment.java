package bluetooth;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test.bpl.com.bplscreens.R;



public class BLEDialogFragment extends android.support.v4.app.DialogFragment{

    TextView content;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.customdialog_security,container);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        content=  view.findViewById(R.id.content);
        content.setText("Please connect to Bluetooth");
    }


}
