package website;

import bean.BilibiliBean;
import bean.VideoBean;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.DownloadUtil;
import util.HttpUtil;
import util.MD5Encoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static util.PrintUtil.println;


/**
 * 哔哩哔哩: https://www.bilibili.com/
 *
 * @author Asche
 * @date 2018-10-20 18:02:29
 * @github https://github.com/asche910
 */
public class Bilibili extends BaseSite {
    // from aid to cids
    private String ApiGetList = "https://www.bilibili.com/widget/getPageList?aid=";
    private String AvApi = "http://interface.bilibili.com/v2/playurl?";
    private String EpApi = "http://bangumi.bilibili.com/player/web_api/playurl?";
    private String SEC_1 = "94aba54af9065f71de72f5508f1cd42e";
    private String SEC_2 = "9b288147e5474dd2aa67085f716c560d";

    // quality
    private final int RESOLUTION_1080 = 112;
    private final int RESOLUTION_720 = 64;
    private final int RESOLUTION_480 = 32;
    private final int RESOLUTION_360 = 15;

    private int quality = RESOLUTION_1080;

//    private List<String> urls = new ArrayList<>();
    private String playUrl;
    private String fileName;
    private int timeLength;
    private int fileSize = 0;
    private int aid;
    private int cid;

    // 视频类型
    private final int AV_VIDEO = 1;
    private final int EP_VIDEO = 2;
    private final int SS_VIDEO = 3;

    private int type = AV_VIDEO;
    private boolean isSupported;

    // ep的关联系列
    private List<BilibiliBean> serialList = new ArrayList<>();

    // 是否已经解析
    private boolean isResolved;


    public Bilibili() {
    }

