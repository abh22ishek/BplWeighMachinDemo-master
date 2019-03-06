package adapter;

import android.annotation.*;
import android.content.*;
import android.graphics.drawable.*;
import android.support.v4.content.*;
import android.view.*;
import android.widget.*;

import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.resource.drawable.*;
import com.bumptech.glide.request.animation.*;
import com.bumptech.glide.request.target.*;

import java.util.*;

import constantsP.*;
import customviews.*;
import logger.*;
import model.*;
import test.bpl.com.bplscreens.*;

public class ExistingIUserAdapterDel extends BaseAdapter implements MarkUsers{

    Context context;
    List<UserModel> userModelList;
    boolean array[];
    ListR listlistner;
    String mUseType;
    boolean activeUsrsArray[];
    Set<String> activeUsersS;
    Set<String> dormantUsersS;
String uri;
    public ExistingIUserAdapterDel(Context context, List<UserModel> userModelList,ListR listlistner,String usetType) {
        this.context = context;
        this.userModelList = userModelList;
        array =new boolean[userModelList.size()];
        this.listlistner=listlistner;
        mUseType=usetType;
    }

    @Override
    public int getCount() {
        return userModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return userModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ExistingIUserAdapterDel.ViewHolder holder;

        if(convertView==null){
            holder = new ExistingIUserAdapterDel.ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.exist_user_bg_del,null);
            holder.txt1 =  convertView.findViewById(R.id.name);
            holder.checkBox = convertView.findViewById(R.id.checkbox);
            holder.phone=convertView.findViewById(R.id.phone);
            holder.age=convertView.findViewById(R.id.age);

            holder.height=convertView.findViewById(R.id.height);
            holder.weight=convertView.findViewById(R.id.weight);

            holder.gender=convertView.findViewById(R.id.gender);
            holder.patientIcon=convertView.findViewById(R.id.patientIcon);
            convertView.setTag(holder);
        }else{

            holder = (ExistingIUserAdapterDel.ViewHolder) convertView.getTag();
        }


        if(userModelList.get(position).getUserName().length()>=15)
        {
            String mx= userModelList.get(position).getUserName().substring(0,14)+"..";
            holder.txt1.setText(new StringBuilder().append(mx).append(".."));

        }else{
             holder.txt1.setText(userModelList.get(position).getUserName());
        }

        holder.phone.setText(userModelList.get(position).getPhone());
        holder.age.setText(new StringBuilder().append(userModelList.get(position).getAddress()).append(" Years").toString());

        holder.gender.setText(new StringBuilder().append(",").
                append(userModelList.get(position).getSex()).toString());
        holder.height.setText(new StringBuilder().append(",").
                append(userModelList.get(position).getHeight()).append(" CM").toString());
       /* holder.weight.setText(new StringBuilder().append(",").
                append(userModelList.get(position).getWeight()).append(" Kg").toString());*/





        if(mUseType.equalsIgnoreCase("Home")){
            uri = get_profile_image(userModelList.get(position).getUserName()+"_"+"home");
        }else{
            uri = get_profile_image(userModelList.get(position).getUserName()+"_"+"clinic");
        }

        if(uri!=""){
            Glide
                    .with(context)
                    .load(uri)
                    .override(80, 80)
                    .centerCrop()// resizes the image to these dimensions (in pixel). does not respect aspect ratio
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new GlideDrawableImageViewTarget(holder.patientIcon) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);

                            Logger.log(Level.DEBUG,"Patient Icon","Glide loaded the image successfully");
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);

                            Logger.log(Level.ERROR,"Patient Icon",e.toString());
                        }
                    });


        }else{
            holder.patientIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.user_icon));
        }



        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if(isChecked)
                {
                    array[position]=true;
                    listlistner.getSelectedUser("data","","",array);

                }
                else{
                    array[position]=false;
                    listlistner.getSelectedUser("data","","",array);
                }

            }
        });

        for(int i=0;i<array.length;i++){
            holder.checkBox.setChecked(array[position]);
        }
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public void markAll() {
        for(int i=0;i <array.length;i++){
            array[i]=true;
        }

        notifyDataSetChanged();

    }

    @Override
    public void unMarkAll() {
        for(int i=0;i <array.length;i++){
            array[i]=false;
        }

        notifyDataSetChanged();
    }


    @Override
    public void activeUsers(List<String> activeUsers) {
        for(int i=0;i<array.length;i++){
            Logger.log(Level.DEBUG,"--Check Box--",""+array[i]);
            array[i]=false;

        }

        notifyDataSetChanged();
        List<String> userModelListString=new ArrayList<>();

       for(int i=0;i<userModelList.size();i++){
           userModelListString.add(userModelList.get(i).getUserName());
       }


        for(int i=0;i<activeUsers.size();i++){
           if(userModelListString.contains(activeUsers.get(i)))
            {
                int pos=userModelListString.indexOf(activeUsers.get(i));
                array[pos]=true;
            }else{
                array[i]=false;
            }
            notifyDataSetChanged();
        }


        for(int i=0;i<array.length;i++){
            Logger.log(Level.DEBUG,"--Check Box--",""+array[i]);
        }




    }

    @Override
    public void dormantUsers(List<String> dormantUsers) {
        for(int i=0;i<array.length;i++){
            Logger.log(Level.DEBUG,"--Check Box--",""+array[i]);
            array[i]=false;

        }
        notifyDataSetChanged();
        List<String> userModelListString=new ArrayList<>();

        for(int i=0;i<userModelList.size();i++){
            userModelListString.add(userModelList.get(i).getUserName());
        }


        for(int i=0;i<userModelListString.size();i++){


            if(!userModelListString.contains(dormantUsers))
            {
                array[i]=true;
            }else{
                int pos=userModelListString.indexOf(dormantUsers);
                array[pos]=false;
            }

            notifyDataSetChanged();
        }


        for(int i=0;i<array.length;i++){
            Logger.log(Level.DEBUG,"--Check Box--",""+array[i]);
        }


    }


    class ViewHolder{
        TextView txt1;
        CheckBox checkBox;
        TextView phone ,age,gender,height,weight;
        RoundedImageView patientIcon;
    }


    private String get_profile_image(String key_username) {
        SharedPreferences profile_image_prefs;
        profile_image_prefs =context.
                getSharedPreferences(Constants.PREFERENCE_PROFILE_IMAGE, Context.MODE_PRIVATE);

        String image_str = profile_image_prefs.getString(key_username, "");
        Logger.log(Level.INFO, "SHARED PREFS", "get profile image from shared pref=" + image_str);
        return image_str;

    }





}
