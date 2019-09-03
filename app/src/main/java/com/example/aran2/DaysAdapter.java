package com.example.aran2;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DaysAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static class DaysHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_tags, tv_contents;
        Button btn_delete;
        ViewPager vp_images;
//        ImageView iv_tempV;

        DaysHolder(View view) {
            super(view);
            tv_title = view.findViewById(R.id.todayInfo);
            tv_tags = view.findViewById(R.id.diary_tagEmotion);
            tv_contents = view.findViewById(R.id.contents);
            btn_delete = view.findViewById(R.id.trashCan);
  //          iv_tempV = view.findViewById(R.id.diary_TempimageView);
            vp_images = view.findViewById(R.id.diary_images);
        }

    }

    private ArrayList<DiaryContent> diaryContents;
    private Context context;
    DaysAdapter(Context context, ArrayList<DiaryContent> diaryContents) {
        this.diaryContents = diaryContents;
        this.context = context;
    }

    private View v;
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.diary, viewGroup, false);
        return new DaysHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final DaysHolder daysHolder = (DaysHolder) viewHolder;
        daysHolder.tv_title.setText(diaryContents.get(i).day);
        String emotion = diaryContents.get(i).emotion +", " + diaryContents.get(i).state;
        daysHolder.tv_tags.setText(emotion);
        daysHolder.tv_contents.setText(diaryContents.get(i).content);
        final int position = i;
        daysHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diaryContents.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });
//        if(diaryContents.get(i).uris != null){
//            ViewGroup.LayoutParams temp = daysHolder.vp_images.getLayoutParams();
//            temp.height = 300;
//            daysHolder.vp_images.setLayoutParams(temp);
//            ViewPageAdapter viewPageAdapter = new ViewPageAdapter(context,diaryContents.get(i).uris);
//            daysHolder.vp_images.setAdapter(viewPageAdapter);
//        }else{
//            ViewGroup.LayoutParams temp = daysHolder.vp_images.getLayoutParams();
//            temp.height = 100;
//            daysHolder.vp_images.setLayoutParams(temp);
//        }

        if(diaryContents.get(position).imageUri!=null){
            ViewGroup.LayoutParams temp = daysHolder.vp_images.getLayoutParams();
            temp.height = context.getResources().getDisplayMetrics().widthPixels;
            //      ViewGroup.LayoutParams temp = daysHolder.iv_tempV.getLayoutParams();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            DiaryViewpageAdapter diaryViewpageAdapter = new DiaryViewpageAdapter(context,diaryContents.get(position).imageUri);
            for(int j = 0; j< diaryContents.get(position).imageUri.size();j++){
                System.out.println("링크 " + j +":" + diaryContents.get(position).imageUri.get(j));
            }
            daysHolder.vp_images.setAdapter(diaryViewpageAdapter);
       //     daysHolder.iv_tempV.setImageDrawable(null);
//            StorageReference storageReference = storage.getReference().child(diaryContents.get(position).imageUri);
//            System.out.print(diaryContents.get(position).imageUri);
//            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//
//                    Glide.with(daysHolder.itemView.getContext()).load(uri.toString()).into(daysHolder.iv_tempV);
//                    daysHolder.iv_tempV.getLayoutParams().height = (int)(context.getResources().getDisplayMetrics().widthPixels*0.7);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            });

//            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//
//                }
//            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                }
//            });

        }else{
          //  Glide.with(daysHolder.itemView.getContext()).clear(daysHolder.iv_tempV);
//            ViewGroup.LayoutParams temp = daysHolder.iv_tempV.getLayoutParams();
//            temp.height = 100;
//            daysHolder.iv_tempV.setLayoutParams(temp);
            ViewGroup.LayoutParams temp = daysHolder.vp_images.getLayoutParams();
            temp.height = 100;
            daysHolder.vp_images.setLayoutParams(temp);

        }
    }

    @Override
    public int getItemCount() {
        return diaryContents.size();
    }

}
