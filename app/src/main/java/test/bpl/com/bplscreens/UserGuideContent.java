package test.bpl.com.bplscreens;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class UserGuideContent extends Activity {

    private ImageView img_back_key;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_guide_content);

        img_back_key= findViewById(R.id.imgBackKey);
        img_back_key.setOnClickListener(listner);

       final TextView header_title= findViewById(R.id.base_header_title);
        header_title.setText(getApplicationContext().getString(R.string.user_guide));

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
