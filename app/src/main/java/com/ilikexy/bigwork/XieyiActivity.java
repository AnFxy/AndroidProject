package com.ilikexy.bigwork;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ilikexy.bigwork.baseactivity.BaseActivity;
import com.jaeger.library.StatusBarUtil;

public class XieyiActivity extends BaseActivity {
    private TextView text_back_xieyiactivity,text_xieyiactivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xieyi);
        StatusBarUtil.setColor(XieyiActivity.this, Color.WHITE,4);
        init();
    }
    public void init(){
        text_back_xieyiactivity = (TextView)findViewById(R.id.text_back_xieyiactivity);
        text_xieyiactivity = (TextView)findViewById(R.id.text_xieyiactivity);
        String longstr="首部及导言\n" +
                "\n" +
                "欢迎你使用方讯方信软件及服务！\n" +
                "\n" +
                "为使用方讯方信软件（以下简称“本软件”）及服务，你应当阅读并遵守《方讯方信软件许可及服务协议》（以下简称“本协议”），以及《腾讯服务协议》。请你务必审慎阅读、充分理解各条款内容，特别是免除或者限制责任的条款，以及开通或使用某项服务的单独协议，并选择接受或不接受。限制、免责条款可能以加粗形式提示你注意。\n" +
                "\n" +
                "除非你已阅读并接受本协议所有条款，否则你无权下载、安装或使用本软件及相关服务。你的下载、安装、使用、获取微信帐号、登录等行为即视为你已阅读并同意上述协议的约束。\n" +
                "\n" +
                "如果你未满18周岁，请在法定监护人的陪同下阅读本协议及上述其他协议，并特别注意未成年人使用条款。特别地，如果你是未满14周岁的儿童，则在完成帐号注册前，还应请你的监护人仔细阅读方讯公司专门制定的《儿童隐私保护声明》。只有在取得监护人对《儿童隐私保护声明》的同意后，未满14周岁的儿童方可使用微信服务。\n" +
                "\n" +
                "一、协议的范围\n" +
                "\n" +
                "1.1 协议适用主体范围\n" +
                "\n" +
                "本协议是你与方讯之间关于你下载、安装、使用、复制本软件，以及使用方讯相关服务所订立的协议。\n" +
                "\n" +
                "1.2 协议关系及冲突条款\n" +
                "\n" +
                "本协议被视为《方讯服务协议》的补充协议，是其不可分割的组成部分，与其构成统一整体。本协议与上述内容存在冲突的，以本协议为准。\n" +
                "\n" +
                "本协议内容同时包括方讯可能不断发布的关于本服务的相关协议、业务规则等内容。上述内容一经正式发布，即为本协议不可分割的组成部分，你同样应当遵守。\n" +
                "\n" +
                "二、关于本服务\n" +
                "\n" +
                "2.1 本服务的内容\n" +
                "\n" +
                "本服务内容是指方讯向用户提供的跨平台的通讯工具（以下简称“方信”），支持单人、多人参与，在发送语音短信、视频、图片、表情和文字等即时通讯服务的基础上，同时为用户提供包括但不限于关系链拓展、便捷工具、微信公众帐号、开放平台、与其他软件或硬件信息互通等功能或内容的软件许可及服务（以下简称“本服务”）。\n" +
                "\n" +
                "2.2 本服务的形式\n" +
                "\n" +
                "2.2.1 你使用本服务需要下载方讯方信客户端软件，对于这些软件，方讯给予你一项个人的、不可转让及非排他性的许可。方信网页版、Windows版、Mac版等需要通过二维码扫描登录。你仅可为访问或使用本服务的目的而使用这些软件及服务。\n" +
                "\n" +
                "2.2.2 本服务中方讯方信客户端软件可能提供包括但不限于iOS、Android、Windows Phone、Symbian、BlackBerry、Windows、Mac等多个应用版本，用户必须选择与所安装终端设备相匹配的软件版本。\n" +
                "\n" +
                "2.3 本服务许可的范围\n" +
                "\n" +
                "2.3.1 方讯给予你一项个人的、不可转让及非排他性的许可，以使用本软件。你可以为非商业目的在单一台终端设备上安装、使用、显示、运行本软件。\n" +
                "\n";
        text_xieyiactivity.setText(longstr);
        text_back_xieyiactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XieyiActivity.this.onBackPressed();
            }
        });
    }

}