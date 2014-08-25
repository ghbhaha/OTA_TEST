系统检测更新器
===========

开源说明
----------------

1.此项目为安卓版本，方便为有兴趣的机友提供ota接口

2.如果你对本项目感兴趣或者有什么意见或建议,欢迎联系[510146422@qq.com](mailto:510146422@qq.com)

使用教程
---------------
1.申请github账号，将我的的OTA_TEST开源项目Fork

2.了解安卓开发的朋友可以直接将我的项目导入，进行修改编译（签名需要使用系统签名）

3.普通机油可以反编译我提供的成品（MIUI版本，需要导入MIUI资源框架），打开

res\values\strings.xml，修改version_url（版本号），update_info_url（更新日志），

disk_url（百度网盘），最后别忘了用我提供的工具签名

https://raw.githubusercontent.com/ghbhaha/OTA_TEST/master/version

（将ghbhaha改成你的账号，该地址为打开version后点击raw得到的）

https://raw.githubusercontent.com/ghbhaha/OTA_TEST/master/update_info

（将ghbhaha改成你的账号，该地址为打开update_info后点击raw得到的）

当你需要更新时，修改OTA_TEST/version,uodate_info就可以了

网盘可以分享出一个文件夹专门用来存放刷机包（ota或整包）


