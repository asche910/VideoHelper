package util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 该类提供一些Http的一些基本操作
 *
 * @author As_
 * @date 2018-09-17 20:29:12
 * @github https://github.com/asche910
 */
@SuppressWarnings("Duplicates")
public class HttpUtil {

    /**
     * 读取输入流并写到输出流中,该函数用于合并分散的视频文件
     *
     * @param in
     * @param out
     * @throws IOException
     */
    public static void combineStream(InputStream in, OutputStream out) throws IOException {
        byte[] bytes = new byte[1024];
        int n;
        while ((n = in.read(bytes)) != -1) {
            out.write(bytes, 0, n);
        }
        in.close();
    }

    public static InputStream getInputStream(String url) throws IOException {
        return getInputStream(url, null);
    }

    public static InputStream getInputStream(String url, Map<String, List<String>> headerMap) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");

        if (headerMap != null) {
            Iterator<Map.Entry<String, List<String>>> iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> entry = iterator.next();
                List<String> values = entry.getValue();
                for (String value : values)
                    connection.setRequestProperty(entry.getKey(), value);
            }
        }
        return connection.getInputStream();
    }

    public static String getResponseContent(String url) throws IOException {

        return getResponseContent(url, null);
    }

    public static String getResponseContent(String url, Map<String, List<String>> headerMap) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");

        if (headerMap != null) {
            Iterator<Map.Entry<String, List<String>>> iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, List<String>> entry = iterator.next();
                List<String> values = entry.getValue();
                for (String value : values)
                    connection.setRequestProperty(entry.getKey(), value);
            }
        }

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        inputStream.close();
        return stringBuilder.toString();
    }
}