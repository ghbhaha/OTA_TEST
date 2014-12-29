package com.suda.updater;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.suda.updater.R;
import com.sudarom.tools.HttpTools;

public class UpdateInfo extends Activity
{

    private String updateinfo;
    private String version;
    private TextView tvversion;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_info);
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        version = bundle.getString("version");
        tvversion = (TextView) findViewById(R.id.tvversion);

        tvversion.setText(version);
        info infotask = new info();
        infotask.execute();


    }


    public int getUpdateinfo()
    {

        try
        {
            updateinfo = HttpTools.getcontent(getResources().getString(R.string.base_url) + "info_" + version); //获取版本号
            if(updateinfo.equals(null))
            {
                return 0;
            }
            return 1;

        }
        catch (Exception e)
        {
            return 0;
        }

    }



    public static String strchange(String message, String oldchar, String newChar) //字符转换函 将返回的"&"转为回车
    {
        if(message.indexOf(oldchar) != -1)
        {
            message = message.replace(oldchar, newChar);
        }
        return message;
    }



    //后台执行网络操作，防止卡死
    private class info extends AsyncTask<Object, Integer, Integer>
    {

        TextView tvupdateinfo = (TextView) findViewById(R.id.update_info);


        protected void onPreExecute()
        {

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

            return getUpdateinfo();

        }

        @Override
        protected void onPostExecute(Integer result)
        {

            switch (result)
            {
            case 0:
                tvupdateinfo.setText("暂无更新内容");
                break;
            case 1:
                tvupdateinfo.setText(getResources().getString(R.string.updateinfo) + strchange(updateinfo, "&", "\n"));
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

}
