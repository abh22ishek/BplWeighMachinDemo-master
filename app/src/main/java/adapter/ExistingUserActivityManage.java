package adapter;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.app.*;
import android.support.v7.app.AlertDialog;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import constantsP.*;
import database.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.*;

public class ExistingUserActivityManage extends FragmentActivity implements ListR, PopupMenu.OnMenuItemClickListener {

    private ListView recyclerView;
    private List<UserModel> userModelList_;

    private GlobalClass globalVariable;
    private Button proceed,exportUsers;


    private ListR listR;

    String selectedTAG="";


    private LinearLayout layout;
    private PopupMenu popup;
    private ImageView optionsSettings;
    private ExistingIUserAdapterDel existingUserAdapter;


    String mUseType;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exit_user);
        globalVariable = (GlobalClass) getApplicationContext();
        recyclerView=findViewById(R.id.listView);
        userModelList_= new ArrayList<>();

        layout=findViewById(R.id.layout);
        layout.setVisibility(View.GONE);


        optionsSettings=findViewById(R.id.optionSettings);
        optionsSettings.setVisibility(View.VISIBLE);

        optionsSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 popup = new PopupMenu(ExistingUserActivityManage.this,view);
                popup.setOnMenuItemClickListener(ExistingUserActivityManage.this);
                popup.inflate(R.menu.settings_options);
                if(globalVariable!=null && globalVariable.getUserType().equalsIgnoreCase("home")){
                    popup.getMenu().findItem(R.id.marlActive).setTitle("Mark Active Members").setVisible(false);
                    popup.getMenu().findItem(R.id.MarkDormant).setTitle("Mark Dormant Members").setVisible(false);

                }else{
                    popup.getMenu().findItem(R.id.marlActive).setTitle("Mark Active Users");
                    popup.getMenu().findItem(R.id.MarkDormant).setTitle("Mark Dormant Users");
                }

                if(!popUpTag){
                    if(globalVariable!=null && globalVariable.getUserType().equalsIgnoreCase("home")){
                        popup.getMenu().findItem(R.id.markAll).setTitle("Mark All Members");
                    }else{
                        popup.getMenu().findItem(R.id.markAll).setTitle("Mark All Users");
                    }


                }else{
                    if(globalVariable!=null && globalVariable.getUserType().equalsIgnoreCase("home")){
                        popup.getMenu().findItem(R.id.markAll).setTitle("Unmark All Members");
                    }else{
                        popup.getMenu().findItem(R.id.markAll).setTitle("Unmark All Users");
                    }


                }
                popup.show();
            }
        });
        listR=this;

        final TextView headertitle= findViewById(R.id.base_header_title);



        if(globalVariable.getUserType().equalsIgnoreCase("clinic")){
            userModelList_.addAll(DatabaseManager.getInstance().getAllUserprofilecontent
                    (globalVariable.getUsername(), Constants.USER_NAME));
            mUseType="Clinic";
            headertitle.setText(getString(R.string.sel_user));

        }else{
            userModelList_.addAll(DatabaseManager.getInstance().getAllMemberProfilecontent
                    (globalVariable.getUsername(), Constants.USER_NAME));
            mUseType="Home";
            headertitle.setText(getString(R.string.sel_fam_mem));

        }



        existingUserAdapter=new ExistingIUserAdapterDel(ExistingUserActivityManage.this,
                userModelList_,listR,mUseType);


        recyclerView.setAdapter(existingUserAdapter);



        final ImageView backKey=findViewById(R.id.imgBackKey);

        backKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        proceed=findViewById(R.id.proceed);
        exportUsers=findViewById(R.id.ExportUsers);

        exportUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedTAG.equals("")){
                    Toast.makeText(ExistingUserActivityManage.this, "Please Select Any Users",
                            Toast.LENGTH_SHORT).show();
                return;
            }
                if(selectedTAG.equalsIgnoreCase("Mark All Users") ||
                        selectedTAG.equalsIgnoreCase("Mark All Members"))
                {
                    ExportAllUsers();
                }else if(selectedTAG.equalsIgnoreCase("Mark Active Users")
                        || selectedTAG.equalsIgnoreCase("Mark Active Members"))
                {
                    ExportActiveUsers();

                }else if(selectedTAG.equalsIgnoreCase("Mark Dormant Users") ||
                        selectedTAG.equalsIgnoreCase("Mark Dormant Members")){
                    ExportDormantUsers();
                }

            }
        });


        proceed.setText(getString(R.string.delete));

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(booleansArr!=null){

                    ConfirmDialog(ExistingUserActivityManage.this);
                }else{
                    Toast.makeText(ExistingUserActivityManage.this,
                            "Please Select Any record",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }





    private boolean [] booleansArr;

    private void deleteSelectedRecords(boolean [] array){
        for(int i=0;i<array.length;i++){
                if(array[i]){
                    // delete those records
                    deleteRecords(i);
                    }
            }

    }



    private void deleteRecords(int index){

        DatabaseManager.getInstance().openDatabase();
        try{
            if(globalVariable.getUserType().equalsIgnoreCase("clinic"))
            DatabaseManager.getInstance().deleteUser(userModelList_.get(index).getUserName());
            else
                DatabaseManager.getInstance().deleteFamilyMembers(userModelList_.get(index).getUserName());

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            refreshRecords();
        }
    }

    private void refreshRecords(){
        if(userModelList_.size()>0)
        {
            userModelList_.clear();
        }

        try{
            userModelList_= DatabaseManager.getInstance().
                    getAllUserprofilecontent(globalVariable.getUsername(), Constants.USER_NAME);


        }catch (Exception e){
            e.printStackTrace();
        }
    }


    List<LastActivityModel> lastActivityModelList;
    List<LastActivityModel> ActiveUserModelList;
    List<LastActivityModel> DormantUserModelList;
    boolean popUpTag;

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.markAll:
                // do your code

                if(globalVariable.getUserType().equalsIgnoreCase("clinic"))
                selectedTAG="Mark All Users";
                else
                    selectedTAG="Mark All Members";
                if(!popUpTag)
                {
                    existingUserAdapter.markAll();
                    Toast.makeText(this, "Mark All", Toast.LENGTH_SHORT).show();
                    popUpTag=true;

                }else{
                    popUpTag=false;
                    existingUserAdapter.unMarkAll();
                    booleansArr=null;
                //    popup.getMenu().findItem(R.id.markAll).setTitle("Un Mark All");
                }

                return true;

            case R.id.marlActive:
                // do your code

                // Database Query
                if(globalVariable.getUserType().equalsIgnoreCase("clinic"))
                selectedTAG="Mark Active Users";
                else
                    selectedTAG="Mark Active Members";

                if(globalVariable.getUsername()!=null){
                    DatabaseManager.getInstance().openDatabase();
                   lastActivityModelList= DatabaseManager.getInstance().
                           getUserNamesFromLastActivity(globalVariable.getUsername());

                   ActiveUserModelList=new ArrayList<>();
                   DormantUserModelList=new ArrayList<>();

                   for(LastActivityModel m:lastActivityModelList)
                   {

                       if(Integer.parseInt(m.getLastActivity())<=30){
                           // Active Users
                           LastActivityModel p=new LastActivityModel();
                           p.setUserNames_(m.getUserNames_());
                           p.setDeviceParameter(m.getDeviceParameter());
                           p.setLastActivity(m.getLastActivity());
                           ActiveUserModelList.add(p);
                       }else{

                           // Dormant Users

                           LastActivityModel p1=new LastActivityModel();
                           p1.setUserNames_(m.getUserNames_());
                           p1.setDeviceParameter(m.getDeviceParameter());
                           p1.setLastActivity(m.getLastActivity());
                           DormantUserModelList.add(p1);
                       }
                   }

                    Logger.log(Level.DEBUG,"--Active User Model-",""+ActiveUserModelList.size());
                    Logger.log(Level.DEBUG,"--Dormant User Model-",""+DormantUserModelList.size());


                    // iterate through active and dormant


                    List<String> FinalActiveUserList = new ArrayList<>();

                    Logger.log(Level.DEBUG,"--ActiveUser Hash Set-",""+FinalActiveUserList.size());
                  //  ActiveUserModelList.clear();

                    Set<String> userNameSet=new HashSet<>();
                    for(int i=0; i<ActiveUserModelList.size();i++){
                        userNameSet.add(ActiveUserModelList.get(i).getUserNames_());


                    }

                    FinalActiveUserList.addAll(userNameSet);
                    Logger.log(Level.DEBUG,"--Active User userNameSet-",""+FinalActiveUserList);
                    existingUserAdapter.activeUsers(FinalActiveUserList);

                }

                return true;
            case R.id.MarkDormant:
                // do your code
                if(globalVariable.getUserType().equalsIgnoreCase("clinic"))
                selectedTAG="Mark Dormant Users";
                else
                    selectedTAG="Mark Dormant Members";

                if(globalVariable.getUsername()!=null){
                    DatabaseManager.getInstance().openDatabase();
                    lastActivityModelList= DatabaseManager.getInstance().
                            getUserNamesFromLastActivity(globalVariable.getUsername());

                    ActiveUserModelList=new ArrayList<>();
                    DormantUserModelList=new ArrayList<>();

                    for(LastActivityModel m:lastActivityModelList)
                    {

                        if(Integer.parseInt(m.getLastActivity())<=30){
                            // Active Users
                            LastActivityModel p=new LastActivityModel();
                            p.setUserNames_(m.getUserNames_());
                            p.setDeviceParameter(m.getDeviceParameter());
                            p.setLastActivity(m.getLastActivity());
                            ActiveUserModelList.add(p);
                        }else{

                            // Dormant Users

                            LastActivityModel p1=new LastActivityModel();
                            p1.setUserNames_(m.getUserNames_());
                            p1.setDeviceParameter(m.getDeviceParameter());
                            p1.setLastActivity(m.getLastActivity());
                            DormantUserModelList.add(p1);
                        }
                    }

                    Logger.log(Level.DEBUG,"--Active User Model-",""+ActiveUserModelList.size());
                    Logger.log(Level.DEBUG,"--Dormant User Model-",""+DormantUserModelList.size());


                    // iterate through active and dormant


                    List<String> FinalDormantUserList = new ArrayList<>();

                    Logger.log(Level.DEBUG,"--ActiveUser Hash Set-",""+FinalDormantUserList.size());
                    //  ActiveUserModelList.clear();

                    Set<String> userNameSet=new HashSet<>();
                    for(int i=0; i<DormantUserModelList.size();i++){
                        userNameSet.add(DormantUserModelList.get(i).getUserNames_());


                    }

                    FinalDormantUserList.addAll(userNameSet);
                    Logger.log(Level.DEBUG,"--Active User userNameSet-",""+FinalDormantUserList);
                    existingUserAdapter.dormantUsers(FinalDormantUserList);

                }
                return true;



                default:
                return false;
        }

    }



    private void ExportAllUsers()
    {
        StringBuilder sb=new StringBuilder();

        try {
            for(int i=0;i<userModelList_.size();i++){
                sb.append(i+1).append(". ").append(" Name :").append(userModelList_.get(i).getUserName()).
                        append(" Email :").append(userModelList_.get(i).getUserEmail()).append(" Gender :")
                        .append(userModelList_.get(i).getSex()).append(" Age : ").append(userModelList_.get(i).getAddress()).
                        append("Years").append(" Phone No. ").append(userModelList_.get(i).getPhone()).append(" Height :").
                        append(userModelList_.get(i).getHeight()+"CM").append(" Weight :").append(userModelList_.get(i).getWeight()+"Kg").
                        append(" Active Status :").append("Active");
                sb.append("\n\n");

            }

            createUsersTextFile("ListOfUsers_"+DateTime.getDateTime()+".txt", globalVariable.getUsername(),sb);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Toast.makeText(ExistingUserActivityManage.this,
                "Users Exported Successfully",Toast.LENGTH_SHORT).show();
    }

    private void ExportActiveUsers()
    {
        StringBuilder sb=new StringBuilder();

        try {
            for(int i=0;i<userModelList_.size();i++){
                sb.append(i+1).append(". ").append(" Name :").append(userModelList_.get(i).getUserName()).
                        append(" Email :").append(userModelList_.get(i).getUserEmail()).append(" Gender :")
                        .append(userModelList_.get(i).getSex()).append(" Age : ").append(userModelList_.get(i).getAddress()).
                        append("Years").append(" Phone No. ").append(userModelList_.get(i).getPhone()).append(" Height :").
                        append(userModelList_.get(i).getHeight()+"CM").append(" Weight :").append(userModelList_.get(i).getWeight()+"Kg").
                        append(" Active Status :").append("Active");
                sb.append("\n\n");

            }

            createUsersTextFile("ListOfUsers_"+DateTime.getDateTime()+".txt", globalVariable.getUsername(),sb);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Toast.makeText(ExistingUserActivityManage.this,
                "Active Users Exported Successfully",Toast.LENGTH_SHORT).show();
    }
    private void ExportDormantUsers()
    {
        StringBuilder sb=new StringBuilder();

        try {
            for(int i=0;i<userModelList_.size();i++){
                sb.append(i+1).append(". ").append(" Name :").append(userModelList_.get(i).getUserName()).
                        append(" Email :").append(userModelList_.get(i).getUserEmail()).append(" Gender :")
                        .append(userModelList_.get(i).getSex()).append(" Age : ").append(userModelList_.get(i).getAddress()).
                        append("Years").append(" Phone No. ").append(userModelList_.get(i).getPhone()).append(" Height :").
                        append(userModelList_.get(i).getHeight()+"CM").append(" Weight :").append(userModelList_.get(i).getWeight()+"Kg").
                        append(" Active Status :").append("Active");
                sb.append("\n\n");

            }

            createUsersTextFile("ListOfUsers_"+DateTime.getDateTime()+".txt", globalVariable.getUsername(),sb);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Toast.makeText(ExistingUserActivityManage.this,
                "Dormant Users Exported Successfully",Toast.LENGTH_SHORT).show();
    }

    private void ConfirmDialog(Context context)
    {
      final Dialog dialog = new Dialog(context);
        if(dialog.getWindow()!=null)
        {
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogBoxAnimation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.customdialog_security);
        }

        final TextView content= dialog.findViewById(R.id.content);
        final TextView header=  dialog.findViewById(R.id.header);
        final Button save=  dialog.findViewById(R.id.save);
        final Button cancel=  dialog.findViewById(R.id.cancel);

        save.setText(getString(R.string.yes));
        cancel.setText(getString(R.string.no));

        header.setText(context.getResources().getString(R.string.sel_user));
        content.setText(context.getResources().getString(R.string.sure_sel_user));

        save.setText(context.getResources().getString(R.string.yes));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                deleteSelectedRecords(booleansArr);
                Toast.makeText(ExistingUserActivityManage.this,
                        "Selected Records Deleted",Toast.LENGTH_SHORT).show();
                finish();
                dialog.dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }



    private static void  createUsersTextFile(String fileName, String loginFileName, StringBuilder data)
    {
        String fileNameDir=Constants.BPL_FOLDER;

        File file =new File(Environment.getExternalStorageDirectory(),fileNameDir);
        if(!file.exists())
        {
            file.mkdir();

        }

        File loginFile=new File(file,loginFileName);

        if(!loginFile.exists()){
            loginFile.mkdir();
        }


        File listOfUsersFile=new File(loginFile,fileName);


        try {
            FileWriter filewriter=new FileWriter(listOfUsersFile,false);
            filewriter.append(data);
            filewriter.flush();
            filewriter.close();
            Log.i("FilePath", "saving data into file");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    @Override
    public void getSelectedUser(String data, String age1, String gender1, boolean[] arrays) {
        booleansArr=new boolean[arrays.length];
        //copying one array to another
       // selectedTAG="without_tag";
        booleansArr=Arrays.copyOf(arrays,arrays.length);
    }




    android.support.v7.app.AlertDialog alert;


    private void showDialog(Context context,String title,String message)
    {

            android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if(alert.isShowing()){
                                alert.dismiss();
                               finish();
                            }

                        }
                    });



            alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            logger.Logger.log(logger.Level.DEBUG, "Existing User Manage", "Alert dialog box gets called");
            if(!alert.isShowing()){
                alert.show();
            }

        }


    @Override
    protected void onResume() {
        super.onResume();
        if(userModelList_.size()==0){
            if(globalVariable.getUserType().equalsIgnoreCase("Home"))
            showDialog(ExistingUserActivityManage.this,"No Members Available",
                    "Sorry No Primary Members Available. Please create a new one");
            else{
                showDialog(ExistingUserActivityManage.this,"No Users Available",
                        "Sorry No Users are Available. Please create a new one");

            }
        }
    }
}


