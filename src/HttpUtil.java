import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 该类完成视频下载的一些基本操作
 * @author As_
 * @date 2018-09-17 20:29:12
 * @github https://github.com/apknet
 */
public class HttpUtil {

    /**
     * 由api提取出最高清晰度的url
     * @param url
     * @return play_url
     */
    public static String getPlayUrl(String url) throws IOException {

        String json = getContent(url);

        JSONObject jsonObject = new JSONObject(json).getJSONObject("playlist");

        if(jsonObject.has("hd")){
            return jsonObject.getJSONObject("hd").getString("play_url");
        }else if(jsonObject.has("sd")){
            return jsonObject.getJSONObject("sd").getString("play_url");
        }else if(jsonObject.has("ld")){
            return jsonObject.getJSONObject("ld").getString("play_url");
        }
        return null;
    }

    /**
     * 解析出播放清单文件内分散的多个js格式视频url
     * @param url
     * @return
     */
    public static List<String> getSrcList(String url) throws IOException {
        List<String> list = new ArrayList<>();
        String content = getContent(url);
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

    /**
     * 读取输入流并写到输出流中,该函数用于合并分散的视频文件
     * @param in
     * @param out
     * @throws IOException
     */
    public static void combineStream(InputStream in, OutputStream out) throws IOException {
        byte[] bytes = new byte[1024];
        int n;
        while ((n = in.read(bytes)) != -1){
            out.write(bytes, 0, n);
        }
        in.close();
    }

    public static InputStream getInputStream(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
        return connection.getInputStream();
    }

    private static String getContent(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null){
            stringBuilder.append(line);
        }
        reader.close();
        inputStream.close();
        return stringBuilder.toString();
    }
}
