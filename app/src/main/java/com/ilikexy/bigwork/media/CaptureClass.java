package com.ilikexy.bigwork.media;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.ilikexy.bigwork.baseactivity.BaseActivity;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

//调用系统的相机，图片拍照后会生成相应的uri,从uri中获取数据
public class CaptureClass {
    private File mphotoFile;//图片文件
    private Uri mphotoUri;//图片Uri
    private Context context;
    public CaptureClass(BaseActivity c_base){
        context = c_base;
    }
    //打开相机
    public Uri openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//Action为多媒体库的拍照
        intent.addCategory(Intent.CATEGORY_DEFAULT);//未指定categories
        mphotoFile = getPhotoFileFromDcim();//从相册中获取指定路径名称的空图片
        Uri photouri = null;//空图片的uri
        //根据Android版本不同，uri获取方式不同
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){//Android api 大于 24 ， Android版本7.0
            //获得临时读取权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            photouri = FileProvider.getUriForFile(context,
                    "com.ilikexy.bigwork.fileprovider",mphotoFile);
        }else{//Android api 小于 24 ， Android版本小于7.0
            photouri = Uri.fromFile(mphotoFile);
        }//指定生成图片文件类型,将Uri通过intent传给相机活动，key名是绝对化固定的
        if (photouri!=null){
            mphotoUri = photouri;
            intent.putExtra(MediaStore.EXTRA_OUTPUT,mphotoUri);//Uri传入
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());//图片类型传入
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            ((BaseActivity)context).startActivityForResult(intent,1);//回调跳转，指明唯一请求码
            return mphotoUri;
        }else{
            Toast.makeText(context,"图片Uri生成失败！",Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    //获取空图片
    public File getPhotoFileFromDcim(){
        try {
            String dcimpath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath();//获取相册路径
            String filename = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());//图片名称
            File fatherFile = new File(dcimpath);//父类文件
            fatherFile.mkdirs();
            File sonFile = File.createTempFile(filename,".jpg",fatherFile);//生成子类文件
            return sonFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //-----------------------------------相册-------------------------------------
    //相册
    public void openDcim(){
        //请求读取权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((BaseActivity)context,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE},11);
        }else{
            openDcimright();
        }
    }

    //打开相册
    public void openDcimright(){
        //打开系统相册， 未指定具体的uri, ACTION_PICK
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((BaseActivity)context).startActivityForResult(intentToPickPic,2);
    }


    //对于请求权限结果处理需要在活动中实现
    //关于换头像的问题，我封装成一个CaptureClass类可以拍照，也可以从相册选择。
    //对拍照结果回调进行处理,由于回调结果的重写必须在Activity中，所以难以实现，所以对于请求结果的方法，需要在活动中重写
    //由于多个地方调用相机调用，显然重写工作量大，我试着在BaseActivity中重写回调函数,
    //但是问题出现了：当启动拍照后，控件图片设置就已经运行了，导致拍照后的结果无法设置为控件图片,多线程并行问题
    //因为onActivityResult方法类似于一个子线程（不然为何不能直接更新ui呢）
    //线程最大特点就是不用等待run方法执行完就可以直接执行start后面的方法，所以我们更新控件图片不能通过传递获取后更新
    //而是要在onActivityResult中更新。（runonUiThread）









}
