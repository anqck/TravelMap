package com.n.travelmap.Library.BottomSheetInfomation;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.n.travelmap.R;

import java.util.List;

public class ItemPagerAdapter extends android.support.v4.view.PagerAdapter {

    private List<Bitmap> mList;

    Context mContext;
    LayoutInflater mLayoutInflater;
    ImageView imageView;

    public ItemPagerAdapter(Context context, List<Bitmap> items) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mList = items;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
         imageView = (ImageView) itemView.findViewById(R.id.imageView);

//        Glide.with(mContext).load(dressDetailResult.getImages())
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(imageView);

        imageView.setImageBitmap(mList.get(position));
       // imageView.setImageDrawable(mList.get(position));
        //imageView.setImageResource(mItems[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public void SetImage(Bitmap img)
    {
        imageView.setImageBitmap(img);
        //this.
    }

}
