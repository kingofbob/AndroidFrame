package com.template.project.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.template.project.R;
import com.template.project.listobjects.CommonStickyListObject;
import com.template.project.objects.StudentDetails;
import com.template.project.utils.ImageLoader;
import com.template.project.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * 主页HomeFragment  Adapter
 * Created by YoKeyword on 16/2/1.
 */
public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<CommonStickyListObject> datas = new ArrayList<>();
    StudentDetails studentDetails = new StudentDetails();
    private LayoutInflater mInflater;
    private View profilePictureHeader;

    private OnItemClickListener mClickListener;

    public ProfileAdapter(Context context, List<CommonStickyListObject> datas, StudentDetails studentDetails, final Toolbar mToolbar) {
        this.datas = datas;
        this.mInflater = LayoutInflater.from(context);
        this.studentDetails = studentDetails;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position > 0){
            ((MyViewHolder)holder).title.setText(datas.get(position).getTitle());
            ((MyViewHolder)holder).subtitle.setText(datas.get(position).getSubTitle());
//            ((MyViewHolder)holder).subtitle.setHint(datas.get(position).getHint());
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) { // main header
            profilePictureHeader = mInflater.inflate(R.layout.header_picture_profile, parent, false);
            return new ProfilePictureViewHolder(profilePictureHeader);
        } else if (datas.get(viewType).isHeader()) {
            View headerView = mInflater.inflate(R.layout.header_profile, parent, false);
            return new MyViewHolder(headerView);
        } else {
            View bodyView = mInflater.inflate(R.layout.row_profile, parent, false);
            return new MyViewHolder(bodyView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView subtitle;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);


        }
    }

    class ProfilePictureViewHolder extends RecyclerView.ViewHolder {


        public ProfilePictureViewHolder(View itemView) {
            super(itemView);
            // set profile name and id
            TextView tvProfileName = (TextView) itemView.findViewById(R.id.profile_name);
            TextView tvProfileId = (TextView) itemView.findViewById(R.id.profile_id);
            ImageView ivProfileImage = (ImageView) itemView.findViewById(R.id.profile_image);

            tvProfileName.setText(studentDetails.getFullName());
            tvProfileId.setText(studentDetails.getStudentId());
            if(studentDetails.getPhotoURL() != null){
                new ImageLoader.DownloadImageTask(ivProfileImage)
                        .execute(studentDetails.getPhotoURL());
            }
        }
    }


    public View getProfilePictureHeader(){
        return profilePictureHeader;
    }



}
