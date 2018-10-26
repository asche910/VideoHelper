import website.BaseSite;
import website.Bilibili;
import website.Diyishipin;
import website.Zhihu;

public class UrlRecognition {

    private static String url;

    public static BaseSite reconize(String url, String outputDir){

        UrlRecognition.url = url;

        if (contains(Site.Bilibili))
            return new Bilibili(url, outputDir);
        if (contains(Site.Zhihu))
            return new Zhihu(url, outputDir);
        if (contains(Site.Diyishipin))
            return new Diyishipin(url, outputDir);

        return null;
    }

    private static boolean contains(String[] array){
        for(String keyword: array)
            if (url.contains(keyword))
                return true;
        return false;
    }
}
