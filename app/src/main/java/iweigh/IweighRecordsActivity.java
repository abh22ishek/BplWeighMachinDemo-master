package iweigh;

import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import test.bpl.com.bplscreens.*;

public class IweighRecordsActivity extends FragmentActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iweigh_records);

        final TextView base_header_title=findViewById(R.id.base_header_title);
        base_header_title.setText(getString(R.string.record));


        final ImageView  mBackKey=  findViewById(R.id.imgBackKey);
        mBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
