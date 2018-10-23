import website.Bilibili;

import java.io.File;

import static util.PrintUtil.println;

public class Main {
    public static void main(String[] args) {

        String playUrl = "https://www.bilibili.com/video/av29446057/?spm_id_from=333.334.bili_music.5";
        String outputdir = "/home/as_/IdeaProjects/VideoHelper/";

        println(args.length);

        if (args.length == 1) {
            playUrl = args[0];
        }else if (args.length == 2){
            playUrl = args[0];
            outputdir = args[1];

            File file = new File(outputdir);
            if (!file.isDirectory()){
                println("请输入有效的输出目录！");
                return;
            }
        }

        try {
            // new Zhihu().downloadByUrl(playUrl, outputdir);

            println(playUrl + "..." + outputdir);

            new Bilibili().downloadByUrl(playUrl, outputdir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
