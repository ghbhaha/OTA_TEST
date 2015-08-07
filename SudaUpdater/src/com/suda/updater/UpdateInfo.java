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
            updateinfo = HttpTools.getcontent(getResources().getString(R.string.base_url) + "info_" + version); //��ȡ�汾��
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



    public static String strchange(String message, String oldchar, String newChar) //�ַ�ת���� �����ص�"&"תΪ�س�
    {
        if(message.indexOf(oldchar) != -1)
        {
            message = message.replace(oldchar, newChar);
        }
        return message;
    }



    //��ִ̨�������������ֹ����
    private class info extends AsyncTask<Object, Integer, Integer>
    {

        TextView tvupdateinfo = (TextView) findViewById(R.id.update_info);


        protected void onPreExecute()
        {

        }

        // �ں�̨����


        /**
         * �����Object������ӦAsyncTask�еĵ�һ������
         * �����Int����ֵ��ӦAsyncTask�ĵ���������
         * �÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
         * ���ǿ��Ե���publishProgress��������onProgressUpdate��UI���в���
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
                tvupdateinfo.setText("���޸�������");
                break;
            case 1:
                tvupdateinfo.setText(getResources().getString(R.string.updateinfo) + strchange(updateinfo, "&", "\n"));
                break;
            }
        }

        /**
         * �����Intege������ӦAsyncTask�еĵڶ�������
         * ��doInBackground�������У���ÿ�ε���publishProgress�������ᴥ��onProgressUpdateִ��
         * onProgressUpdate����UI�߳���ִ�У����п��Զ�UI�ռ���в���
         */
        @Override
        protected void onProgressUpdate(Integer... values)
        {

        }


    }

}
