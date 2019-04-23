package com.openwudi.animal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.openwudi.animal.R;


/**
 * Created by haoyonglong on 2016/12/15.
 * 说明：
 */
public class TableCellView extends RelativeLayout {
    //右侧没有图标，不可输入
    public static final int TYPE_NORMAL = 0;
    //右侧有图标，不可输入
    public static final int TYPE_INDICATOR = 1;
    //可输入状态
    public static final int TYPE_INPUT = 2;
    //可输入状态，右侧有图标
    public static final int TYPE_INPUT_INDICATOR = 3;
    private ImageView mRightIv;
    private TextView mTitleTv;
    private TextView mRightTv;
    private View mRoot;
    private boolean isNotShowDeleteIcon;

    public boolean isNotShowDeleteIcon() {
        return isNotShowDeleteIcon;
    }

    public void setNotShowDeleteIcon(boolean notShowDeleteIcon) {
        isNotShowDeleteIcon = notShowDeleteIcon;
        if (mInputEt != null) {
            mInputEt.setNotShowDelICon(isNotShowDeleteIcon);
        }
    }

    public EditText getmInputEt() {
        return mInputEt;
    }


    private ClearEditText mInputEt;

    private int mCurrentType;

    public TableCellView(Context context) {
        this(context, null);
    }

