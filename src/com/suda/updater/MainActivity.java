package com.suda.updater;



import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sudarom.tools.HttpTools;
import com.sudarom.tools.RootCmd;

public class MainActivity extends Activity
{

    private TextView tvversion_msg;
    private Button btck;
    private RelativeLayout rl;
    private String flag = "check";//按钮检测标志
    private String mWay = null;//星期
    private String ydss_url = "http://bbs.ydss.cn"; //下载更新定位的网址，可以写在github(ydss_url)
    private String strlatest = SystemProperties.get("ro.mk.version");  //最新版本
    private String strcurrent  = SystemProperties.get("ro.mk.version");  //本地版本
    //调用SystemProperties.get("ro.mk.version");
    //需要导入layoutlib.jar 具体可以参考http://blog.sina.com.cn/s/blog_6b597ccb0100ywrw.html
    
    
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);



        tvversion_msg = (TextView) findViewById(R.id.version_msg);
        btck = (Button) findViewById(R.id.buttonck);
        rl = (RelativeLayout) findViewById(R.id.topLayout);

        tvversion_msg.setText("当前版本:" + strcurrent);

        //按钮功能随flag改变
        btck.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                if(flag.equals("check"))
                {
                    check checktask = new check();
                    checktask.execute();

                }
                else if(flag.equals("disk"))
                {

                    Uri uri = Uri.parse(ydss_url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }

            }
        });

        //更新日志
        rl.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO 自动生成的方法存根
                if(strlatest == strcurrent || strlatest == null) //为空或为当前版本显示当前日志
                {
                    Intent it = new Intent(MainActivity.this, UpdateInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("version", strcurrent);
                    it.putExtras(bundle);
                    startActivity(it);

                }
                else
                {
                    Intent it = new Intent(MainActivity.this, UpdateInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("version", strlatest);
                    it.putExtras(bundle);
                    startActivity(it);

                }

            }
        });


        //每周五六日启动自行检测一次
        Calendar c = Calendar.getInstance();
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        if("1".equals(mWay) || "6".equals(mWay) || "7".equals(mWay))
        {
            check checktask = new check();
            checktask.execute();//如果为星期五，六，日，软件打开自动检测一次
        }
    }



    //后台执行，防止卡死
    private class check extends AsyncTask<Object, Integer, Integer>
    {


        Button btck = (Button) findViewById(R.id.buttonck);
        @Override

        protected void onPreExecute()
        {
            btck.setText("检测中。。。");
        }

        // 在后台运行


        /**
         * 这里的Object参数对应AsyncTask中的第一个参数
         * 这里的Int返回值对应AsyncTask的第三个参数
         * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
         * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
         */


        @Override
        protected Integer doInBackground(Object... arg0)
        {
            // TODO Auto-generated method stub
            return checkupdate();

        }

        @Override
        protected void onPostExecute(Integer result)
        {

            switch (result)
            {
            case 0:
                Toast.makeText(MainActivity.this, getResources().getString(R.string.isnew), Toast.LENGTH_SHORT).show();
                btck.setText("检测更新");
                break;
            case 1:
                tvversion_msg.setText("最新版本:" + strlatest);
                flag = "disk";
                Toast.makeText(MainActivity.this, getResources().getString(R.string.nisnew), Toast.LENGTH_SHORT).show();
                btck.setText("下载更新");
                break;
            case 2:
                Toast.makeText(MainActivity.this, getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                btck.setText("检测更新");
                break;
            }
        }

        /**
         * 这里的Intege参数对应AsyncTask中的第二个参数
         * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
         * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
         */
        @Override
        protected void onProgressUpdate(Integer... values)
        {

        }


    }

    public int checkupdate()
    {
        int tmp;
        try
        {
            strlatest = HttpTools.getcontent(getResources().getString(R.string.base_url) + "version"); //获取版本号
            ydss_url = HttpTools.getcontent(getResources().getString(R.string.base_url) + "ydss_url"); //获取链接


            if(strcurrent.equals(strlatest))
            {
                tmp = 0;
            }
            else
            {
                tmp = 1;
            }
        }
        catch (Exception e)
        {
            tmp = 2;
        }
        return tmp;

    }


    //菜单选项
    @Override
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
                    RootCmd.RunRootCmd("reboot recovery");
                }
            });
            ad.setNegativeButton(getResources().getString(R.string.cancel), null);
            ad.show();//显示对话框
        }


        if(item.getItemId() == 2)
        {
            new AlertDialog.Builder(MainActivity.this)
            .setTitle(getResources().getString(R.string.about))
            .setMessage(getResources().getString(R.string.author_info))
            .show();
        }
        return super.onOptionsItemSelected(item);
    }
}
