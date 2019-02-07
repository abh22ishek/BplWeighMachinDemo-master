package test.bpl.com.bplscreens;


import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.transition.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import constantsP.*;
import adapter.*;
import biolight.*;
import localstorage.*;



public class MenuPageActivty extends FragmentActivity {

    Dialog dialog;
    Button app_version,user_guide,my_profile,logout,manageUser;
    ImageView back;

    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }
        setContentView(R.layout.menu_page);
        setupWindowAnimations();

        app_version=  findViewById(R.id.app_version);
        app_version.setOnClickListener(mListner);

        user_guide=  findViewById(R.id.user_guide);
        user_guide.setOnClickListener(mListner);

        my_profile=  findViewById(R.id.my_profile);
        my_profile.setOnClickListener(mListner);

        logout=  findViewById(R.id.logout);
        logout.setOnClickListener(mListner);

        back=  findViewById(R.id.back_key);
        back.setOnClickListener(mListner);


        manageUser=findViewById(R.id.btnManageUser);
        manageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MenuPageActivty.this, ExistingUserActivityManage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }


    View.OnClickListener mListner= new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view==app_version)
            {
                app_version_dialog(MenuPageActivty.this);
            }else if(view==user_guide)
            {
                choose_option(MenuPageActivty.this);
            }else if(view==my_profile){

                intent=new Intent(MenuPageActivty.this,UsersProfileActivity.class);
                intent.putExtra(Constants.PROFILE_FLAG,Constants.PROFILE_EDIT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else if(view==logout)
            {
               confirmLogOut(MenuPageActivty.this);
            }else if(view==back)
            {
                finish();
            }
        }
    };

    private void app_version_dialog(Context context)
    {

        /*MyCustomDialogFragment dialogFragment=new MyCustomDialogFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragment.show(fragmentManager,"fragment");*/


        dialog = new Dialog(context);

        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.customdialog_security);

        final TextView content=  dialog.findViewById(R.id.content);
        final TextView header=  dialog.findViewById(R.id.header);
        final Button save= dialog.findViewById(R.id.save);
        final Button cancel=  dialog.findViewById(R.id.cancel);

        cancel.setVisibility(View.GONE);
        header.setText(getResources().getString(R.string.app_version));
        content.setText("The current app version is "+BuildConfig.VERSION_NAME );

        save.setText("OK");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void setupWindowAnimations() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition enterTrans = new Explode().setDuration(700);
            getWindow().setEnterTransition(enterTrans);

            //Transition returnTrans = new Slide().setDuration(700);
            //getWindow().setReturnTransition(returnTrans);
        }


    }


    private void confirmLogOut(Context context)
    {
        final Dialog dialog = new Dialog(context);


        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.customdialog_security);

        final TextView content=  dialog.findViewById(R.id.content);
        final TextView header=  dialog.findViewById(R.id.header);
        final Button save=  dialog.findViewById(R.id.save);
        save.setText(context.getString(R.string.ok));
        final Button cancel=  dialog.findViewById(R.id.cancel);

        cancel.setText(getString(R.string.no));
        header.setText(getString(R.string.logout));
        content.setText(context.getResources().getString(R.string.logout_content));

        save.setText(getString(R.string.yes));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StoreCredentials.loggedout(MenuPageActivty.this);
                final Intent intent=new Intent(MenuPageActivty.this,OximeterMainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        dialog.show();
    }



    ArrayList<String> options;


    private void choose_option(final Context context)
    {
        if(options==null)
        {
            options=new ArrayList<>();
            options.add(getString(R.string.weigh_mc));
            options.add(getString(R.string.ioxy));
            options.add(getString(R.string.biolight));
        }


        ArrayAdapter<String> adapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,options);
        if(dialog==null)
        {
            dialog = new Dialog(context);
        }


        dialog.getWindow().getAttributes().windowAnimations =R.style.DialogBoxAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.custom_dialog_items);
        final ListView content= dialog.findViewById(R.id.list_content);
        final TextView header= dialog.findViewById(R.id.header);


        content.setAdapter(adapter);
        header.setText(getResources().getString(R.string.selection));
        content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).equals(getString(R.string.weigh_mc)))
                {
                    // selectOptionWeighMachine(context,ConstantsP.RECORDS_WEIGH_MACHINE);
                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(MenuPageActivty.this);

                        Intent intent=new Intent(MenuPageActivty.this,WeighingMachineUsersguide.class);
                        intent.putExtra("weigh machine","weigh machine 2");
                        intent   .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent,options.toBundle());
                    }else{

                        Intent intent=new Intent(MenuPageActivty.this, WeighingMachineUsersguide.class);
                                intent.putExtra("weigh machine","weigh machine 2");
                               intent. setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }else if(adapterView.getItemAtPosition(i).equals(getString(R.string.ioxy)))
                {
                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(MenuPageActivty.this);

                        Intent intent=new Intent(MenuPageActivty.this, UserGuideContent.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent,options.toBundle());
                    }else{

                        Intent intent=new Intent(MenuPageActivty.this,UserGuideContent.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(intent);
                    }
                }else if(adapterView.getItemAtPosition(i).equals(getString(R.string.biolight)))
                {



                    ActivityOptions options;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        options = ActivityOptions.makeSceneTransitionAnimation(MenuPageActivty.this);

                        Intent intent=new Intent(MenuPageActivty.this,
                                BioLightUserHelpActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent,options.toBundle());
                    }else{

                        Intent intent=new Intent(MenuPageActivty.this,
                                BioLightUserHelpActivity.class).
                                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

                dialog.dismiss();
            }

        });

        dialog.show();
    }



}
