package website;

import org.json.JSONObject;
import util.DownloadUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.HttpUtil.getResponseContent;
import static util.PrintUtil.println;

/**
 * 知乎: https://www.zhihu.com/
 * @author Asche
 * @date 2018-10-20 18:02:29
 * @github https://github.com/asche910
 */
public class Zhihu extends BaseSite{

    private String ApiUrl = "https://lens.zhihu.com/api/videos/";
    private int timeLength;
    private int fileSize = 0;
    private String format;
    private String id;

    @Override
    public void downloadByUrl(String playUrl, String outputDir){
        System.out.println("website.Zhihu start: ");
        // 解析出视频id
        String[] strs = playUrl.split("/");
        for (String str: strs)
            if (str.matches("\\d{8,}"))
                id = str;
        String fileDir = outputDir + File.separatorChar + "zhihu_" + id + ".mp4";

        try {
            String videoSrc = getVideoSrc(ApiUrl + id);

            println("# Title: " + "zhihu_" + id);
            println("     -TimeLength: " + timeLength / 60 + ":" + timeLength % 60);
            println("     -File Size: " + fileSize / 1024 / 1024 + " M");

            if (format.equals("mp4")){
                download(Collections.singletonList(videoSrc), fileDir);
            }else {
                FileOutputStream outputStream = new FileOutputStream(new File(fileDir));
                List<String> urls = getSrcList(videoSrc);

                for(String url: urls){
                    util.HttpUtil.combineStream(util.HttpUtil.getInputStream(url), outputStream);
                }
                outputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    void download(List<String> videoSrcs, String outputDir) throws IOException {
        DownloadUtil.downloadVideo(videoSrcs.get(0), outputDir);
    }

    /**
     * 由api提取出最高清晰度的url, format, timeLength, fileSize
     * @param url
     * @return play_url
     */
    private String getVideoSrc(String url) throws IOException {

        String json = getResponseContent(url);

        JSONObject jsonObject = new JSONObject(json).getJSONObject("playlist");

        if(jsonObject.has("hd")){
            JSONObject jb = jsonObject.getJSONObject("hd");
            format = jb.getString("format");
            timeLength = jb.getInt("duration");
            fileSize = jb.getInt("size");

            return jb.getString("play_url");
        }else if(jsonObject.has("sd")){
            JSONObject jb = jsonObject.getJSONObject("sd");
            format = jb.getString("format");
            timeLength = jb.getInt("duration");
            fileSize = jb.getInt("size");

            return jb.getString("play_url");
        }else if(jsonObject.has("ld")){
            JSONObject jb = jsonObject.getJSONObject("ld");
            format = jb.getString("format");
            timeLength = jb.getInt("duration");
            fileSize = jb.getInt("size");

            return jb.getString("play_url");
        }
        return null;
    }

    /**
     * 解析出播放清单文件内分散的多个js格式视频url
     * @param url
     * @return
     */
    private List<String> getSrcList(String url) throws IOException {
        System.out.println(url);

        List<String> list = new ArrayList<>();
        String content = getResponseContent(url);
        // 提取出相对路径
        String relUrl = url.replaceAll("/\\w+-\\w+-\\w+-\\w+-\\w+\\.m3u8.*", "");

        // 正则提取出的为相对路径, 需与前面的relUrl完成拼接
        Matcher matcher = Pattern.compile("EXTINF:\\d+\\.\\d+,(.+?)#").matcher(content);

        while (matcher.find()){
            System.out.println(matcher.group(1));
            list.add(relUrl + "/" + matcher.group(1));
        }
        return list;
    }
}
