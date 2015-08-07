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
    private String flag = "check";//��ť����־
    private String mWay = null;//����
    private String ydss_url = "http://bbs.ydss.cn"; //���ظ��¶�λ����ַ������д��github(ydss_url)
    private String strlatest = SystemProperties.get("ro.mk.version");  //���°汾
    private String strcurrent  = SystemProperties.get("ro.mk.version");  //���ذ汾
    //����SystemProperties.get("ro.mk.version");
    //��Ҫ����layoutlib.jar ������Բο�http://blog.sina.com.cn/s/blog_6b597ccb0100ywrw.html
    
    
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

        tvversion_msg.setText("��ǰ�汾:" + strcurrent);

        //��ť������flag�ı�
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

        //������־
        rl.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO �Զ����ɵķ������
                if(strlatest == strcurrent || strlatest == null) //Ϊ�ջ�Ϊ��ǰ�汾��ʾ��ǰ��־
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


        //ÿ���������������м��һ��
        Calendar c = Calendar.getInstance();
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));

        if("1".equals(mWay) || "6".equals(mWay) || "7".equals(mWay))
        {
            check checktask = new check();
            checktask.execute();//���Ϊ�����壬�����գ�������Զ����һ��
        }
    }



    //��ִ̨�У���ֹ����
    private class check extends AsyncTask<Object, Integer, Integer>
    {


        Button btck = (Button) findViewById(R.id.buttonck);
        @Override

        protected void onPreExecute()
        {
            btck.setText("����С�����");
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
                btck.setText("������");
                break;
            case 1:
                tvversion_msg.setText("���°汾:" + strlatest);
                flag = "disk";
                Toast.makeText(MainActivity.this, getResources().getString(R.string.nisnew), Toast.LENGTH_SHORT).show();
                btck.setText("���ظ���");
                break;
            case 2:
                Toast.makeText(MainActivity.this, getResources().getString(R.string.net_error), Toast.LENGTH_SHORT).show();
                btck.setText("������");
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

    public int checkupdate()
    {
        int tmp;
        try
        {
            strlatest = HttpTools.getcontent(getResources().getString(R.string.base_url) + "version"); //��ȡ�汾��
            ydss_url = HttpTools.getcontent(getResources().getString(R.string.base_url) + "ydss_url"); //��ȡ����


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


    //�˵�ѡ��
    @Override
    public boolean onCreateOptionsMenu(Menu menu)//��ӵײ��˵�������rec�͹��ڣ�
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
            ad.show();//��ʾ�Ի���
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
