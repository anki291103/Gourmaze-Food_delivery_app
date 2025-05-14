package com.example.fooddeliveryapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class BannerAdapter extends PagerAdapter {

    private Context context;
    private int[] bannerImages;

    public BannerAdapter(Context context, int[] bannerImages) {
        this.context = context;
        this.bannerImages = bannerImages;
    }

    @Override
    public int getCount() {
        return bannerImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, container, false);
        ImageView imageView = view.findViewById(R.id.bannerImage);
        imageView.setImageResource(bannerImages[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
