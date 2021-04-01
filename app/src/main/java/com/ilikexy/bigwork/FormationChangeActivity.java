package com.ilikexy.bigwork;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.baseactivity.ToastAction;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.netaction.SelectAction;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class FormationChangeActivity extends BaseActivity implements View.OnClickListener{
    //对于这个界面的父类控件进行声明，昵称，年龄，家乡，所在地，个性签名
    private LinearLayout mLinearFakeName,mLinearAge,mLinearHometown,mLinearAddress,mLinearSignature,mLinearSex;
    //编辑器
    private EditText mEditFakeName,mEditAge,mEditHometown,mEditAddress,mEditSignature;
    //同时还有返回text， 保存按钮,男女选择点击的text,返回键上方显示内容的textview
    private TextView mTextBack,mTextWoman,mTextMan,mTextTitle;
    //保存按钮
    private Button mBtnSave;
    //选了哪个控件
    private LinearLayout mChosedLinear;
    private EditText mChosedEdit;
    private String mtitleer = "性别";//选中控件的名
    private String mtitleEnglish="";//英文
    private String msexname = "男";//性别名
    //保存按钮是否可点击
    private boolean isCanClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formation_change);
        StatusBarUtil.setColor(FormationChangeActivity.this, Color.parseColor("#ededed"),0);
        StatusBarUtil.setLightMode(FormationChangeActivity.this);
        init();
        getWhichKongjian();
    }

    //控件初始化，根据上一个活动传来的数据决定哪一个控件显示
    public void init(){
        //父类控件初始化
        mLinearFakeName = (LinearLayout) findViewById(R.id.linear_fakename_forchange);
        mLinearAge = (LinearLayout)findViewById(R.id.linear_age_forchange);
        mLinearHometown = (LinearLayout)findViewById(R.id.linear_hometown_forchange);
        mLinearAddress = (LinearLayout)findViewById(R.id.linear_address_forchange);
        mLinearSignature = (LinearLayout)findViewById(R.id.linear_signature_forchange);
        mLinearSex = (LinearLayout)findViewById(R.id.linear_sex_forchange);
        //子类编辑器控件初始化
        mEditFakeName = (EditText)findViewById(R.id.edit_fakename_forchange);
        mEditAge = (EditText)findViewById(R.id.edit_age_forchange);
        mEditHometown = (EditText)findViewById(R.id.edit_hometown_forchange);
        mEditAddress = (EditText)findViewById(R.id.edit_address_forchange);
        mEditSignature = (EditText)findViewById(R.id.edit_signature_forchange);
        //返回 text view， 标题textview，男性，女性text
        mTextBack = (TextView)findViewById(R.id.text_back_formachange);
        mTextTitle = (TextView)findViewById(R.id.text_title_formachange);
        mTextMan = (TextView)findViewById(R.id.text_sexman_forchange);
        mTextWoman = (TextView)findViewById(R.id.text_sexwoman_forchange);
        mTextBack.setOnClickListener(this);
        mTextMan.setOnClickListener(this);
        mTextWoman.setOnClickListener(this);
        //保存按钮
        mBtnSave = (Button) findViewById(R.id.btn_save_formachange);
        mBtnSave.setOnClickListener(this);
    }
    //我们封装一下显示控件的方法
    public void showKongjian(LinearLayout c_linear,final EditText c_edit,String c_title, String c_data){
        c_linear.setVisibility(View.VISIBLE);//设置为可见
       // c_edit.setLayerType(View.LAYER_TYPE_HARDWARE,null); //获得焦点
        c_edit.setText(c_data);//设置输入框的内容
        mChosedLinear = c_linear;//被选中的父类控件
        mChosedEdit = c_edit; //被选中的子类控件
        mtitleer = c_title;//被选中控件名
        mTextTitle.setText("更改"+c_title);//设置标题内容

        //当输入框有文件输入时，且不为空，button设置为绿色，字体为白色
        c_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tmpget = c_edit.getText().toString();
                if (!tmpget.equals("")){//不为空
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBtnSave.setBackgroundColor(Color.parseColor("#03c45c"));
                            mBtnSave.setTextColor(Color.parseColor("#ffffff"));
                            isCanClick = true;
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBtnSave.setBackgroundColor(Color.parseColor("#ededed"));
                            mBtnSave.setTextColor(Color.parseColor("#6d6d6d"));
                            isCanClick =false;
                        }
                    });
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    //从上一个活动中获取需要显示哪一个活动
    public void getWhichKongjian(){
        Intent intent = getIntent();
        String getStringer1 = intent.getStringExtra("name");
        String getStringer2 = intent.getStringExtra("data");
        switch (getStringer1){
            case "昵称":
                showKongjian(mLinearFakeName,mEditFakeName,getStringer1,getStringer2);
                mtitleEnglish = "fakename";
                break;
            case "性别":
                mTextTitle.setText("更改性别");
                mLinearSex.setVisibility(View.VISIBLE);
                mtitleEnglish = "sex";
                msexname = getStringer2;
                if (getStringer2.equals("男")){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLinearSex.setBackground(getDrawable(R.drawable.man));

                        }
                    });
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mLinearSex.setBackground(getDrawable(R.drawable.woman));

                        }
                    });
                }
                break;
            case "年龄":
                mtitleEnglish = "age";
                showKongjian(mLinearAge,mEditAge,getStringer1,getStringer2);
                break;
            case "家乡":
                mtitleEnglish = "hometown";
                showKongjian(mLinearHometown,mEditHometown,getStringer1,getStringer2);
                break;
            case "所在地":
                mtitleEnglish = "address";
                showKongjian(mLinearAddress,mEditAddress,getStringer1,getStringer2);
                break;
            case "个性签名":
                mtitleEnglish = "signature";
                showKongjian(mLinearSignature,mEditSignature,getStringer1,getStringer2);
                break;
            default:
                break;
        }
    }
    //点击事件处理
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_sexman_forchange://当点击男性别
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLinearSex.setBackground(getDrawable(R.drawable.man));//更换图片
                        mBtnSave.setBackgroundColor(Color.parseColor("#03c45c"));//保存按钮颜色
                        mBtnSave.setTextColor(Color.parseColor("#ffffff"));
                        msexname = "男";
                        isCanClick = true;
                    }
                });
                break;
            case R.id.text_sexwoman_forchange://当点击女性别时
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLinearSex.setBackground(getDrawable(R.drawable.woman));
                        mBtnSave.setBackgroundColor(Color.parseColor("#03c45c"));
                        mBtnSave.setTextColor(Color.parseColor("#ffffff"));
                        msexname = "女";
                        isCanClick = true;
                    }
                });
                break;
            case R.id.text_back_formachange:
                onBackPressed();
                break;
            case R.id.btn_save_formachange://当点击保存信息按钮时，判定是否可点击
                if(isCanClick){//可保存
                    //获取这个控件的新信息, 非按钮
                     String getdataer="";
                    if (mtitleer.equals("性别")){//性别选择
                        getdataer = msexname;
                    }else{
                        getdataer = mChosedEdit.getText().toString();
                    }
                    //启动网络请求，对于信息表的修改（包括了动画，请求结果的处理）
                    final String finalGetdataer = getdataer;
                    final String finalGetdataer1 = getdataer;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Call call = new SelectAction().updateFormation(MainFunctionActivity.usename,mtitleEnglish, finalGetdataer1);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call,IOException e) {

                                }
                                @Override
                                public void onResponse(Call call,Response response) throws IOException {
                                    Looper.prepare();
                                    String getdata = response.body().string();
                                    if (getdata.equals("upadate Success!!!")){//修改信息成功
                                        ToastAction.startToast(FormationChangeActivity.this,
                                                "更改"+mtitleer+"成功！");
                                        SharedPreferences.Editor editor = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS,MODE_PRIVATE).edit();
                                        editor.putString(mtitleEnglish, finalGetdataer);
                                        editor.commit();
                                        //调用返回函数

                                    }else {
                                        ToastAction.startToast(FormationChangeActivity.this,
                                                "更改"+mtitleer+"失败！");
                                    }
                                    Looper.loop();
                                }
                            });
                        }
                    }).start();
                    onBackPressed();
                }
                break;
            default:
                break;
        }
    }


}