package website;

import java.io.IOException;
import java.util.List;

abstract public class BaseSite {
    abstract public void downloadByUrl(String playUrl, String outputDir);
    abstract void download(List<String> videoSrcs, String outputDir) throws IOException;
}
