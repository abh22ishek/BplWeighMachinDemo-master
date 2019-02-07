package biolight;

import android.app.*;
import android.os.*;
import android.support.annotation.*;
import android.view.*;
import android.widget.*;

import test.bpl.com.bplscreens.*;

public class BioLightUserHelpActivity extends Activity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bio_user_guide);

        final TextView headerText=findViewById(R.id.base_header_title);
        headerText.setText(new StringBuilder().append(getString(R.string.biolight)).append(" ").
                append(getString(R.string.user_guide)).toString());

        final ImageView backKey=findViewById(R.id.imgBackKey);
        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
