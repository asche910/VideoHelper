import website.Bilibili;
import website.Zhihu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import static util.PrintUtil.println;

public class Main {
    public static void main(String[] args) {

        String playUrl = "https://www.bilibili.com/video/av34350976/";
        String outputdir = "/home/as_/IdeaProjects/VideoHelper/";


        if (args.length == 1) {
            playUrl = args[0];

//            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//            outputdir = path.substring(0, path.lastIndexOf(File.separatorChar));

            outputdir = System.getProperty("user.dir");
            println("OutputDir: " + outputdir);
        }else if (args.length == 2){
            playUrl = args[0];
            outputdir = args[1];

            println("OutputDir: " + args[1]);

            File file = new File(outputdir);
            if (!file.isDirectory()){    
                println("请输入有效的输出目录！");
                return;
            }
        }

        try {
//             new Zhihu().downloadByUrl(playUrl, outputdir);

            new Bilibili().downloadByUrl(playUrl, outputdir);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

