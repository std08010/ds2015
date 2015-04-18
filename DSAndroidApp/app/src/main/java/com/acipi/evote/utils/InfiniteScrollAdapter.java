package com.acipi.evote.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Collection;

public abstract class InfiniteScrollAdapter extends BaseAdapter
{
    private boolean doneLoading = false;
    private LayoutInflater mInflater;

    public InfiniteScrollAdapter(Activity mActivity)
    {
        this.mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return getItems().size() + (!doneLoading ? 1 : 0);
    }

    @Override
    public Object getItem(int position)
    {
        if (!doneLoading && position == getCount())
        {
            throw new IllegalArgumentException("Can not call getItem on loading placeholder.");
        }
        else
        {
            return getRealItem(position);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (!doneLoading && position >= getItems().size())
        {
            return getLoadingView(mInflater, parent);
        }
        else
        {
            return getRealView(mInflater, position, convertView, parent);
        }
    }

    protected void setDoneLoading()
    {
        doneLoading = true;
    }

    protected void resetDoneLoading()
    {
        doneLoading = false;
    }

    public abstract Collection getItems();

    public abstract void addItems(Collection items, String errorMessage);

    public abstract void resetItems();

    public abstract Object getRealItem(int position);

    public abstract View getRealView(LayoutInflater inflater, int position, View convertView, ViewGroup parent);

    public abstract View getLoadingView(LayoutInflater inflater, ViewGroup parent);
}
