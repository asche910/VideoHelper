import website.Bilibili;

public class Main {
    public static void main(String[] args) {
        String playUrl = "https://www.bilibili.com/bangumi/play/ss11410";
        String outputdir = "/home/as_/IdeaProjects/VideoHelper/";

        try {
            // new Zhihu().downloadByUrl(playUrl, outputdir);

            new Bilibili().downloadByUrl(playUrl, outputdir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
