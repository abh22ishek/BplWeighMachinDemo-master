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

public class ExistingUserAdapter extends BaseAdapter implements Filterable{


    CustomFilter filter;
    Context context;
    List<UserModel> userModelList;
    boolean array[];
    ListR listlistner;
    String searchByTag;
    List<UserModel> filterList;
    String uri;
    String useType;
    public ExistingUserAdapter(Context context, List<UserModel> userModelList,
                               ListR listlistner,String serchbyTag,String mUseType) {
        this.context = context;
        this.userModelList = userModelList;
        array =new boolean[userModelList.size()];
        this.listlistner=listlistner;
        this. filterList=userModelList;
        this.searchByTag=serchbyTag;
        this.useType=mUseType;
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
        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();


            convertView = LayoutInflater.from(context).inflate(R.layout.existuser_bg,null);
            holder.txt1 =  convertView.findViewById(R.id.name);
            holder.radioButton = convertView.findViewById(R.id.checkbox);

            holder.phone=convertView.findViewById(R.id.phone);
            holder.age=convertView.findViewById(R.id.age);

            holder.height=convertView.findViewById(R.id.height);
            holder.weight=convertView.findViewById(R.id.weight);
            holder.gender=convertView.findViewById(R.id.gender);
            holder.patientIcon=convertView.findViewById(R.id.patientIcon);

            holder.weight.setVisibility(View.GONE);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if(userModelList.get(position).getUserName().length()>=15)
        {
            String mx= userModelList.get(position).getUserName().substring(0,14)+"..";
            holder.txt1.setText(new StringBuilder().append(mx).append(".."));

        }else{
            holder.txt1.setText(userModelList.get(position).getUserName());
        }
        holder.radioButton.setChecked(array[position]);
        holder.phone.setText(userModelList.get(position).getPhone());

        if(useType.equalsIgnoreCase("Clinic")){
            holder.age.setText(new StringBuilder().
                    append(userModelList.get(position).getAddress()).append(" Years").toString());

        }else{
            holder.age.setText(new StringBuilder().
                    append(userModelList.get(position).getAge()).append(" Years").toString());
        }

        holder.gender.setText(new StringBuilder().append(",").
                append(userModelList.get(position).getSex()).toString());
        holder.height.setText(new StringBuilder().append(",").
                append(userModelList.get(position).getHeight()).append(" CM").toString());
     //   holder.weight.setText(new StringBuilder().append(",").
       //         append(userModelList.get(position).getWeight()).append("Kg").toString());


        if(useType.equalsIgnoreCase("Clinic")) {
            uri = get_profile_image(userModelList.get(position).getUserName()+"_"+"clinic");
        }
        else{
             uri = get_profile_image(userModelList.get(position).getUserName()+"_"+"home");
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




        holder.radioButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Logger.log(Level.DEBUG,"{{-------}}",userModelList.get(position).getUserName());
                listlistner.getSelectedUser(userModelList.get(position).getUserName(),userModelList.get(position).getAge()
                        ,userModelList.get(position).getSex(),array);
                for (int i=0;i<array.length;i++){
                    if(i==position){
                        array[i]=true;
                    }else{
                        array[i]=false;
                    }
                }
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter == null)
        {
            filter=new CustomFilter();
        }

        return filter;
    }

    class ViewHolder{
        TextView txt1,phone ,age,gender,height,weight;
        RadioButton radioButton;
        RoundedImageView patientIcon;

    }




    class CustomFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            FilterResults results=new FilterResults();

            if(constraint != null && constraint.length()>0)
            {
                //CONSTARINT TO UPPER
                constraint=constraint.toString().toLowerCase();

                ArrayList<UserModel> filters=new ArrayList<>();


                if(searchByTag.equals("name"))
                {
                    for(int i=0;i<filterList.size();i++)
                    {
                        if(filterList.get(i).getUserName().toLowerCase().contains(constraint))
                        {
                            UserModel m=new UserModel();
                            m.setUserName(filterList.get(i).getUserName());
                            // m.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME_)));
                            m.setAddress(filterList.get(i).getAddress());
                            m.setAge(filterList.get(i).getAge());
                            m.setSex(filterList.get(i).getSex());

                            m.setHeight(filterList.get(i).getHeight());
                            m.setWeight(filterList.get(i).getWeight());

                            m.setPhone(filterList.get(i).getPhone());


                            filters.add(m);
                        }
                    }
                }else{
                    for(int i=0;i<filterList.size();i++)
                    {
                        if(filterList.get(i).getPhone().contains(constraint))
                        {
                            UserModel m=new UserModel();
                            m.setUserName(filterList.get(i).getUserName());
                            // m.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME_)));
                            m.setAddress(filterList.get(i).getAddress());
                            m.setAge(filterList.get(i).getAddress());
                            m.setSex(filterList.get(i).getSex());

                            m.setHeight(filterList.get(i).getHeight());
                            m.setWeight(filterList.get(i).getWeight());

                            m.setPhone(filterList.get(i).getPhone());

                            filters.add(m);
                        }
                    }
                }
                //get specific items


                results.count=filters.size();
                results.values=filters;

            }else
            {
                results.count=filterList.size();
                results.values=filterList;

            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub

            userModelList=(ArrayList<UserModel>) results.values;
            notifyDataSetChanged();
        }

    }



    private String get_profile_image(String key_username) {
        SharedPreferences profile_image_prefs;
        profile_image_prefs =context.getSharedPreferences(Constants.PREFERENCE_PROFILE_IMAGE, Context.MODE_PRIVATE);

        String image_str = profile_image_prefs.getString(key_username, "");
        Logger.log(Level.INFO, "SHARED PREFS", "get profile image from shared pref=" + image_str);
        return image_str;

    }
}
