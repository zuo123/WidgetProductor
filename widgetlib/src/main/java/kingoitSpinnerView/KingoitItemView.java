package kingoitSpinnerView;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingoit.widgetlib.R;

import java.util.List;

/**
 * 适用于左边是等宽度的字体   右边是可以点击弹出下拉菜单的布局
 * 原用于外业调绘2.0  3.0
 * Created by zyh on 2018/7/16.
 */

public class KingoitItemView extends LinearLayout implements SelectItemAdapter.ISelectItemAdapterListener {

    private TextView mLeftTextView;
    private TextView mRightTextView;
    private EditText mRightEditView;
    private int currentStyle = Integer.MIN_VALUE;
    private IKingoitItemViewListener mListener;
    private SelectItemAdapter mSelectItemAdapter;

    interface ViewType {
        int TextStyle = 1;
        int SpinnerStyle = 2;
        int EditStyle = 3;
        String TextStyle_Name = "TextStyle";
        String SpinnerStyle_Name = "SpinnerStyle";
        String EditStyle_Name = "EditStyle";
    }

    public KingoitItemView(Context context) {
        super(context);
    }

    public KingoitItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_attribute_spinner, this);
        mLeftTextView = findViewById(R.id.tv_attr_key);
        mRightTextView = findViewById(R.id.tv_attr_value);
        mRightEditView = findViewById(R.id.tv_attr_value_edittext);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.KingoitItemView);
            currentStyle = typeFilter(typedArray.getString(R.styleable.KingoitItemView_viewType));
            mLeftTextView.setText(typedArray.getString(R.styleable.KingoitItemView_leftText));
            mRightTextView.setText(typedArray.getString(R.styleable.KingoitItemView_rightText));
            typedArray.recycle();
        }
        initView();
    }

    public KingoitItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {
        switch (currentStyle) {
            case ViewType.TextStyle:
                mRightTextView.setVisibility(VISIBLE);
                mRightEditView.setVisibility(GONE);
                break;
            case ViewType.SpinnerStyle:
                mRightTextView.setVisibility(VISIBLE);
                mRightEditView.setVisibility(GONE);
                initAdapter();
                break;
            case ViewType.EditStyle:
                mRightTextView.setVisibility(GONE);
                mRightEditView.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    private void initAdapter() {
        if (mSelectItemAdapter == null) {
            mSelectItemAdapter = new SelectItemAdapter(getContext());
            mSelectItemAdapter.setISelectItemAdapterListener(this);
        }
    }

    /**
     * 类型检查  必填  不填的话会直接抛运行异常
     * 填写到xml里面  具体类型在下面interface
     *
     * @param type
     * @return
     */
    private int typeFilter(String type) {
        int viewType = Integer.MIN_VALUE;

        if (TextUtils.isEmpty(type)) {
            throw new RuntimeException("have you input a correct viewType ? input a viewType and check your viewType is lawful");
        }

        if (ViewType.TextStyle_Name.equals(type)) {
            viewType = ViewType.TextStyle;
        } else if (ViewType.SpinnerStyle_Name.equals(type)) {
            viewType = ViewType.SpinnerStyle;
        } else if (ViewType.EditStyle_Name.equals(type)) {
            viewType = ViewType.EditStyle;
        }

        if (viewType == Integer.MIN_VALUE) {
            throw new RuntimeException("have you input a correct viewType ? input a viewType and check your viewType is lawful");
        } else {
            return viewType;
        }
    }

    public void setList(List<String> list) {
        if (mSelectItemAdapter == null) {
            throw new RuntimeException("the current viewType isn't SpinnerStyle , check your viewType");
        }
        mSelectItemAdapter.setList(list);
    }

    @Override
    public void ISelectItemAdapterListener_onItemClick(String text) {
        mRightTextView.setText(text);
        if (null != mListener) {
            mListener.IKingoitItemViewListener_onSpinnerClick(text);
        }
    }

    public void setIKingoitItemViewListener(IKingoitItemViewListener listener) {
        this.mListener = listener;
    }

    public interface IKingoitItemViewListener {
        void IKingoitItemViewListener_onSpinnerClick(String text);
    }
}
