package customviews;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import test.bpl.com.bplscreens.BuildConfig;
import test.bpl.com.bplscreens.R;

/**
 * Created by admin on 24-01-2017.
 */

public class MyCustomDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final Dialog dialog=new Dialog(getActivity());

        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
        dialog.setContentView(R.layout.customdialog_security);
        final TextView content= (TextView) dialog.findViewById(R.id.content);
        final TextView header= (TextView) dialog.findViewById(R.id.header);
        final Button save= (Button) dialog.findViewById(R.id.save);
        final Button cancel= (Button) dialog.findViewById(R.id.cancel);

        header.setText(getResources().getString(R.string.app_version));
        content.setText(new StringBuilder().append("The current app version is ").append(BuildConfig.VERSION_NAME).toString());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        save.setText(getString(R.string.ok));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        return dialog;
    }
}
