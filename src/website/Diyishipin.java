package website;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.DownloadUtil;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * 第一视频: http://www.v1.cn/
 * @author Asche
 * @date 2018-10-20 18:02:29
 * @github https://github.com/asche910
 */
public class Diyishipin extends BaseSite {

    @Override
    public void downloadByUrl(String playUrl, String outputDir) {
        try {
            Document document = Jsoup.connect(playUrl).get();
            Element embed = document.body().selectFirst("object[id=play1]").selectFirst("embed[name=play1]");

            String flashvars = embed.attr("flashvars");
            String videoSrc = flashvars.substring(flashvars.indexOf("videoUrl=") + 9);

            System.out.println(videoSrc);

            download(Collections.singletonList(videoSrc), outputDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void download(List<String> videoSrcs, String outputDir) {

        String[] strs = videoSrcs.get(0).split("/");
        String fileName = strs[strs.length-1];
        String fileDir = outputDir + File.separatorChar + fileName.replaceAll("[/|\\\\]", "");

        try {
            DownloadUtil.downloadVideo(videoSrcs.get(0), fileDir);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
