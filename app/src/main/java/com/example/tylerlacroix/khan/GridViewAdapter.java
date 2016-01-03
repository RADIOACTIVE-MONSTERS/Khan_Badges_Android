package com.example.tylerlacroix.khan;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

/**
 * Created by Tyler on 15-12-27.
 */
public class GridViewAdapter extends ArrayAdapter<Badge> implements ServerData.ServerDataDelegate {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Badge> mGridData = new ArrayList<Badge>();

    public GridViewAdapter(Context mContext, int layoutResourceId, ArrayList<Badge> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;

        ServerData.loadData(this);
    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Badge> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            holder.pointNumber = (TextView) row.findViewById(R.id.grid_item_point);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        try {
            holder.badge = mGridData.get(position);
            holder.titleTextView.setText(holder.badge.descript);
            if (holder.badge.points != 0) {
                holder.pointNumber.setText(holder.badge.points + "");
            }
            else {
                holder.pointNumber.setText("");
            }
            Picasso.with(mContext).load(holder.badge.icon).into(holder.imageView);
        }
        catch( Exception e) {
            e.printStackTrace();
        }
        return row;
    }

    @Override
    public void finishedLoading() {
        categoryChanged(-1);
    }

    public void categoryChanged(int category) {
        clear();
        ArrayList<Badge> nBadges = ServerData.getBadges(category);
        for (int i = 0; i < nBadges.size(); i++)
            add(nBadges.get(i));

    }

    static class ViewHolder {
        TextView titleTextView;
        TextView pointNumber;
        ImageView imageView;
        Badge badge;
    }
}
