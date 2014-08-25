package com.sudarom.update;

import java.io.PrintWriter;
import java.util.Calendar;

import com.sudarom.tools.UrlTools;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateActivity extends Activity
{

    private Button btcheck = null;
    private Button btdisk = null;
    private Button btinfo = null;
    private TextView tvcurrentversion = null;
    private TextView tvlatestversion = null ;
    private TextView tvupdateinfo = null ;
    private String strlatest = null;
    private String path = null;
    private String mWay = null;//??
    public static String strcurrent  = SystemProperties.get("ro.build.version.incremental");


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        tvcurrentversion = (TextView)findViewById(R.id.current_version);
        tvlatestversion = (TextView)findViewById(R.id.latest_version);
        tvupdateinfo = (TextView)findViewById(R.id.update_info);
        tvcurrentversion.setText(getResources().getString(R.string.current_version) +"MIUI-"+ strcurrent); //??????
        tvlatestversion.setText(getResources().getString(R.string.lates_version) +"MIUI-"+ strcurrent); //??????
        btcheck = (Button)findViewById(R.id.buttoncheck);
        btdisk = (Button)findViewById(R.id.buttondisk);
        btinfo = (Button)findViewById(R.id.buttoninfo);
        Calendar c = Calendar.getInstance();  
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        
        if("1".equals(mWay) || "6".equals(mWay) || "7".equals(mWay))
        {
        checkupdate();//程序打开自动检测一次
        }      
        
        btcheck.setOnClickListener(new View.OnClickListener()//检测新版本按钮监听
        {
            @Override
            public void onClick(View v)  //获取云端版本号
            {
                checkupdate();
            }

        });


        btinfo.setOnClickListener(new View.OnClickListener()  //更新日志按钮监
        {

            @Override
            public void onClick(View v)
            {
                updateinfo();
            }
        });

        btdisk.setOnClickListener(new View.OnClickListener()	//网盘按钮监听
        {

            @Override
            public void onClick(View v)
            {
                path = getResources().getString(R.string.disk_url); //获取网盘地址
                Uri uri = Uri.parse(path);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void updateinfo()
    {
        try
        {
            path = getResources().getString(R.string.update_info_url); //获取github上的更新日志
            strlatest = UrlTools.getcontent(path);
            tvupdateinfo.setText(getResources().getString(R.string.updateinfo) + strchange(strlatest, "&", "\n"));

        }
        catch (Exception e)
        {
            Toast.makeText(UpdateActivity.this, getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void checkupdate()
    {

        try
        {
            path = getResources().getString(R.string.version_url); //获取github上的版本号
            strlatest = UrlTools.getcontent(path);
            tvlatestversion.setText(getResources().getString(R.string.lates_version) +"MIUI-"+ strlatest);
            if(strcurrent.equals(strlatest))
            {
                Toast.makeText(UpdateActivity.this, getResources().getString(R.string.isnew), Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(UpdateActivity.this, getResources().getString(R.string.nisnew), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(UpdateActivity.this, getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
        }

    }

    public static String strchange(String message, String oldchar, String newChar) //字符转换 将服务器端"&"转为回车
    {
        if(message.indexOf(oldchar) != -1)
        {
            message = message.replace(oldchar, newChar);
        }
        return message;
    }

    public static void root(String paramString)//重启rec
    {
        try
        {
            Process localProcess = Runtime.getRuntime().exec("su");
            PrintWriter localPrintWriter = new PrintWriter(localProcess.getOutputStream());
            localPrintWriter.println(paramString);
            localPrintWriter.flush();
            localPrintWriter.close();
            localProcess.waitFor();
            return;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)//添加底部菜单（重启rec和关于）
    {
        menu.add(0, 1, 1, getResources().getString(R.string.recovey));
        menu.add(0, 2, 2, getResources().getString(R.string.about));
        return super.onCreateOptionsMenu(menu);
    }



    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == 1)
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setMessage(getResources().getString(R.string.reboot_recovery));
            ad.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int i)
                {
                    root("reboot recovery");
                }
            });
            ad.setNegativeButton(getResources().getString(R.string.cancel), null);
            ad.show();//显示对话框
        }


        if(item.getItemId() == 2)
        {
            new AlertDialog.Builder(UpdateActivity.this)
            .setTitle(getResources().getString(R.string.about))
            .setMessage(getResources().getString(R.string.author_info))
            .show();
        }
        return super.onOptionsItemSelected(item);
    }
}
