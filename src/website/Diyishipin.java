package website;

import bean.VideoBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.DownloadUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 第一视频: http://www.v1.cn/
 * @author Asche
 * @date 2018-10-20 18:02:29
 * @github https://github.com/asche910
 */
public class Diyishipin extends BaseSite {

    private String playUrl;
    private String outputDir;
    private String fileName;

    private boolean isResolved;

    public Diyishipin() {
    }

    public Diyishipin(String playUrl, String outputDir) {
        if (!isResolved) {
            this.playUrl = playUrl;
            this.outputDir = outputDir;

            try {
                Document document = Jsoup.connect(playUrl).get();
                Element embed = document.body().selectFirst("object[id=play1]").selectFirst("embed[name=play1]");

                String flashvars = embed.attr("flashvars");
                String videoSrc = flashvars.substring(flashvars.indexOf("videoUrl=") + 9);

                urls.add(videoSrc);

                String[] strs = videoSrc.split("/");
                fileName = strs[strs.length-1];
            } catch (IOException e) {
                e.printStackTrace();
            }
            isResolved = true;
        }

    }

    @Override
    public void downloadByUrl(String playUrl, String outputDir) {
        try {
            if (!isResolved) {
                Document document = Jsoup.connect(playUrl).get();
                Element embed = document.body().selectFirst("object[id=play1]").selectFirst("embed[name=play1]");

                String flashvars = embed.attr("flashvars");
                String videoSrc = flashvars.substring(flashvars.indexOf("videoUrl=") + 9);

                urls.add(videoSrc);

                String[] strs = videoSrc.split("/");
                fileName = strs[strs.length-1];
                isResolved = true;
            }

            System.out.println(urls.get(0));

            download(Collections.singletonList(urls.get(0)), outputDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void download(List<String> videoSrcs, String outputDir) {

        String fileDir = outputDir + File.separatorChar + fileName.replaceAll("[/|\\\\]", "");

        try {
            DownloadUtil.downloadVideo(videoSrcs.get(0), fileDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public VideoBean getInfo() {
        VideoBean bean = new VideoBean();
        bean.setTitle(fileName);
        return bean;
    }
}
