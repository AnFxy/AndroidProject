package com.ilikexy.bigwork;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.ilikexy.bigwork.constant.Constant;
import com.ilikexy.bigwork.entity.MyFormation;
import com.ilikexy.bigwork.entity.Users;
import com.ilikexy.bigwork.media.CaptureClass;
import com.ilikexy.bigwork.netaction.SelectAction;
import com.ilikexy.bigwork.photouri.DcimUriget;
import com.ilikexy.bigwork.photouri.PicCutAndScale;
import com.ilikexy.bigwork.recyclerview.FormaRecyclerAdapter;
import com.ilikexy.bigwork.zidingyi.DialogContainer;
import com.ilikexy.bigwork.zidingyi.RoundedSquare;
import com.jaeger.library.StatusBarUtil;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MineFormationActivity extends BaseActivity implements View.OnClickListener{
    private TextView mTextBack;
    private RoundedSquare mRoundedSquare;
    private Bitmap mbitmap;//????????????
    private LinearLayout mLinear;
    private RecyclerView mRecycler;
    //????????????????????????
    private PaizhaoRecieverOne paizhaoReceiverOne;
    private XiangceRecieverOne xiangceReceiverOne;
    private FormaReciever formaReciever;
    //????????????uri
    private Uri mPhotoUri;
    //????????????????????????
    private String mfakenamer,msex,mage,mhometown,maddress,msignature;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_formation);
        StatusBarUtil.setColor(MineFormationActivity.this, Color.parseColor("#ededed"),0);
        StatusBarUtil.setLightMode(MineFormationActivity.this);
        init();
        broadcastPicture();
    }
    //?????????
    public void init(){
        //?????????????????????
        mfakenamer="??????";
        msex = "???";
        mage = "0";
        mhometown = "?????????";
        maddress = "?????????";
        msignature = "??????????????????????????????";
        //????????????
        IntentFilter intentFilter = new IntentFilter("com.ilikexy.bigwork.forma");
         formaReciever = new FormaReciever();
        registerReceiver(formaReciever,intentFilter);

        mbitmap = BitmapFactory.decodeResource(getResources(),R.drawable.graytouxiang);//??????????????????
        mTextBack = (TextView)findViewById(R.id.text_back_forma);
        mRoundedSquare = (RoundedSquare)findViewById(R.id.imag_formation_activity);
        mLinear = (LinearLayout)findViewById(R.id.linear_formation_activity);
        mRecycler = (RecyclerView)findViewById(R.id.recycler_formation_activity);

        mTextBack.setOnClickListener(this);
        mLinear.setOnClickListener(this);
        //??????????????????????????????sharepreference???????????????mine??????????????????????????????,???????????????????????????share????????????
        Bitmap mbitmap1 = getpictureLocal();
        if (mbitmap1!=null){
            mbitmap = mbitmap1;
        }
        touxiangInit(mbitmap);
        getData();
    }
    //??????????????????
    public Bitmap getpictureLocal() {
        SharedPreferences sharedPreferencess = getSharedPreferences(Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE);
        String toto = sharedPreferencess.getString("picture", "");
        if (!toto.equals("")) {
            byte[] buff = Base64.decode(toto, Base64.DEFAULT);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(buff);
            Drawable drawable = Drawable.createFromStream(inputStream, "");
            if (drawable == null) {
                Toast.makeText(MineFormationActivity.this, "drawable??????", Toast.LENGTH_SHORT).show();
            } else {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                return bitmap;
            }
        } else {
            Toast.makeText(MineFormationActivity.this, "??????sharepreferences", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    //????????????
    public void touxiangInit(Bitmap c_bitmap){
        mRoundedSquare.setMbitmap(c_bitmap);
    }
    //????????? ??????????????????????????????
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.linear_formation_activity:
                //???????????????????????????
                openCameraAndDcim();
                break;
            case R.id.text_back_forma:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getData();
    }

    //??????????????????
    public void getData(){
        //??????????????????????????????????????????????????????????????????
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call call = new SelectAction().selectFormation(MainFunctionActivity.usename);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call,IOException e) {

                    }
                    @Override
                    public void onResponse(Call call,Response response) throws IOException {
                        String jsoner = response.body().string();
                        //??????jsoner, new TypeToken<Users>(){}.getType()
                        Gson gsoner = new Gson();
                        Users users = gsoner.fromJson(jsoner, new TypeToken<Users>(){}.getType());
                        if (users.getFakename()!=null){
                            mfakenamer = users.getFakename();
                        }
                        if (users.getSex()!=null){
                            msex = users.getSex();
                        }
                        if (users.getAge()!=null){
                            mage = users.getAge();
                        }
                        if (users.getHometown()!=null){
                            mhometown = users.getHometown();
                        }
                        if (users.getAddress()!=null){
                            maddress = users.getAddress();
                        }
                        if (users.getSignature()!=null){
                            msignature = users.getSignature();
                        }
                        //????????????
                        Intent intent = new Intent("com.ilikexy.bigwork.forma");
                        MineFormationActivity.this.sendBroadcast(intent);
                    }
                });
            }
        }).start();

    }


    //-------------------------------------------------------------------------------------------------
    public void openCameraAndDcim(){
        //???????????????????????????????????????????????????????????????
        DialogContainer.showPictureDialog(MineFormationActivity.this,R.layout.dialog_touxiang,
                "com.ilikexy.bigwork.paizhaoone","com.ilikexy.bigwork.xiangceone");
        //???????????????????????????????????? ???????????????????????????????????????????????????
    }
    //????????????
    public void broadcastPicture(){
        IntentFilter intentFilter1 = new IntentFilter("com.ilikexy.bigwork.paizhaoone");//??????action
        IntentFilter intentFilter2 = new IntentFilter("com.ilikexy.bigwork.xiangceone");//??????action
        paizhaoReceiverOne = new PaizhaoRecieverOne();//????????????????????????????????????
        xiangceReceiverOne = new XiangceRecieverOne();//????????????????????????????????????
        registerReceiver(paizhaoReceiverOne,intentFilter1);//????????????
        registerReceiver(xiangceReceiverOne,intentFilter2);
    }
    //??????????????????
    class PaizhaoRecieverOne extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {//??????????????????????????????
            mPhotoUri = new CaptureClass(MineFormationActivity.this).openCamera();//????????????
        }
    }
    //??????????????????
    class XiangceRecieverOne extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            new CaptureClass(MineFormationActivity.this).openDcim();//????????????
        }
    }
    //???????????????????????????????????????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1://????????????
                if (resultCode == RESULT_OK) {
                    try {
                        final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mPhotoUri);
                        //??????????????? ????????????????????????????????????Glide????????????
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//
                               Bitmap bitmap1 =  PicCutAndScale.getSquareRect(bitmap);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                //****????????????????????????????????????????????????,????????????????????????
                                SharedPreferences.Editor editor = getSharedPreferences(
                                        Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE).edit();
                                //??????????????????json,?????????sharePreference???
                                String tojsoner = new String(android.util.Base64.encodeToString(baos.toByteArray(),
                                        Base64.DEFAULT));
                                editor.putString("picture", tojsoner);
                                editor.apply();
                                mRoundedSquare.setMbitmap(bitmap1);
                                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap,//????????????
                                        "Titler", "?????????");
                                //??????????????????uri?????????, Scanner scan
                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, mPhotoUri);
                                sendBroadcast(intent);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(MineFormationActivity.this,"????????????",Toast.LENGTH_SHORT).show();}
                break;
            case 2:
                if (resultCode==RESULT_OK){//??????????????????
                    Uri uri = data.getData();//??????data????????????uri
                    String filePath = DcimUriget.getFilePathByUri(this, uri);//????????????uri???????????????????????????
                    if (!TextUtils.isEmpty(filePath)) {//?????????????????????
                        //??????Glide????????????
                        RequestOptions requestOptions1 = new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE);
                        //?????????????????? ivImage??????????????????????????????
                        Glide.with(this).load(filePath).apply(requestOptions1)
                                .into(new SimpleTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                        mRoundedSquare.setMbitmap(((BitmapDrawable)resource).getBitmap());
                                    }
                                });
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);//?????????????????????
                        //??????????????????json,?????????sharePreference???
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//bitmap??????????????????baos
                        SharedPreferences.Editor editor = getSharedPreferences(
                                Constant.STRING_SHAREPREFER_USE_PASS, MODE_PRIVATE).edit();
                        String tojsoner = new String(android.util.Base64.encodeToString(baos.toByteArray(),
                                Base64.DEFAULT));
                        editor.putString("picture", tojsoner);
                        editor.apply();
                    }
                }else{Toast.makeText(MineFormationActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();}
                break;
            default:
                break;
        }//switchcasef??????
    }
    //??????????????????
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        switch (requestCode){
            case 11:
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    new CaptureClass(MineFormationActivity.this).openDcimright();//????????????
                }else{
                    Toast.makeText(this,"??????????????????????????????",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }
    //?????????????????????????????????????????????
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(paizhaoReceiverOne);
        unregisterReceiver(xiangceReceiverOne);
        unregisterReceiver(formaReciever);
    }
    //-----------------------------------------------------------------------------------------------

    class FormaReciever extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            List<MyFormation> myFormationList = new ArrayList<MyFormation>();
            MyFormation myFormation1 = new MyFormation("??????",mfakenamer,false,true);
            MyFormation myFormation2 = new MyFormation("??????",msex,true,false);
            MyFormation myFormation3 = new MyFormation("??????",mage,true,false);
            MyFormation myFormation4 = new MyFormation("??????",mhometown,true,false);
            MyFormation myFormation5 = new MyFormation("?????????",maddress,false,true);
            MyFormation myFormation6 = new MyFormation("????????????",msignature,true,false);


            myFormationList.add(myFormation1);
            myFormationList.add(myFormation2);
            myFormationList.add(myFormation3);
            myFormationList.add(myFormation4);
            myFormationList.add(myFormation5);
            myFormationList.add(myFormation6);
            //???????????????
            LinearLayoutManager manager = new LinearLayoutManager(MineFormationActivity.this);
            mRecycler.setLayoutManager(manager);
            FormaRecyclerAdapter adapter = new FormaRecyclerAdapter(myFormationList);
            mRecycler.setAdapter(adapter);
        }
    }


}