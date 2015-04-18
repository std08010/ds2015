package com.acipi.evote.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.acipi.evote.R;
import com.acipi.evote.data.NavigationItem;

import java.util.ArrayList;

public class NavigationListAdapter extends BaseAdapter
{
    private Activity mActivity;
    private ArrayList<NavigationItem> navDrawerItems;
    ViewHolder holder = null;
    private LayoutInflater mInflater;

    public NavigationListAdapter(Activity mActivity, ArrayList<NavigationItem> navDrawerItems)
    {
        this.mActivity = mActivity;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount()
    {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (mInflater == null)
        {
            mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(navDrawerItems.get(position).getIcon());
        holder.title.setText(navDrawerItems.get(position).getTitle());
        return convertView;
    }

    private class ViewHolder
    {
        TextView title;
        ImageView icon;

        ViewHolder(View v)
        {
            this.icon = (ImageView) v.findViewById(R.id.drawerListItemIcon);
            this.title = (TextView) v.findViewById(R.id.drawerListItem);
        }
    }
}
