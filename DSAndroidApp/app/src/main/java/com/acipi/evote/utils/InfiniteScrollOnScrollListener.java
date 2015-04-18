package com.acipi.evote.utils;

import android.widget.AbsListView;

public class InfiniteScrollOnScrollListener implements AbsListView.OnScrollListener
{
    private static final int SCROLL_OFFSET = 2;
    private InfiniteScrollListener listener;

    public InfiniteScrollOnScrollListener(InfiniteScrollListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i)
    {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if (totalItemCount - (firstVisibleItem + 1 + visibleItemCount) < SCROLL_OFFSET && visibleItemCount < totalItemCount)
        {
            listener.endIsNear();
        }
    }

    public void checkForFetchMore(final AbsListView listView)
    {
        Runnable fetchMore = new Runnable()
        {
            @Override
            public void run()
            {
                int last = listView.getLastVisiblePosition();
                if (listView.getChildAt(last) != null)
                {
                    int bottom = listView.getChildAt(last).getBottom();
                    int count = listView.getCount();
                    int height = listView.getHeight();
                    if (last == count - 1 && bottom <= height)
                    {
                        listener.endIsNear();
                    }
                }
            }
        };
        listView.post(fetchMore);
    }
}
