package iweigh;

import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.view.*;
import android.widget.*;

import test.bpl.com.bplscreens.*;

public class IweighSettingsScreenActivity extends FragmentActivity {



    ImageView img_back_key;
    ListView items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iweigh_settings);


        final String [] itemsArray={"Weight in kg","Weight in lbs"};
        items= findViewById(R.id.list_item);
        final ArrayAdapter<String> listAdapter=new ArrayAdapter<String>(IweighSettingsScreenActivity.this,
                android.R.layout.simple_list_item_1, itemsArray);

        items.setAdapter(listAdapter);
        final TextView headerTitle= findViewById(R.id.base_header_title);
        headerTitle.setText(getString(R.string.settings));


        img_back_key=  findViewById(R.id.imgBackKey);
        img_back_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if(i==0)
                {
                    Intent intent=new Intent();
                    intent.putExtra("SETTINGS_TYPE","KG");
                    setResult(RESULT_OK,intent);

                }else {
                    Intent intent=new Intent();
                    intent.putExtra("SETTINGS_TYPE","LBS");
                    setResult(RESULT_OK,intent);


                }

                finish();

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
