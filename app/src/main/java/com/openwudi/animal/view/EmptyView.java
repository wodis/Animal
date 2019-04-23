package com.openwudi.animal.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.openwudi.animal.R;
import com.openwudi.animal.exception.AnimalException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by diwu on 17/1/4.
 */

public class EmptyView extends LinearLayout {
    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.desc)
    TextView desc;
    @BindView(R.id.btn)
    TextView btn;
    @BindView(R.id.loading)
    AVLoadingIndicatorView loading;

    public EmptyView(Context context) {
        super(context);
        initView();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setBackgroundColor(getResources().getColor(R.color.color9));
        setOrientation(VERTICAL);
        View.inflate(getContext(), R.layout.view_empty_single, this);
        ButterKnife.bind(this);
        LineSpinFadeLoaderIndicator indicator = new LineSpinFadeLoaderIndicator();
        loading.setIndicator(indicator);
    }

    public void setImage(int drawableId) {
        iv.setImageDrawable(getResources().getDrawable(drawableId));
        iv.setVisibility(VISIBLE);
    }

    public void setText(String text) {
        desc.setText(text);
        desc.setVisibility(VISIBLE);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != null) {
            btn.setOnClickListener(onClickListener);
            btn.setVisibility(VISIBLE);
        } else {
            btn.setVisibility(GONE);
        }
    }

    public void setLoading(boolean isShow) {
        loading.setVisibility(isShow ? VISIBLE : GONE);
        if (!isShow) {
            loading.hide();
        }else {
            loading.show();
        }
    }

    public void reset(){
        setLoading(false);
        btn.setVisibility(GONE);
        iv.setVisibility(GONE);
        desc.setVisibility(GONE);
    }

    /**
     * 工具类
     */
    public static class ViewHelper {
        public static void set(EmptyView view, Exception ex, OnClickListener onClickListener) {
            String text;
            if (ex != null) {
                if (ex instanceof AnimalException) {
                    text = ((AnimalException) ex).getErrorMsg();
                } else {
                    text = ex.getMessage();
                }
                view.setText(text);
                view.setImage(R.drawable.tips_net);
                view.setLoading(false);
                view.setOnClickListener(onClickListener);
            } else {
                text = "空空如也,\n" +
                        "可能是被妖怪抓走了~";
                view.setText(text);
                view.setImage(R.drawable.tips_ghost);
                view.setLoading(false);

                view.setOnClickListener(onClickListener);
            }
        }
    }
}
