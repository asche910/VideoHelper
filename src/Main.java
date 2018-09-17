import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 将下面的替换成对应的知乎视频url即可
        String url_1 = "https://lens.zhihu.com/api/videos/1024143280014860288";

        try {
            FileOutputStream outputStream = new FileOutputStream(new File("/home/as_/IdeaProjects/VideoHelper/video_1.mp4"));

            List<String> urls = HttpUtil.getSrcList(HttpUtil.getPlayUrl(url_1));

            for(String url: urls){
                HttpUtil.combineStream(HttpUtil.getInputStream(url), outputStream);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
