package test.bpl.com.bplscreens;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import biolight.*;
import constantsP.*;
import database.*;
import home.*;
import iweigh.*;
import logger.*;
import model.*;

import static com.tom_roush.fontbox.ttf.OS2WindowsMetricsTable.TAG;


public class SelectParameterActivity extends Activity {


    private ImageView select_image_ioxy, select_weigh_machine, select_records, bPump, userProfile, myADDUserProfile, infoDialog;

    boolean exit = false;

    GlobalClass globalVariable;
    TextView userNameSelected;

    private TextView userText;

    private TextView txtChangeUserSel;


    int mUsercount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        this.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_in);
        setContentView(R.layout.select_parameter);

        infoDialog = findViewById(R.id.infoDialog);
        infoDialog.setVisibility(View.INVISIBLE);



        infoDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        Constants.SELECTED_USER_SNOW = "";

        userNameSelected = findViewById(R.id.userNameSelected);
        userText = findViewById(R.id.userNameS);
        userNameSelected.setText("");
        userText.setVisibility(View.INVISIBLE);
        select_image_ioxy = findViewById(R.id.select_image_ioxy);
        select_weigh_machine = findViewById(R.id.select_weigh_machine_);
        select_records = findViewById(R.id.select_records);

        bPump = findViewById(R.id.bPump);
        select_records.setOnClickListener(mListner);
        select_weigh_machine.setOnClickListener(mListner);
        select_image_ioxy.setOnClickListener(mListner);
        bPump.setOnClickListener(mListner);

        userProfile = findViewById(R.id.profile);
        userProfile.setOnClickListener(mListner);

        myADDUserProfile = findViewById(R.id.user_profile);
        myADDUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(mUsercount==0){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        myADDUserProfile.setImageDrawable(getDrawable(R.mipmap.add_profile));
                    }else{
                        myADDUserProfile.setImageDrawable(getResources().getDrawable(R.mipmap.add_profile));
                    }
                    callAddOrEditUser(Constants.PROFILE_ADD);

                }
                else{
                    UserProfileOnClick(SelectParameterActivity.this);
                }


            }
        });
        txtChangeUserSel = findViewById(R.id.txtChangeUserSel);
        txtChangeUserSel.setText(getString(R.string.exit_user_log));


        txtChangeUserSel.setVisibility(View.INVISIBLE);


        txtChangeUserSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //

            }
        });

        globalVariable = (GlobalClass) getApplicationContext();

        Constants.LOGGED_User_ID = globalVariable.getUsername();


    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if the selected user is deleted


      /*List<UserModel> userList=DatabaseManager.getInstance().getAllUserprofilecontent(globalVariable.getUsername(),ConstantsP.USER_NAME);
        if(!userNameSelected.getText().toString().equals("") && !userList.contains(userNameChosen))
        {
           userNameChosen="";
        }*/

    }

    private View.OnClickListener mListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (view == select_weigh_machine) {

               /* Toast.makeText(SelectParameterActivity.this,"Coming Soon",Toast.LENGTH_SHORT).show();
                return;*/

               if (userNameSelected.getText().toString().equals("")) {

                   if(globalVariable.getUserType().equalsIgnoreCase("home")){
                       callExistingUserActivity(SelectParameterActivity.this);
                   }else{
                       if(isUserExistsForLoginProfile()){
                           callExistingUserActivity(SelectParameterActivity.this);
                       }else{
                           Toast.makeText(SelectParameterActivity.this,"Please Add User",Toast.LENGTH_SHORT).show();
                       }

                   }

                } else {
                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        startActivity(new Intent(SelectParameterActivity.this, IweighHomeScreenActivityl.class)
                                .putExtra(Constants.USER_NAME, userNameChosen)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
                    } else {
                        startActivity(new Intent(SelectParameterActivity.this, IweighHomeScreenActivityl.class)
                                .putExtra(Constants.USER_NAME, userNameChosen).
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }


            } else if (view == select_records) {
                //---To dispaly records of either Iweigh or Ioxy

                if (userNameSelected.getText().toString().equals("")) {
                    if(globalVariable.getUserType().equalsIgnoreCase("home")){
                        callExistingUserActivity(SelectParameterActivity.this);
                    }else{
                        if(isUserExistsForLoginProfile()){
                            callExistingUserActivity(SelectParameterActivity.this);
                        }else{
                            Toast.makeText(SelectParameterActivity.this,"Please Add User",Toast.LENGTH_SHORT).show();
                        }
                    }


                }else{
                    choose_option(SelectParameterActivity.this);

                }

            } else if (view == select_image_ioxy) {


                if (userNameSelected.getText().toString().equals("")) {
                    //selectOptionBPachine(SelectParameterActivity.this, "");
                    if(isUserExistsForLoginProfile()){
                        callExistingUserActivity(SelectParameterActivity.this);
                    }else{
                        Toast.makeText(SelectParameterActivity.this,"Please Add User",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        startActivity(new Intent(SelectParameterActivity.this, HomeScreenActivity.class).
                                putExtra(Constants.USER_NAME, userNameChosen).
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
                    } else {
                        startActivity(new Intent(SelectParameterActivity.this, HomeScreenActivity.class).
                                putExtra(Constants.USER_NAME, userNameChosen).
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }

            } else if (view == bPump) {

                if (userNameSelected.getText().toString().equals("")) {
                    if(globalVariable.getUserType().equalsIgnoreCase("home")){
                        callExistingUserActivity(SelectParameterActivity.this);
                    }else{
                        if(isUserExistsForLoginProfile()){
                            callExistingUserActivity(SelectParameterActivity.this);
                        }else{
                            Toast.makeText(SelectParameterActivity.this,"Please Add User",Toast.LENGTH_SHORT).show();
                        }
                    }



                } else {
                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        startActivity(new Intent(SelectParameterActivity.this, BioLightDeviceScanActivity.class).
                                putExtra(Constants.USER_NAME, userNameChosen).
                                putExtra("age", ageX).
                                putExtra("gender", genderX)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
                    } else {
                        startActivity(new Intent(SelectParameterActivity.this, BioLightDeviceScanActivity.class).
                                putExtra(Constants.USER_NAME, userNameChosen).
                                        putExtra("age", ageX).
                                        putExtra("gender", genderX).
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }

                //  Toast.makeText(SelectParameterActivity.this,"Coming Soon",Toast.LENGTH_SHORT).show();
            } else if (view == userProfile) {

                ActivityOptions options;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                    startActivity(new Intent(SelectParameterActivity.this, MenuPageActivty.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
                } else {
                    startActivity(new Intent(SelectParameterActivity.this, MenuPageActivty.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();

        Logger.log(Level.DEBUG,TAG,"On Restart ()");
        List<UserModel> userModelList_=new ArrayList<>();

        if(globalVariable.getUserType().equalsIgnoreCase("clinic")){
            userModelList_.addAll( DatabaseManager.getInstance().
                    getAllUserprofilecontent(globalVariable.getUsername(),Constants.USER_NAME));
        }else{
            userModelList_.addAll( DatabaseManager.getInstance().
                    getAllMemberProfilecontent(globalVariable.getUsername(),Constants.USER_NAME));
        }


        if(userModelList_.size()==0){
            userNameChosen="";
            userNameSelected.setText("");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //setupWindowAnimations();
    }


    @Override
    public void onBackPressed() {


        if (exit) {
            super.onBackPressed();
            this.finishAffinity(); // removes the activity from same task

        } else {
            Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }


    ArrayList<String> options;
    Dialog dialog;

    private void choose_option(final Context context) {
        if (options == null) {
            options = new ArrayList<>();
            options.add("I-Weigh");
            options.add("Bpl-Ioxy");
            options.add("BP Machine");
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, options);
        if (dialog == null) {
            dialog = new Dialog(context);
        }


        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_items);
        final ListView content = dialog.findViewById(R.id.list_content);
        final TextView header = dialog.findViewById(R.id.header);


        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i) == "I-Weigh") {
                    Toast.makeText(SelectParameterActivity.this,"Not Available",Toast.LENGTH_SHORT).show();
                    return;

                  /*  ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        Intent intent = new Intent(SelectParameterActivity.this, WeightMachine2RecordActivity.class)
                                .putExtra(ConstantsP.USER_NAME, userNameChosen).
                                        setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent, options.toBundle());
                    } else {

                        Intent intent = new Intent(SelectParameterActivity.this,
                                WeightMachine2RecordActivity.class).putExtra(ConstantsP.USER_NAME, userNameChosen).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }*/
                } else if (adapterView.getItemAtPosition(i) == "Bpl-Ioxy") {
                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        Intent intent = new Intent(SelectParameterActivity.this, UsersRecordActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.USER_NAME, userNameChosen);
                        startActivity(intent, options.toBundle());
                    } else {

                        Intent intent = new Intent(SelectParameterActivity.this, UsersRecordActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.USER_NAME, userNameChosen);

                        startActivity(intent);
                    }
                } else if (adapterView.getItemAtPosition(i) == "BP Machine") {


                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        Intent intent = new Intent(SelectParameterActivity.this,
                                BioLightRecordActivity.class).
                                putExtra(Constants.USER_NAME, userNameChosen).
                                putExtra("gender",genderX).putExtra("age",ageX).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent, options.toBundle());
                    } else {

                        Intent intent = new Intent(SelectParameterActivity.this,
                                BioLightDeviceScanActivity.class).
                                putExtra(Constants.USER_NAME, userNameChosen).
                                putExtra("gender",genderX).putExtra("age",ageX).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                dialog.dismiss();
            }

        });

        dialog.show();
    }


    private ArrayList<String> optionsList;
    private Dialog mDialog;
  /*  private void selectOptionWeighMachine(final Context context,final String TAG)
    {



        if(optionsList==null)
        {
            optionsList=new ArrayList<>();
            optionsList.add("I-Weigh Machine 1");
            optionsList.add("I-Weigh Machine 2");
        }



        ArrayAdapter<String> adapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,optionsList);
        if(mDialog==null)
        {
            mDialog = new Dialog(context);
        }

        try{
            mDialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }catch (Exception e){
            e.printStackTrace();
        }



        mDialog.setContentView(R.layout.custom_dialog_items);
        final ListView content=  mDialog.findViewById(R.id.list_content);
        final TextView header= mDialog.findViewById(R.id.header);


        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals("I-Weigh Machine 1"))  // weigh machine 1
                {
                    if(TAG.equalsIgnoreCase(ConstantsP.RECORDS_WEIGH_MACHINE)){
                        startActivity(new Intent(SelectParameterActivity.this,
                                WeighMachineRecordActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                }else{
                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        startActivity(new Intent(SelectParameterActivity.this,WeighMachineActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),options.toBundle());
                    }else{
                        startActivity(new Intent(SelectParameterActivity.this,WeighMachineActivity.class).
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }



                }

                else  // weigh machine 2
                {
                    if(TAG.equalsIgnoreCase(ConstantsP.RECORDS_WEIGH_MACHINE)){
                        startActivity(new Intent(SelectParameterActivity.this,WeightMachine2RecordActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }

                }

                mDialog.dismiss();
            }

        });

        mDialog.show();
    }
*/


    // select BP Machine
    private List<String> bPoptionsList;

   /* private void selectOptionBPachine(final Context context,final String TAG)
    {



        if(bPoptionsList==null)
        {
            bPoptionsList=new ArrayList<>();
            bPoptionsList.add(getString(R.string.biolight));
            bPoptionsList.add(getString(R.string.bpump));
        }



        ArrayAdapter<String> adapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,bPoptionsList);
        if(mDialog==null)
        {
            mDialog = new Dialog(context);
        }


        mDialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.custom_dialog_items);

        final ListView content= mDialog.findViewById(R.id.list_content);
        final TextView header= mDialog.findViewById(R.id.header);


        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals(getString(R.string.biolight)))  // biolight
                {
                    if(TAG.equalsIgnoreCase(ConstantsP.RECORDS_BP_MACHINE)){
                        startActivity(new Intent(SelectParameterActivity.this, BioLightRecordActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                    }else{
                        ActivityOptions options;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                            startActivity(new Intent(SelectParameterActivity.this,BioLightDeviceScanActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),options.toBundle());
                        }else{
                            startActivity(new Intent(SelectParameterActivity.this,BioLightDeviceScanActivity.class).
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    }



                }else  // BPUMP
                {
                    if(TAG.equalsIgnoreCase(ConstantsP.RECORDS_BP_MACHINE)){

                        Intent intent=new Intent(SelectParameterActivity.this,BplHomeScreenActivity.class);
                        intent.putExtra(ConstantsP.USER_NAME,ConstantsP.LOGGED_User_ID);
                        intent.putExtra("RECORD","RECORD_");
                        startActivity(intent);
                    }else{
                        ActivityOptions options;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                            options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                            Intent intent=new Intent(SelectParameterActivity.this,BplHomeScreenActivity.class);
                            intent.putExtra(ConstantsP.USER_NAME,ConstantsP.LOGGED_User_ID);
                            intent.putExtra("RECORD","d");
                            startActivity(intent,options.toBundle());
                        }else{

                            Intent intent=new Intent(SelectParameterActivity.this,BplHomeScreenActivity.class);
                            intent.putExtra(ConstantsP.USER_NAME,ConstantsP.LOGGED_User_ID);
                            intent.putExtra("RECORD","d");
                            startActivity(intent);
                        }
                    }

                }

                mDialog.dismiss();
            }

        });

        mDialog.show();
    }
*/

    List<String> userList;


    private void selectOptionBPachine(final Context context, final String TAG) {


        if (userList == null) {
            userList = new ArrayList<>();
            userList.add(getString(R.string.new_user));
            userList.add(getString(R.string.exist_user));
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, userList);
        if (mDialog == null) {
            mDialog = new Dialog(context);
        }


        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.custom_dialog_items);

        final ListView content = mDialog.findViewById(R.id.list_content);
        final TextView header = mDialog.findViewById(R.id.header);

        final ActivityOptions[] options = new ActivityOptions[1];
        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i).equals(getString(R.string.new_user)))  // biolight
                {


                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options[0] = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        Intent intent = new Intent(context, PatientProfileActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(Constants.PROFILE_FLAG, Constants.PROFILE_ADD);
                        startActivity(intent, options[0].toBundle());
                    } else {

                        Intent intent = new Intent(context, PatientProfileActivity.class);
                        intent.putExtra(Constants.PROFILE_FLAG, Constants.PROFILE_ADD);
                        startActivity(intent);
                    }


                } else  // Existing User
                {

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options[0] = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

                        Intent intent = new Intent(context, ExistingUserActivity.class);

                        startActivityForResult(intent, Constants.REQUEST_CODE, options[0].toBundle());

                    } else {

                        Intent intent = new Intent(context, ExistingUserActivity.class);
                        startActivityForResult(intent, Constants.REQUEST_CODE);
                    }

                }

                mDialog.dismiss();
            }

        });

        mDialog.show();
    }


    String userNameChosen = "";
    String ageX="";
    String genderX="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODE)
            if (resultCode == 200) {

                mUsercount++;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    myADDUserProfile.setImageDrawable(getDrawable(R.mipmap.profile_icon));
                }else{
                    myADDUserProfile.setImageDrawable(getResources().getDrawable(R.mipmap.profile_icon));
                }

                userText.setVisibility(View.VISIBLE);
                userNameChosen = data.getExtras().getString("user");
               ageX= data.getExtras().getString("age");
               genderX= data.getExtras().getString("gender");

                Constants.SELECTED_USER_SNOW = userNameChosen;
                txtChangeUserSel.setVisibility(View.GONE);
                userNameSelected.setText(new StringBuilder().append("Hello ").
                        append(userNameChosen).toString());


            }
    }


    private void callAddOrEditUser(String TAG)
    {
        ActivityOptions options;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);

            if(globalVariable.getUserType().equalsIgnoreCase("clinic")){
                startActivity(new Intent(SelectParameterActivity.this, PatientProfileActivity.class)
                        .putExtra(Constants.PROFILE_FLAG,TAG).putExtra("user",userNameChosen).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
            }else{
                startActivity(new Intent(SelectParameterActivity.this, HomeMemberProfileActivity.class)
                        .putExtra(Constants.PROFILE_FLAG,TAG).putExtra("user",userNameChosen).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK), options.toBundle());
            }

        } else {
            if(globalVariable.getUserType().equalsIgnoreCase("clinic")) {
                startActivity(new Intent(SelectParameterActivity.this, PatientProfileActivity.class)
                        .putExtra(Constants.PROFILE_FLAG,TAG).putExtra("user",userNameChosen).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }else{
                startActivity(new Intent(SelectParameterActivity.this, HomeMemberProfileActivity.class)
                        .putExtra(Constants.PROFILE_FLAG,TAG).putExtra("user",userNameChosen).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }

        }


    }


    private boolean isUserExistsForLoginProfile()
    {
        boolean flag=false;
        if(globalVariable.getUsername()!=null){
            List<UserModel>  list=DatabaseManager.getInstance().getAllUserprofilecontent(globalVariable.getUsername(),
                    Constants.USER_NAME);
            if(list.size()>=1)
                flag=true;
            else
                flag= false;
        }

     return flag;
    }




    private void UserProfileOnClick(final Context context)
    {


        final List<String>   userList = new ArrayList<>();


            userList.add(getString(R.string.ch_sel_user));
            userList.add(getString(R.string.add_user));
            userList.add(getString(R.string.switch_user));



        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, userList);
        if (mDialog == null) {
            mDialog = new Dialog(context);
        }


        mDialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.custom_dialog_items);

        final ListView content = mDialog.findViewById(R.id.list_content);
        final TextView header = mDialog.findViewById(R.id.header);

        final ActivityOptions[] options = new ActivityOptions[1];
        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (adapterView.getItemAtPosition(i).equals(getString(R.string.add_user)))  //
                {

                    callAddOrEditUser(Constants.PROFILE_ADD);


                } else if(adapterView.getItemAtPosition(i).equals(getString(R.string.switch_user))){

                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);
                        startActivityForResult(new Intent(SelectParameterActivity.this,
                                ExistingUserActivity.class), Constants.REQUEST_CODE, options.toBundle());
                    } else {
                        startActivityForResult(new Intent(SelectParameterActivity.this,
                                ExistingUserActivity.class), Constants.REQUEST_CODE);
                    }

                }


                else if(adapterView.getItemAtPosition(i).equals(getString(R.string.ch_sel_user)))  // Existing User
                {

                   callAddOrEditUser(Constants.PROFILE_EDIT);

                }

                mDialog.dismiss();
            }

        });

        mDialog.show();
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //
        Logger.log(Level.INFO,SelectParameterActivity.class.getName(),"On New Intent () called");
    }

    private void callExistingUserActivity(Context context)
    {
        ActivityOptions options;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(SelectParameterActivity.this);
            Intent intent = new Intent(context, ExistingUserActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE, options.toBundle());

        } else {

            Intent intent = new Intent(context, ExistingUserActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE);
        }
    }





}
