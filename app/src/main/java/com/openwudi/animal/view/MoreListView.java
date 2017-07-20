package com.openwudi.animal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListView;

/**
 * Created by diwu on 16/9/5.
 */
public class MoreListView extends ListView {

    private boolean isLoadingMore = false;
    private boolean isLoadMoreEnable = false;
    private OnLoadMoreListener onLoadMoreListener;

    private OnScrollListenerProxy onScrollListenerProxy;

    public MoreListView(Context context) {
        super(context);
        init();
    }

    public MoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        onScrollListenerProxy = new OnScrollListenerProxy();
        super.setOnScrollListener(onScrollListenerProxy);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        onScrollListenerProxy.setOnScrollListener(l);
    }

    /**
     * 滑动代理，用于控制加载更多的操作
     */
    private class OnScrollListenerProxy implements OnScrollListener {
        private long mLastLoadMoreTime;
        private OnScrollListener onScrollListener;

        public OnScrollListenerProxy() {
        }

        public OnScrollListenerProxy(OnScrollListener onScrollListener) {
            this.onScrollListener = onScrollListener;
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (onScrollListener != null) {
                this.onScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (isLastThreeItemVisible(view) && !isLoadingMore && onLoadMoreListener != null && isLoadMoreEnable) {
                long time = System.currentTimeMillis();
                if (time - mLastLoadMoreTime > 200 && view.getAdapter().getCount() > 19) {
                    //最少19条数据，并且间隔超过200ms，才能加载更多功能
                    isLoadingMore = true;
                    onLoadMoreListener.onLoadMore();
                    mLastLoadMoreTime = time;
                } else if (view != null && view.getAdapter().getCount() < 19) {
                    onLoadMoreDisable();
                } else {
                    //稍后加载
                }
            }
            if (onScrollListener != null) {
                this.onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }

        protected boolean isLastItemVisible(AbsListView mLv) {
            final Adapter adapter = mLv.getAdapter();

            if (null == adapter || adapter.isEmpty()) {
                return true;
            }

            final int lastItemPosition = adapter.getCount() - 1;
            final int lastVisiblePosition = mLv.getLastVisiblePosition();

            /**
             * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
             * internally uses a FooterView which messes the positions up. For me we'll just subtract
             * one to account for it and rely on the inner condition which checks getBottom().
             */
            if (lastVisiblePosition >= lastItemPosition - 1) {
                final int childIndex = lastVisiblePosition - mLv.getFirstVisiblePosition();
                final int childCount = mLv.getChildCount();
                final int index = Math.min(childIndex, childCount - 1);
                final View lastVisibleChild = mLv.getChildAt(index);
                if (lastVisibleChild != null) {
//                return lastVisibleChild.getBottom() <= mLv.getBottom();
                    return true;
                }
            }

            return false;
        }

        protected boolean isLastThreeItemVisible(AbsListView mLv) {
            final Adapter adapter = mLv.getAdapter();

            if (null == adapter || adapter.isEmpty()) {
                return false;
            }

            final int lastItemPosition = adapter.getCount() - 4;
            final int lastVisiblePosition = mLv.getLastVisiblePosition();

            /**
             * This check should really just be: lastVisiblePosition == lastItemPosition, but ListView
             * internally uses a FooterView which messes the positions up. For me we'll just subtract
             * one to account for it and rely on the inner condition which checks getBottom().
             */
            if (lastVisiblePosition >= lastItemPosition - 1) {
                final int childIndex = lastVisiblePosition - mLv.getFirstVisiblePosition();
                final int childCount = mLv.getChildCount();
                final int index = Math.min(childIndex, childCount - 1);
                final View lastVisibleChild = mLv.getChildAt(index);
                if (lastVisibleChild != null) {
//                return lastVisibleChild.getBottom() <= mLv.getBottom();
                    return true;
                }
            }

            return false;
        }

        public void setOnScrollListener(OnScrollListener onScrollListener) {
            this.onScrollListener = onScrollListener;
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        isLoadMoreEnable = true;
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void onLoadMoreDisable() {
        isLoadMoreEnable = false;
    }

    public void onLoadMoreEnable() {
        isLoadMoreEnable = true;
    }

    public void onLoadMoreComplete() {
        if (isLoadingMore) {
            isLoadingMore = false;
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
