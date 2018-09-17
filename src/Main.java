import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String url_1 = "https://lens.zhihu.com/api/videos/1024143280014860288";
        String url_2 = "https://vdn.vzuu.com/Act-ss-m3u8-hd/4fd6633884584a64b1928ec227eab88e/cbd51892-b857-11e8-8ab2-0242ac112a18None-00001.ts?auth_key=1537185501-0-0-0673e36c483f793ab7f9eb130942a5ad";


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
