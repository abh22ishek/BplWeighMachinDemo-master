package test.bpl.com.bplscreens;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.text.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import adapter.*;
import constantsP.*;
import database.*;
import logger.*;
import model.*;

public class ExistingUserActivity extends Activity implements ListR{

    private EditText autoCompleteTextView;
    ListView recyclerView;
    List<UserModel> userModelList_;
    List<UserModel> userModelList_home;
    private ArrayAdapter<CharSequence> adapter;
    private GlobalClass globalVariable;
    Button proceed;

    private Spinner SearchBy;

    String searchByTag="";
    ListR listR;

    String selectedUser="";
    String age="";
    String gender="";

    ImageView optionsSettings;

    String mUseType;
    @Override
    protected void onResume() {
        super.onResume();
        hide_soft_keypad();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_user);
        globalVariable = (GlobalClass)
                getApplicationContext();

        recyclerView=findViewById(R.id.listView);

        Button ExportUsers=findViewById(R.id.ExportUsers);
        ExportUsers.setVisibility(View.GONE);

        userModelList_= new ArrayList<>();
        userModelList_home=new ArrayList<>();

        autoCompleteTextView=findViewById(R.id.autoText);
        SearchBy=findViewById(R.id.searchBy);
        listR=  ExistingUserActivity.this;

        final TextView headertitle= findViewById(R.id.base_header_title);
        headertitle.setText(getString(R.string.sel_user));

        optionsSettings=findViewById(R.id.optionSettings);
        optionsSettings.setVisibility(View.GONE);

        final ImageView backKey=findViewById(R.id.imgBackKey);

        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if(globalVariable.getUserType()!=null){
            if(globalVariable.getUserType().equalsIgnoreCase("home")){
                headertitle.setText(getString(R.string.sel_fam_mem));
                mUseType="Home";

            }else{
                headertitle.setText(getString(R.string.sel_user));
                mUseType="Clinic";
            }
        }


        proceed=findViewById(R.id.proceed);
        proceed.setText(getString(R.string.select));
        proceed.setWidth(200);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!selectedUser.equals("")){
                    Intent intent = new Intent();
                    intent.putExtra("user", selectedUser);
                    intent.putExtra("age", age);
                    intent.putExtra("gender", gender);
                    setResult(200, intent);
                    finish();
                }else
                    Toast.makeText(ExistingUserActivity.this, "Please select Any User",Toast.LENGTH_SHORT).show();
                return;

            }
        });

        if(globalVariable.getUserType().equalsIgnoreCase("clinic")){
            userModelList_.addAll( DatabaseManager.getInstance().
                    getAllUserprofilecontent(globalVariable.getUsername(),Constants.USER_NAME));

            if(userModelList_.size()>=1){
                proceed.setVisibility(View.VISIBLE);
                populateRecyclerView("",mUseType);
            }else{
                Toast.makeText(ExistingUserActivity.this,"Sorry No Users",Toast.LENGTH_SHORT).show();
                proceed.setVisibility(View.INVISIBLE);
            }
        }else{
            userModelList_home.addAll( DatabaseManager.getInstance().
                    getAllMemberProfilecontent(globalVariable.getUsername(),Constants.USER_NAME));

            if(userModelList_home.size()>=1){
                proceed.setVisibility(View.VISIBLE);
                populateRecyclerView("",mUseType);
            }else{
                Toast.makeText(ExistingUserActivity.this,"Sorry No Users",Toast.LENGTH_SHORT).show();
                proceed.setVisibility(View.INVISIBLE);
            }
        }





        adapter = ArrayAdapter.createFromResource(ExistingUserActivity.this,
                R.array.search_by, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SearchBy.setAdapter(adapter);

        final String [] spinnerArr= getResources().getStringArray(R.array.search_by);
        SearchBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                if(adapterView.getItemAtPosition(pos).toString().equalsIgnoreCase(spinnerArr[0]))
                {
                    searchByTag="name";
                    autoCompleteTextView.setHint(getString(R.string.search_by_name));
                    autoCompleteTextView.setInputType(InputType.TYPE_CLASS_TEXT);
                }else{
                    searchByTag="phone";
                    autoCompleteTextView.setHint(getString(R.string.search_by_phone));
                    autoCompleteTextView.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                autoCompleteTextView.getText().clear();
                hide_soft_keypad();
                populateRecyclerView(searchByTag,mUseType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                //
                Logger.log(Level.DEBUG,ExistingUserActivity.class.getName(),
                        "Nothing gets selected in Spinner");
            }


        });
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {

                autoCompleteTextView.setCursorVisible(true);
                existingUserAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





}
    ExistingUserAdapter existingUserAdapter;

        private void   populateRecyclerView(String searchByTag,String useType){


            if(useType.equalsIgnoreCase("Clinic")){
                existingUserAdapter=new ExistingUserAdapter(ExistingUserActivity.this,
                        userModelList_,listR,searchByTag,useType);
            }else{
                existingUserAdapter=new ExistingUserAdapter(ExistingUserActivity.this,
                        userModelList_home,listR,searchByTag,useType);
            }

            recyclerView.setAdapter(existingUserAdapter);

        }

    @Override
    public void getSelectedUser(String data,String age1, String gender1, boolean[] array) {
        selectedUser=data;
        age=age1;
        gender=gender1;
    }


    //generate the data part 2 Absolute

    private void hide_soft_keypad() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }






}
