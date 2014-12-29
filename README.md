新版系统检测更新器
===========

开源说明
----------------

1.此项目为安卓版本，方便为有兴趣的机友提供ota接口

2.如果你对本项目感兴趣或者有什么意见或建议,欢迎联系[510146422@qq.com](mailto:510146422@qq.com)

使用教程
---------------
1.申请github账号，将我的的OTA_TEST开源项目Fork

2.了解安卓开发的朋友可以直接将我的项目导入，进行修改编译

注意：调用SystemProperties.get("ro.mk.version");
      需要导入layoutlib.jar 具体可以参考http://blog.sina.com.cn/s/blog_6b597ccb0100ywrw.html

3.普通机油可以反编译我提供的成品，反编译修改打开

res\values-zh-rCN\strings.xml，修改base_url（基准地址）

https://raw.githubusercontent.com/ghbhaha/OTA_TEST/bacon_mokee/

解释：https://raw.githubusercontent.com/你的账号/项目名/分支名/

4.在github中编写更新日志，版本号，更新定位网址

version：当前版本

info_版本号：相应版本对应日志

ydss_url：更新下载所需定位的网址

具体可以参考https://github.com/ghbhaha/OTA_TEST/tree/bacon_mokee

界面截图
---------------
![Image text](https://github.com/ghbhaha/OTA_TEST/blob/Android_New/screenshots/Screenshot_2014-12-29-14-19-55.png)
![Image text](https://github.com/ghbhaha/OTA_TEST/blob/Android_New/screenshots/Screenshot_2014-12-29-14-19-59.png)
![Image text](https://github.com/ghbhaha/OTA_TEST/blob/Android_New/screenshots/Screenshot_2014-12-29-14-20-04.png)
