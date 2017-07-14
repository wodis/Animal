package com.openwudi.animal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.openwudi.animal.R;
import com.openwudi.animal.utils.Utils;

/**
 * Created by diwu on 16/6/16.
 */
public class TitleBarView extends FrameLayout {
    private View mRoot;
    private View mLeft;
    private ImageView mBtnLeft;
    private ImageView mBtnRight;
    private TextView mBtnRightTv;
    private View mTvCenterLl;
    private TextView mTvTitle;
    private TextView mTvSubTitle;
    private TextView mBtnLeftSub;
    //底部描述
    private TextView mTvDesc;


    public TitleBarView(Context context) {
        super(context);
        initView();
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttrs(context, attrs);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttrs(context, attrs);
    }

    private void initView() {
        mRoot = LayoutInflater.from(getContext()).inflate(R.layout.view_title_bar, this);
        mLeft = mRoot.findViewById(R.id.left);
        mBtnLeft = (ImageView) mRoot.findViewById(R.id.left_btn);
        mBtnRight = (ImageView) mRoot.findViewById(R.id.right_btn);
        //中间文字显示相关
        mTvCenterLl = mRoot.findViewById(R.id.text_ll);
        mTvTitle = (TextView) mRoot.findViewById(R.id.title);
        mTvSubTitle = (TextView) mRoot.findViewById(R.id.subtitle);
        mBtnRightTv = (TextView) mRoot.findViewById(R.id.right_tv);
        mBtnLeftSub = (TextView) mRoot.findViewById(R.id.left_sub_btn);
        mTvDesc = (TextView) mRoot.findViewById(R.id.desc_tv);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);

        Drawable dLeft = a.getDrawable(R.styleable.TitleBarView_tbv_left);
        if (dLeft != null) {
            mBtnLeft.setImageDrawable(dLeft);
        } else {
            mBtnLeft.setVisibility(GONE);
        }

        Drawable dRight = a.getDrawable(R.styleable.TitleBarView_tbv_right);
        if (dRight != null) {
            mBtnRight.setImageDrawable(dRight);
        } else {
            mBtnRight.setVisibility(GONE);
        }

        String sRight = a.getString(R.styleable.TitleBarView_tbv_right_text);
        if (TextUtils.isEmpty(sRight)) {
            mBtnRightTv.setVisibility(GONE);
        } else {
            mBtnRightTv.setText(sRight);
            mBtnRightTv.setVisibility(VISIBLE);
        }

        String title = a.getString(R.styleable.TitleBarView_tbv_title);
        if (title != null) {
            mTvTitle.setText(title);
        }

        int titlec = a.getColor(R.styleable.TitleBarView_tbv_title_color, getResources().getColor(R.color.color6));
        if (titlec != 0) {
            mTvTitle.setTextColor(titlec);
        }

        String subtitle = a.getString(R.styleable.TitleBarView_tbv_subtitle);
        if (subtitle != null) {
            mTvSubTitle.setText(subtitle);
        } else {
            mTvSubTitle.setVisibility(GONE);
        }

        int subtitlec = a.getColor(R.styleable.TitleBarView_tbv_subtitle_color, getResources().getColor(R.color.color6));
        if (subtitlec != 0) {
            mTvSubTitle.setTextColor(subtitlec);
        }

        int bgc = a.getColor(R.styleable.TitleBarView_tbv_bg_color, getResources().getColor(R.color.colorPrimary));
        if (bgc != 0) {
            mRoot.setBackgroundColor(bgc);
        }

        boolean shadow = a.getBoolean(R.styleable.TitleBarView_tbv_shadow, false);
        mRoot.findViewById(R.id.shadow).setVisibility(shadow ? VISIBLE : GONE);

        int titleTextSize = a.getInteger(R.styleable.TitleBarView_tbv_title_text_size, 17);
        mTvTitle.setTextSize(titleTextSize);

