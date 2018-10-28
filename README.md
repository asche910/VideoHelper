# VideoHelper

### 主要功能：完成一些主流网站的视频下载任务
### 相应的功能在爬虫的首选语言python都有实现，这里的目的是只是对java的挚爱以及对爬虫的练习
### 目前支持：Bilibili（支持av和ep等类型）、知乎、第一视频网

## 使用方式一：代码使用，引用导入[VideoHelper.jar](https://github.com/asche910/VideoHelper/raw/master/VideoHelper.jar)
然后：
```JAVA
  // 首先完成初始化；这里playUrl就是视频的播放地址， outputDir则是视频下载的保存目录
  // 其中 outputDir = System.getProperty("user.dir"); 可以保存至运行目录或自定义
  BaseSite base = UrlRecognition.reconize(playUrl, outputDir);
  // 然后再下载
  base.downloadByUrl(playUrl, outputDir);

```

## 使用方式二：cmd或bash，下载[VideoHelper.jar](https://github.com/asche910/VideoHelper/raw/master/VideoHelper.jar)
然后：
cmd或bash里面
```
  // 后面outputDir可选
  java -jar VideoHelper.jar playUrl outputDir
  
```
如图：

![image](https://github.com/asche910/VideoHelper/blob/master/screenshots/TIM%E6%88%AA%E5%9B%BE20181027201653.png)

## 使用方式三：图形界面，运行[VideoHelperGUI.jar](https://github.com/asche910/VideoHelper/raw/master/VideoHelperGUI.jar)
运行方式同上（但要注意这均需要java运行环境jre）
如图：

![image](https://github.com/asche910/VideoHelper/blob/master/screenshots/TIM%E6%88%AA%E5%9B%BE20181027212558.png)




知乎教程介绍参照这里:
https://www.cnblogs.com/asche/p/9664987.html