    /**
     * 先获取信息再决定是否下载
     * @param playUrl
     * @param outputDir
     */
    public Bilibili(String playUrl, String outputDir) {
        if (!isResolved) {
            this.playUrl = playUrl;

            String[] strs = playUrl.split("/");

            for (String str : strs) {
                if (str.matches("av\\d{4,}")) {
                    aid = Integer.parseInt(str.substring(2));
                    isSupported = true;
                    break;
                } else if(str.matches("ep\\d{4,}")){
                    type = EP_VIDEO;
                    isSupported = true;
                    break;
                } else if(str.matches("ss\\d{4,}")){
                    type = SS_VIDEO;
                    isSupported = true;
                    break;
                }
            }

            try {
                switch (type) {
                    case SS_VIDEO:
                    case EP_VIDEO:
                        initEp();

                        String epApi = generateEpApi(EpApi, cid, quality);
                        println(epApi);

                        parseEpApiResponse(epApi);
                        break;
                    case AV_VIDEO:
                        initAv();

                        String avApi = generateAvApi(AvApi, cid, quality);
                        println(avApi);

                        parseAvApiResponse(avApi);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            isResolved = true;
        }
    }

    @Override
    public void downloadByUrl(String playUrl, String outputDir) {
        System.out.println("Bilibili start: ");

        this.playUrl = playUrl;
        String[] strs = playUrl.split("/");

        for (String str : strs) {
            if (str.matches("av\\d{4,}")) {
                aid = Integer.parseInt(str.substring(2));
                isSupported = true;
                break;
            } else if(str.matches("ep\\d{4,}")){
                type = EP_VIDEO;
                isSupported = true;
                break;
            } else if(str.matches("ss\\d{4,}")){
                type = SS_VIDEO;
                isSupported = true;
                break;
            }
        }

        try {

            if (!isResolved) {
                switch (type) {
                    case SS_VIDEO:
                    case EP_VIDEO:
                        initEp();

                        String epApi = generateEpApi(EpApi, cid, quality);
                        println(epApi);

                        parseEpApiResponse(epApi);
                        break;
                    case AV_VIDEO:
                        initAv();

                        String avApi = generateAvApi(AvApi, cid, quality);
                        println(avApi);

                        parseAvApiResponse(avApi);
                        break;
                }
                isResolved = true;
            }

            println("# Title: " + fileName);
            println("     -TimeLength: " + timeLength / 1000 / 60 + ":" + String.format("%02d", timeLength / 1000 % 60));
            println("     -File Size: " + fileSize / 1024 / 1024 + " M");

            download(urls, outputDir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 内部下载入口
     *
     * @param videoSrcs
     * @param outputDir
     */
    @Override
    public void download(List<String> videoSrcs, String outputDir) throws IOException {
        Map<String, List<String>> headerMap = new HashMap<>();
        // 缺失Referer会导致453错误
        headerMap.put("Referer", Collections.singletonList("http://interface.bilibili.com/v2/playurl?appkey=84956560bc028eb7&cid=59389212&otype=json&qn=3&quality=3&type=&sign=4c841d687bb7e479e3111428c6a4d3b8"));

        int index = 0;

        for (String src : videoSrcs) {
            println("Download: " + ++index + "/" + videoSrcs.size());

            String fileDir;
            if (videoSrcs.size() == 1) {

                fileDir = outputDir + File.separatorChar + fileName.replaceAll("[/|\\\\]", "") + ".flv";
            } else {
                fileDir = outputDir + File.separatorChar + fileName.replaceAll("[/|\\\\]", "") + "【" + index + "】.flv";
            }

            DownloadUtil.downloadVideo(src, fileDir, headerMap);
        }
        println("Download: All Done!");
    }

    @Override
    public VideoBean getInfo() {
        VideoBean bean = new VideoBean();
        bean.setTitle(fileName);
        bean.setTimeLength(timeLength / 1000 / 60 + ":" + String.format("%02d", timeLength / 1000 % 60));
        bean.setSize(fileSize / 1024 / 1024);
        return bean;
    }

    /**
     * cid, fileName
     *
     * @throws IOException
     */
    private void initAv() throws IOException {
        String result = HttpUtil.getResponseContent(ApiGetList + aid);
        JSONObject jb = (JSONObject) new JSONArray(result).get(0);
        cid = jb.getInt("cid");

        Document doc = Jsoup.connect(playUrl).get();

        Element ele = doc.selectFirst("div[id=viewbox_report]").selectFirst("h1");
        if (ele.hasAttr("title"))
            fileName = ele.attr("title");

    }

    /**
     * cid, fileName and related eps
     *
     * @throws IOException
     */
    private void initEp() throws IOException {
        Document doc = Jsoup.connect(playUrl).get();
        Element ele = doc.body().child(2);

        String preResult = ele.toString();
        // println(preResult);

        String result = preResult.substring(preResult.indexOf("__=") + 3, preResult.indexOf(";(function()"));
        // println(result);

        JSONObject object = new JSONObject(result);

        JSONObject curEpInfo = object.getJSONObject("epInfo");

        fileName = object.getJSONObject("mediaInfo").getString("title");

        cid = curEpInfo.getInt("cid");


        JSONArray ja = object.getJSONArray("epList");

        for (Object obj : ja) {
            JSONObject epObject = (JSONObject) obj;

            int aid = epObject.getInt("aid");
            int cid = epObject.getInt("cid");
            int duration = epObject.getInt("duration");
            int epId = epObject.getInt("ep_id");

            String index = epObject.getString("index");
            String indexTitle = epObject.getString("index_title");

            BilibiliBean bean = new BilibiliBean(aid, cid, duration, epId, index, indexTitle);

            serialList.add(bean);

            println(bean.toString());
        }
    }

    /**
     * timeLength, fileSize, urls
     *
     * @param avReqApi
     * @throws IOException
     */
    private void parseAvApiResponse(String avReqApi) throws IOException {
        String result = HttpUtil.getResponseContent(avReqApi);

        // println(result);

        JSONObject jsonObject = new JSONObject(result);
        timeLength = jsonObject.getInt("timelength");

        JSONArray ja = jsonObject.getJSONArray("durl");

        Iterator<Object> iterator = ja.iterator();
        while (iterator.hasNext()) {
            JSONObject jb = (JSONObject) iterator.next();

            String videoSrc = jb.getString("url");
            urls.add(videoSrc);

            fileSize += jb.getInt("size");
        }
    }

    /**
     * timeLength, fileSize, urls
     *
     * @param epReqApi
     * @throws IOException
     * @throws DocumentException
     */
    private void parseEpApiResponse(String epReqApi) throws IOException, DocumentException {
        String response = HttpUtil.getResponseContent(epReqApi);

        SAXReader reader = new SAXReader();
        org.dom4j.Element rootElement = reader.read(new ByteArrayInputStream(response.getBytes("utf-8"))).getRootElement();

        timeLength = Integer.parseInt(rootElement.element("timelength").getText().trim());

        List<org.dom4j.Element> elements = rootElement.elements("durl");

        for (org.dom4j.Element ele : elements) {
            int curSize = Integer.parseInt(ele.element("size").getText());
            fileSize += curSize;

            String url = ele.element("url").getText();
            urls.add(url);
        }

        println(fileName + ": " + fileSize / 1024 / 1024 + "M");
    }

    /**
     * 生成av类型视频下载信息的api请求链接
     *
     * @param url
     * @param cid
     * @param quality
     * @return
     */
    private String generateAvApi(String url, int cid, int quality) {
        String paramStr = String.format("appkey=84956560bc028eb7&cid=%d&otype=json&qn=%d&quality=%d&type=", cid, quality, quality);
        try {
            String checkSum = MD5Encoder.md5(paramStr + SEC_1).toLowerCase();
            return url + paramStr + "&sign=" + checkSum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成ep类型视频下载信息的api请求链接
     *
     * @param url
     * @param cid
     * @param quality
     * @return
     */
    private String generateEpApi(String url, int cid, int quality) {
        String paramStr = String.format("cid=%d&module=bangumi&player=1&quality=%d&ts=%s",
                cid, quality, System.currentTimeMillis() / 1000 + "");
        try {
            String checkSum = MD5Encoder.md5(paramStr + SEC_2).toLowerCase();
            return url + paramStr + "&sign=" + checkSum;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
