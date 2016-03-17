package com.bluberry.album.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bluberry.album.R;
import com.bluberry.album.entity.Image;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blueberry on 2016/3/17.
 */
public class AlbumGridViewAdapter extends BaseAdapter {

    private int max = 10;
    private List<Image> images;
    private List<Image> mSelectedIamges = new ArrayList<>();

    private LayoutInflater mInflate;
    private Context context;


    private OnSelectedChangeListener onSelectedChangeListener;

    public AlbumGridViewAdapter(Context context, List<Image> images) {
        this.images = images;
        this.context = context;
        mInflate = LayoutInflater.from(context);
    }

    public void setMax(int max) {
        this.max = max;
    }

   public List<Image> getSelectedIamges(){
       return mSelectedIamges;
   }

    public void setOnSelectedChangeListener(OnSelectedChangeListener l) {
        this.onSelectedChangeListener = l;
    }

    public void changeDataSet(List<Image> images) {
        this.images = images;
    }

    @Override
    public int getCount() {
        if (images == null) return 0;
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = mInflate.inflate(R.layout.item_image_gridview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final View mask = viewHolder.mask;
        final ImageButton checkBox = viewHolder.checkBox;
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectedState(position, mask, checkBox);
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSelectedState(position, mask, checkBox);
            }
        });

        Glide.with((Activity) context)
                .load(images.get(position).path)
                .into(viewHolder.iv);

        if (images.get(position).checked) {
            viewHolder.mask.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setSelected(true);
        } else {
            viewHolder.mask.setVisibility(View.GONE);
            viewHolder.checkBox.setSelected(false);
        }

        return convertView;
    }

    private void changeSelectedState(int position, View mask, ImageButton checkBox) {
        if (!checkOverFlow(position)) return;
        images.get(position).checked = !images.get(position).checked;
        mask.setVisibility(images.get(position).checked ? View.VISIBLE : View.GONE);
        checkBox.setSelected(images.get(position).checked);
        if (images.get(position).checked) {
            if (!mSelectedIamges.contains(images.get(position))) {
                mSelectedIamges.add(images.get(position));
            }
        } else {
            if (mSelectedIamges.contains(images.get(position))) {
                mSelectedIamges.remove(images.get(position));
            }
        }
        if (null != onSelectedChangeListener) {
            onSelectedChangeListener.OnSelectedChange(mSelectedIamges);
        }
    }

    private boolean checkOverFlow(int position) {
        if (!images.get(position).checked) {
            if (mSelectedIamges.size() >= max) {
                if (null != onSelectedChangeListener) {
                    onSelectedChangeListener.onSelectedOverflow(max);
                }
                return false;
            }
        }

        return true;
    }


    private class ViewHolder {
        private ImageView iv;
        private ImageButton checkBox;
        private View mask;

        private ViewHolder(View root) {
            iv = (ImageView) root.findViewById(R.id.image_item_iv);
            checkBox = (ImageButton) root.findViewById(R.id.image_item_check_box);
            mask = root.findViewById(R.id.image_item_mask);
        }
    }

    public interface OnSelectedChangeListener {
        void OnSelectedChange(List<Image> selecteds);

        void onSelectedOverflow(int max);
    }
}
