package com.openwudi.animal.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.openwudi.animal.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by long on 2016/12/12.
 */
public class AlertDialogFragment extends DialogFragment {

    private AlertParams mAlertParams;

    /**
     * button click listener
     */
    public interface OnClickListener {
        void onClick(View view);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels - ConvertUtils.dp2px(getActivity(), 105)), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    /**
     * listview item click listener
     */
    public interface OnItemClickListener {
        void onItemClick(String first, String second, int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mAlertParams == null) {
            return null;
        }
        if (mAlertParams.mViewLayoutResId > 0) {
             /* set custom layout  */
            return inflater.inflate(mAlertParams.mViewLayoutResId, container);
        }
        View root = inflater.inflate(R.layout.fragment_alert_dialog, container);
        View topSpace = root.findViewById(R.id.top_space);
        View bottomSpace = root.findViewById(R.id.bottom_space);
        /* set up listview*/
        if (mAlertParams.mItemsTitleList != null && mAlertParams.mItemsSchemeList != null
                && mAlertParams.mItemsTitleList.size() > 0 && mAlertParams.mItemsSchemeList.size() > 0
                && mAlertParams.mItemsTitleList.size() == mAlertParams.mItemsSchemeList.size()) {
            ListView sheetLv = (ListView) root.findViewById(R.id.sheet_lv);
            sheetLv.setVisibility(View.VISIBLE);
            if (mAlertParams.mAdapter == null) {
                if (mAlertParams.mListResId <= 0) {
                    mAlertParams.mAdapter = new ArrayAdapter<>(mAlertParams.mContext, android.R.layout.simple_list_item_1, mAlertParams.mItemsTitleList);
                } else {
                    mAlertParams.mAdapter = new ArrayAdapter<>(mAlertParams.mContext, mAlertParams.mListResId, mAlertParams.mItemsTitleList);
                }
            }
            sheetLv.setAdapter(mAlertParams.mAdapter);
            sheetLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!mAlertParams.mDismissible) {
                        dismiss();
                    }
                    if (mAlertParams.mOnItemClickListener != null) {
                        String first = mAlertParams.mItemsTitleList.get(i);
                        String second = mAlertParams.mItemsSchemeList.get(i);
                        mAlertParams.mOnItemClickListener.onItemClick(first, second, i);
                    }
                }
            });
            topSpace.setVisibility(View.GONE);
            bottomSpace.setVisibility(View.GONE);
            return root;
        }
        /* set title */
        if (!TextUtils.isEmpty(mAlertParams.mTitle)) {
            TextView titleTv = (TextView) root.findViewById(R.id.title_tv);
            titleTv.setVisibility(View.VISIBLE);
            if (mAlertParams.mTitleTextFace != null) {
                titleTv.setTypeface(mAlertParams.mTitleTextFace);
            }
            titleTv.setText(mAlertParams.mTitle);
            topSpace.setVisibility(View.VISIBLE);
            bottomSpace.setVisibility(View.VISIBLE);
            if (mAlertParams.mTitleImageResId > 0) {
                Drawable drawable = getResources().getDrawable(mAlertParams.mTitleImageResId);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                titleTv.setCompoundDrawables(drawable, null, null, null);
                mAlertParams.mTitleDrawPadding = mAlertParams.mTitleDrawPadding <= 0 ? 10 : mAlertParams.mTitleDrawPadding;
                titleTv.setCompoundDrawablePadding(ConvertUtils.dp2px(mAlertParams.mContext, mAlertParams.mTitleDrawPadding));
            }

        }
        /* set message */
        if (!TextUtils.isEmpty(mAlertParams.mMessage)) {
            TextView messageTv = (TextView) root.findViewById(R.id.message_tv);
            messageTv.setVisibility(View.VISIBLE);
            messageTv.setText(mAlertParams.mMessage);
            topSpace.setVisibility(View.VISIBLE);
            bottomSpace.setVisibility(View.VISIBLE);
        }
        /* add custom view to content linearlayout */
        if (mAlertParams.mViews.size() > 0) {
            LinearLayout contentLl = (LinearLayout) root.findViewById(R.id.content_ll);
            contentLl.setVisibility(View.VISIBLE);
            for (View view : mAlertParams.mViews) {
                if (view != null) {
                    contentLl.addView(view);
                }
            }
            topSpace.setVisibility(View.VISIBLE);
            bottomSpace.setVisibility(View.VISIBLE);
        }
        /* add positive button listener */
        if (!TextUtils.isEmpty(mAlertParams.mPositiveButtonText)) {
            TextView positiveBt = (TextView) root.findViewById(R.id.positive_bt);
            positiveBt.setVisibility(View.VISIBLE);
            positiveBt.setText(mAlertParams.mPositiveButtonText);
            if (mAlertParams.mPositiveButtonTextFace != null) {
                positiveBt.setTypeface(mAlertParams.mPositiveButtonTextFace);
            }
            positiveBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mAlertParams.mDismissible) {
                        dismiss();
                    }
                    if (null != mAlertParams.mPositiveButtonListener) {
                        mAlertParams.mPositiveButtonListener.onClick(view);
                    }
                }
            });
        }
         /* add negative button listener */
        if (!TextUtils.isEmpty(mAlertParams.mNegativeButtonText)) {
            Button negativeBt = (Button) root.findViewById(R.id.negative_bt);
            negativeBt.setVisibility(View.VISIBLE);
            negativeBt.setText(mAlertParams.mNegativeButtonText);
            negativeBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mAlertParams.mDismissible) {
                        dismiss();
                    }
                    if (null != mAlertParams.mNegativeButtonListener) {
                        mAlertParams.mNegativeButtonListener.onClick(view);
                    }
                }
            });
        }
        if (root.findViewById(R.id.positive_bt).getVisibility() == View.VISIBLE
                && root.findViewById(R.id.negative_bt).getVisibility() == View.VISIBLE) {
            root.findViewById(R.id.button_divider).setVisibility(View.VISIBLE);
        } else {
            root.findViewById(R.id.button_divider).setVisibility(View.GONE);
        }
        setBackground();
        super.setCancelable(mAlertParams.mCancelable);
        return root;
    }

    public static class Builder {
        private AlertParams P;

        public Builder(Context context, FragmentManager manager) {
            P = new AlertParams(context);
            P.mFragmentManager = manager;
        }

        public Builder setTitle(@StringRes int titleId) {
            P.mTitle = P.mContext.getText(titleId).toString();
            return this;
        }

        public Builder setTag(String tag) {
            P.mTag = tag;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }
        public Builder setTitle(CharSequence title,Typeface typeface) {
            P.mTitle = title;
            P.mTitleTextFace = typeface;
            return this;
        }

        public Builder setTitleResId(int titleResId) {
            P.mTitleImageResId = titleResId;
            return this;
        }

        public Builder setTitleDrawPadding(int titlePadding) {
            P.mTitleDrawPadding = titlePadding;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            P.mMessage = P.mContext.getText(messageId).toString();
            return this;
        }

        public Builder setMessage(CharSequence message) {
            P.mMessage = message;
            return this;
        }

        public Builder setCustomLayout(int layoutResId) {
            P.mViewLayoutResId = layoutResId;
            return this;
        }

        public Builder addContentView(View view) {
            P.mViews.add(view);
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, final OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId).toString();
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(String text, final OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, Typeface typeface, final OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId).toString();
            P.mPositiveButtonTextFace = typeface;
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(String text, Typeface typeface, final OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonTextFace = typeface;
            P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(@StringRes int textId, final OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId).toString();
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(String text, final OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setItems(List<Pair<String, String>> pairItemsList, final OnItemClickListener listener) {
            P.mItemsTitleList = new ArrayList<>();
            P.mItemsSchemeList = new ArrayList<>();
            for (Pair<String, String> pair : pairItemsList) {
                P.mItemsTitleList.add(pair.first);
                P.mItemsSchemeList.add(pair.second);
            }
            P.mOnItemClickListener = listener;
            return this;
        }

        public Builder setItems(List<Pair<String, String>> pairItemsList, @LayoutRes int itemRes, final OnItemClickListener listener) {
            P.mItemsTitleList = new ArrayList<>();
            P.mItemsSchemeList = new ArrayList<>();
            for (Pair<String, String> pair : pairItemsList) {
                P.mItemsTitleList.add(pair.first);
                P.mItemsSchemeList.add(pair.second);
            }
            P.mListResId = itemRes;
            P.mOnItemClickListener = listener;
            return this;
        }

        public Builder setItems(List<Pair<String, String>> pairItemsList, ListAdapter adapter, final OnItemClickListener listener) {
            P.mItemsTitleList = new ArrayList<>();
            P.mItemsSchemeList = new ArrayList<>();
            for (Pair<String, String> pair : pairItemsList) {
                P.mItemsTitleList.add(pair.first);
                P.mItemsSchemeList.add(pair.second);
            }
            P.mAdapter = adapter;
            P.mOnItemClickListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setDismissible(boolean dismissible) {
            P.mDismissible = dismissible;
            return this;
        }

        public void show() {
            if (P == null || P.mContext == null) {
                return;
            }
            if (P.mContext instanceof Activity && ((Activity) P.mContext).isFinishing()) {
                return;
            }
            AlertDialogFragment dialogFragment = new AlertDialogFragment();
            P.show(dialogFragment);
        }
    }

    public void setAlertParams(AlertParams alertParams) {
        this.mAlertParams = alertParams;
    }

    private static class AlertParams {
        final Context mContext;

        private FragmentManager mFragmentManager;
        private String mTag;

        private int mViewLayoutResId;

        private CharSequence mTitle;
        public Typeface mTitleTextFace;
        private int mTitleImageResId;
        private int mTitleDrawPadding;
        private CharSequence mMessage;

        private List<View> mViews;

        private String mPositiveButtonText;
        private String mNegativeButtonText;
        private OnClickListener mPositiveButtonListener;
        private OnClickListener mNegativeButtonListener;

        public Typeface mPositiveButtonTextFace;

        private List<String> mItemsTitleList;
        private List<String> mItemsSchemeList;

        private ListAdapter mAdapter;
        private int mListResId;
        private OnItemClickListener mOnItemClickListener;

        private boolean mCancelable;
        private boolean mDismissible;

        AlertParams(Context context) {
            this.mContext = context;
            this.mCancelable = true;
            this.mDismissible = false;
            this.mViews = new ArrayList<>();
            this.mTag = "dialog";
        }

        private void show(AlertDialogFragment dialogFragment) {
            dialogFragment.setAlertParams(this);
            FragmentTransaction transaction = this.mFragmentManager.beginTransaction();
            transaction.add(dialogFragment, this.mTag);
            transaction.commitAllowingStateLoss();
            transaction.show(dialogFragment);
        }
    }

    private void setBackground() {
        int roundRadius = dp2px(getContext(), 8f);
        int fillColor = Color.parseColor("#FFFFFF");
        GradientDrawable bg = new GradientDrawable();//创建drawable
        bg.setColor(fillColor);
        bg.setCornerRadius(roundRadius);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(bg);
        }
    }

    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public interface AlertOnClickListener {
        void onClick(View view, String model);
    }
}