        String leftSub = a.getString(R.styleable.TitleBarView_tbv_left_sub_text);
        if (leftSub != null) {
            mRoot.findViewById(R.id.left_sub).setVisibility(VISIBLE);
            mBtnLeftSub.setVisibility(VISIBLE);
            mBtnLeftSub.setText(leftSub);
        } else {
            mRoot.findViewById(R.id.left_sub).setVisibility(GONE);
            mBtnLeftSub.setVisibility(GONE);
        }

        int leftsubc = a.getColor(R.styleable.TitleBarView_tbv_left_sub_color, getResources().getColor(R.color.color6));
        if (leftsubc != 0) {
            mBtnLeftSub.setTextColor(leftsubc);
        }
        String descStr = a.getString(R.styleable.TitleBarView_tbv_desc_text);
        if (!TextUtils.isEmpty(descStr)) {
            mTvDesc.setVisibility(VISIBLE);
            mTvDesc.setText(descStr);
        } else {
            mTvDesc.setVisibility(GONE);
        }
        boolean leftImageVisible = a.getBoolean(R.styleable.TitleBarView_tbv_left_visible, true);
        mLeft.setVisibility(leftImageVisible ? VISIBLE : GONE);
        boolean leftSubVisible = a.getBoolean(R.styleable.TitleBarView_tbv_left_sub_text_visible, false);
        mRoot.findViewById(R.id.left_sub).setVisibility(leftSubVisible ? VISIBLE : GONE);
        a.recycle();
    }

    public void setListener(OnClickListener leftListener, OnClickListener rightListener) {
        if (leftListener != null) {
            mRoot.findViewById(R.id.left).setOnClickListener(leftListener);
        }

        if (rightListener != null) {
            mBtnRight.setOnClickListener(rightListener);
            mBtnRightTv.setOnClickListener(rightListener);
        }
    }

    public void setLeftListener(OnClickListener leftListener) {
        if (leftListener != null) {
            mRoot.findViewById(R.id.left).setOnClickListener(leftListener);
        }
    }

    public void setRightListener(OnClickListener rightListener) {
        if (rightListener != null) {
            mBtnRight.setOnClickListener(rightListener);
            mBtnRightTv.setOnClickListener(rightListener);
        }
    }

    public void setLeftSubListener(OnClickListener leftListener) {
        if (leftListener != null) {
            mRoot.findViewById(R.id.left_sub).setOnClickListener(leftListener);
        }
    }

    public void setTitle(String title) {
        if (mTvTitle != null && !TextUtils.isEmpty(title)) {
            Paint paint = mTvTitle.getPaint();
            float length = paint.measureText(title);
            int moreLength = (int) paint.measureText("...");
            int dp150 = ConvertUtils.dp2px(getContext(), 150);
            int screenWidth = ScreenUtils.getScreenWidth(getContext());
            if (length > dp150) {
                String s = Utils.breakTxts(paint, title, screenWidth - dp150, moreLength);
                mTvTitle.setText(s);
            } else {
                mTvTitle.setText(title);
            }
        }
    }

    public void setLeftSub(String text) {
        if (mBtnLeftSub != null) {
            mBtnLeftSub.setText(text);
        }
    }

    public void setLeftSubVisible(int visible) {
        if (mBtnLeftSub != null) {
            mBtnLeftSub.setVisibility(visible);
        }
    }

    public void setRightVisible(int visible) {
        if (mBtnRightTv != null) {
            mBtnRightTv.setVisibility(visible);
        }
    }

    public void setRightText(String text) {
        if (mBtnRightTv != null) {
            mBtnRightTv.setText(text);
            mBtnRightTv.setVisibility(VISIBLE);
        }
    }

    public void setLeftVisible(int visible) {
        if (mLeft != null) {
            mLeft.setVisibility(visible);
        }
    }

    public void setDescText(String desc) {
        if (!TextUtils.isEmpty(desc)) {
            mTvDesc.setVisibility(VISIBLE);
            mTvDesc.setText(desc);
        } else {
            mTvDesc.setVisibility(GONE);
        }
    }

    public void setRightImage(int imageId) {
        if (imageId > 0 && mBtnRight != null) {
            mBtnRight.setImageResource(imageId);
        }
    }
}
