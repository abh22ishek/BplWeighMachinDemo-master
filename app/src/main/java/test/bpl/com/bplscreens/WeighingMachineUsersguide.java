package test.bpl.com.bplscreens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class WeighingMachineUsersguide extends Activity {

    private ImageView img_back_key;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_guide_content);

        img_back_key= findViewById(R.id.imgBackKey);
        img_back_key.setOnClickListener(listner);

        final TextView header_title=  findViewById(R.id.base_header_title);
        final TextView user_guide_content= findViewById(R.id.userGuide);



                    user_guide_content.setText(getString(R.string.weigh_machine_manual_1));
                header_title.setText(getApplicationContext().getString(R.string.weigh_mc));






    }


    private View.OnClickListener listner= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v==img_back_key)
            {
                finish();
            }
        }
    };
}