    public TableCellView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        initAttrs(attrs);
    }


    private void initView() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.table_cell_view, this);
        mRoot = root.findViewById(R.id.root);
        mRightIv = (ImageView) root.findViewById(R.id.right_iv);
        mTitleTv = (TextView) root.findViewById(R.id.title_tv);
        mRightTv = (TextView) root.findViewById(R.id.desc_tv);
        mInputEt = (ClearEditText) root.findViewById(R.id.input_et);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TableCellView);

        Drawable rightDrawable = typedArray.getDrawable(R.styleable.TableCellView_tcv_right_drawable);
        if (rightDrawable != null) {
            mRightIv.setImageDrawable(rightDrawable);
        }

        String titleText = typedArray.getString(R.styleable.TableCellView_tcv_title);
        if (!TextUtils.isEmpty(titleText)) {
            mTitleTv.setText(titleText);
        }

        String rightText = typedArray.getString(R.styleable.TableCellView_tcv_right_text);
        if (!TextUtils.isEmpty(rightText)) {
            mInputEt.setText(rightText);
            mRightTv.setText(rightText);
        }
        String rightHintText = typedArray.getString(R.styleable.TableCellView_tcv_right_hint);
        if (!TextUtils.isEmpty(rightHintText)) {
            mInputEt.setHint(rightHintText);
            mRightTv.setHint(rightHintText);
        }

        mCurrentType = typedArray.getInt(R.styleable.TableCellView_tcv_type, TYPE_NORMAL);
        switch (mCurrentType) {
            case TYPE_INPUT:
                mRightIv.setVisibility(GONE);
                mRightTv.setVisibility(GONE);
                mInputEt.setVisibility(VISIBLE);
                break;
            case TYPE_INPUT_INDICATOR:
                mRightIv.setVisibility(VISIBLE);
                mRightTv.setVisibility(GONE);
                mInputEt.setVisibility(VISIBLE);
                break;
            case TYPE_INDICATOR:
                mRightIv.setVisibility(VISIBLE);
                mRightTv.setVisibility(VISIBLE);
                mInputEt.setVisibility(GONE);
                break;
            default:
                mRightIv.setVisibility(GONE);
                mRightTv.setVisibility(VISIBLE);
                mInputEt.setVisibility(GONE);

        }
        typedArray.recycle();
    }

    public void setOnFocusListener(OnFocusChangeListener listener) {
        if (listener != null && mInputEt != null) {
            mInputEt.setOnFocusChangeListener(listener);
        }
    }

    public void setInputType(int inputType) {
        if (mCurrentType == TYPE_INPUT || mCurrentType == TYPE_INPUT_INDICATOR) {
            mInputEt.setInputType(inputType);
        }
    }

    public String getRightText() {
        switch (mCurrentType) {
            case TYPE_INPUT:
            case TYPE_INPUT_INDICATOR:
                return mInputEt.getText().toString().trim();
            default:
                return mRightTv.getText().toString().trim();
        }
    }

    //不去掉空格的字符串
    public String getRightRealString() {
        switch (mCurrentType) {
            case TYPE_INPUT:
            case TYPE_INPUT_INDICATOR:
                return mInputEt.getText().toString();
            default:
                return mRightTv.getText().toString();
        }
    }

    public void setRightText(String text) {
        switch (mCurrentType) {
            case TYPE_INPUT:
            case TYPE_INPUT_INDICATOR:
                mInputEt.setText(text);
                Editable editable = mInputEt.getText();
                Selection.setSelection(editable, editable.length());
                break;
            default:
                mRightTv.setText(text);
        }
    }

    public void setRightEditTextLength(int length) {
        if (length <= 0) {
            return;
        }
        switch (mCurrentType) {
            case TYPE_INPUT:
            case TYPE_INPUT_INDICATOR:
                mInputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)}); //最大输入长度
                break;
            case TYPE_NORMAL:
                break;
            case TYPE_INDICATOR:
                break;
            default:
                break;
        }
    }

    public void setRightTextColor(@ColorRes int color) {
        switch (mCurrentType) {
            case TYPE_INPUT:
                mInputEt.setTextColor(ContextCompat.getColorStateList(getContext(), color));
                break;
            default:
                mRightTv.setTextColor(ContextCompat.getColorStateList(getContext(), color));
        }
    }

    public void setTitleColor(@ColorRes int color) {
        mTitleTv.setTextColor(ContextCompat.getColorStateList(getContext(), color));

    }

    public void setRightHint(String text) {
        switch (mCurrentType) {
            case TYPE_INPUT:
                mInputEt.setHint(text);
                break;
            default:
                mRightTv.setHint(text);
        }
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title) && mTitleTv != null) {
            mTitleTv.setText(title);
        }
    }

    public void setCellType(int type) {
        if (type < TYPE_NORMAL) {
            mCurrentType = TYPE_NORMAL;
        } else if (type > TYPE_INPUT_INDICATOR) {
            mCurrentType = TYPE_INPUT_INDICATOR;
        } else {
            mCurrentType = type;
        }
        if (mCurrentType == TYPE_NORMAL) {
            mRightIv.setVisibility(GONE);
            mInputEt.setVisibility(GONE);
            mRightTv.setVisibility(VISIBLE);
        } else if (mCurrentType == TYPE_INPUT) {
            mInputEt.setVisibility(VISIBLE);
            mRightTv.setVisibility(GONE);
            mRightIv.setVisibility(GONE);
        } else if (mCurrentType == TYPE_INPUT_INDICATOR) {
            mRightIv.setVisibility(VISIBLE);
            mInputEt.setVisibility(VISIBLE);
            mRightTv.setVisibility(GONE);
        } else {
            mRightIv.setVisibility(VISIBLE);
            mInputEt.setVisibility(GONE);
            mRightTv.setVisibility(VISIBLE);
        }
    }

    public void setInputMaxLength(int maxLength) {
        if (maxLength > 0) {
            mInputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        }
    }

    public void setRightDrawable(@DrawableRes int resId) {
        if (resId != 0) {
            mRightIv.setImageDrawable(getResources().getDrawable(resId));
            mRightIv.setVisibility(VISIBLE);
        } else {
            mRightIv.setVisibility(GONE);
        }
    }

    public void setBackgroundDrawable(@DrawableRes int resId) {
        mRoot.setBackgroundDrawable(getResources().getDrawable(resId));
    }

    public void setInputEnable(boolean enable) {
        if (!enable) {
            mInputEt.clearFocus();
        } else {
            mInputEt.requestFocus();
        }
        mInputEt.setEnabled(enable);
    }

    public void clearFocus() {
        mInputEt.clearFocus();
    }

    public int getCellType() {
        return mCurrentType;
    }
}
