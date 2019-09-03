package com.example.aran2;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class ViewPageAdapter extends PagerAdapter {
    Context context;
    ArrayList<Uri> images;
    LayoutInflater inflater;

    public  ViewPageAdapter(Context context, ArrayList<Uri> images){
       super();
        this.context = context;
        this.images = images;

    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position){
//        inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.image_viewpager, container, false);
//        ImageView imageView = v.findViewById(R.id.viewPager_image);
//        imageView.setImageURI(images.get(position));
//        System.out.println(images.get(position).getPath());
//        System.out.println("이미지 띄워줌");
//        inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflater.inflate(R.layout.image_viewpager,null);
//        ImageView imageView = v.findViewById(R.id.viewPager_image);
//        imageView.setImageURI(images.get(position));
//        container.addView(v);
        ImageView imageView = new ImageView(context);
        imageView.setImageURI(images.get(position));
        ((ViewPager)container).addView(imageView);
        return imageView;
//        return v;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object o){
        container.removeView((View)o);
        container.invalidate();

    }
}
