package test.bpl.com.bplscreens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class SettingsWeighingMachineActivity extends FragmentActivity implements View.OnClickListener {


    ListView items;
    private ImageView img_back_key;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_wt);

        final String [] itemsArray={"Weight in kg","Weight in lbs"};
        items= findViewById(R.id.list_item);
        final ArrayAdapter<String> listAdapter=new ArrayAdapter<String>(SettingsWeighingMachineActivity.this,
                android.R.layout.simple_list_item_1, itemsArray);

        items.setAdapter(listAdapter);
       final TextView headerTitle= findViewById(R.id.base_header_title);
        headerTitle.setText(getString(R.string.settings));


        img_back_key=  findViewById(R.id.imgBackKey);
        img_back_key.setOnClickListener(this);


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

    @Override
    public void onClick(View view) {
        if(view==img_back_key)
        {
            finish();
        }
    }
}
