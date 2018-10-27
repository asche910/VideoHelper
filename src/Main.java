import website.BaseSite;

import java.io.File;

import static util.PrintUtil.println;

public class Main {
    private static String playUrl = "https://www.bilibili.com/bangumi/play/ss25733/";
    private static String outputDir;

    public static void main(String[] args) {

        handleArgs(args);

        try {
            BaseSite baseSite = UrlRecognition.reconize(playUrl, outputDir);

            baseSite.downloadByUrl(playUrl, outputDir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleArgs(String[] args){

        if (args.length == 1) {
            playUrl = args[0];

            outputDir = System.getProperty("user.dir");
            println("OutputDir: " + outputDir);
        }else if (args.length == 2){
            playUrl = args[0];
            outputDir = args[1];

            println("OutputDir: " + args[1]);

            File file = new File(outputDir);
            if (!file.isDirectory()){
                println("请输入有效的输出目录！");
                return;
            }
        }else {
            outputDir = System.getProperty("user.dir");
            println("OutputDir: " + outputDir);
        }
    }

    public static String getOutputDir(){
        if (outputDir == null || outputDir.equals("")){
            outputDir = System.getProperty("user.dir");
        }
        return outputDir;
    }
}

