package website;

import bean.VideoBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract public class BaseSite {

    // 解析出的下载地址
    public List<String> urls = new ArrayList<>();

    abstract public void downloadByUrl(String playUrl, String outputDir);

    abstract void download(List<String> videoSrcs, String outputDir) throws IOException;

    abstract public VideoBean getInfo();
}
