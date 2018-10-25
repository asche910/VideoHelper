package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.PrintUtil.println;

public class DownloadUtil {

    public static void downloadVideo(String url, String fileDir) throws IOException {
        downloadVideo(url, fileDir, null);
    }

    @SuppressWarnings("Duplicates")
    public static void downloadVideo(String url, String fileDir, Map<String, List<String>> headerMap) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        connection.setRequestProperty("Connection", "keep-alive");

        if (headerMap != null) {
            Iterator<Map.Entry<String, List<String>>> iterator = headerMap.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, List<String>> entry = iterator.next();
                List<String> values = entry.getValue();
                for (String value: values)
                    connection.setRequestProperty(entry.getKey(), value);
            }
        }

        println(fileDir);

        File file = new File(fileDir);

        // 文件存在则重命名
        while (file.exists()){
            Matcher matcher = Pattern.compile("\\.\\w+$").matcher(fileDir);
            while (matcher.find()){
                int index = matcher.start();

                String startStr = fileDir.substring(0, index);
                String format = fileDir.substring(index);

                if (startStr.matches(".*\\(\\d+\\)$")){
                    int indexStart = startStr.lastIndexOf('(');
                    int indexEnd = startStr.lastIndexOf(')');

                    int num = Integer.parseInt(startStr.substring(indexStart + 1, indexEnd));
                    startStr = startStr.substring(0, indexStart) + "(" + ++num + ")";
                }else {
                    startStr += "(1)";
                }
                fileDir = startStr + format;
            }
            file = new File(fileDir);
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        InputStream inputStream = connection.getInputStream();

        System.out.println("Start downloading...");

        byte[] bytes = new byte[1024];
        int n;
        int curSize = 0, count = 0;
        while ((n = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, n);
            curSize += n;

            if (count++ % 500 == 0) {
                System.out.println(String.format("Downloading... %.2f M", curSize / 1024.00 / 1024.00));
            }
        }
        outputStream.close();
        inputStream.close();

        int code = connection.getResponseCode();
        Map map = connection.getHeaderFields();
        // println(code + "\n" + map);

        println("Download finished!");
    }

}
