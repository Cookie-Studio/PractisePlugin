package me.liuli.pra.utils;

import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import com.google.gson.GsonBuilder;
import me.liuli.pra.PractisePlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class OtherUtil {
    public static String y2j(File file) {
        Config yamlConfig = new Config(file, Config.YAML);
        ConfigSection section = yamlConfig.getRootSection();
        return new GsonBuilder().create().toJson(section);
    }

    public static void writeFile(String path, String text) {
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8));
            writer.write(text);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTextFromInputStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }

        byteArrayOutputStream.close();
        inputStream.close();

        return byteArrayOutputStream.toString("UTF-8");
    }

    public static long getTime() {
        return (System.currentTimeMillis() / 1000);
    }

    public static String getTextFromResource(String resourceName) {
        InputStream inputStream = PractisePlugin.class.getClassLoader().getResourceAsStream(resourceName);
        try {
            return getTextFromInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float angle(Vector3 from, Vector3 other) {
        double dot = from.dot(other) / (from.length() * other.length());
        return (float) Math.acos(dot);
    }

    public static void downloadFile(String urlStr, String filePath, String fileName) throws IOException {
        PractisePlugin.plugin.getLogger().info("DOWNLOADING " + fileName + " FROM URL: " + urlStr);

        long startTime = System.currentTimeMillis();
        File jar = new File(filePath, fileName);
        if (jar.exists()) {
            return;
        }
        File tmp = new File(jar.getPath() + ".tmp");
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3 * 1000);
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36");
        InputStream is = conn.getInputStream();
        int totalSize = conn.getContentLength(), nowSize = 0, lastSize = -1;
        FileOutputStream os = new FileOutputStream(tmp);
        byte[] buf = new byte[4096];
        int size = 0;
        while ((size = is.read(buf)) != -1) {
            os.write(buf, 0, size);
            nowSize += size;
            int progcess = 100 * nowSize / totalSize;
            if (progcess % 5 == 0 && progcess != lastSize) {
                PractisePlugin.plugin.getLogger().info("DOWNLOADING " + fileName + " PROCESS:" + (100 * nowSize / totalSize) + "%");
                lastSize = progcess;
            }
        }
        is.close();
        os.flush();
        os.close();
        if (jar.exists())
            jar.delete();
        tmp.renameTo(jar);
        PractisePlugin.plugin.getLogger().info("DOWNLOAD " + fileName + " COMPLETE(" + ((System.currentTimeMillis() - startTime) / 1000) + "s)");
    }

    public static void copyFile(File orifile, File tofile) throws IOException {
        FileInputStream infile = new FileInputStream(orifile);
        FileOutputStream outfile = new FileOutputStream(tofile);
        FileChannel inc = infile.getChannel();
        FileChannel outc = outfile.getChannel();
        inc.transferTo(0, inc.size(), outc);
        inc.close();
        outc.close();
        infile.close();
        outfile.close();
    }

    public static void copyDir(String oridir, String todir) throws IOException {
        new File(todir).mkdirs();
        File[] flist = new File(oridir).listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isFile()) {
                copyFile(flist[i], new File(new File(todir).getPath() + "/" + flist[i].getName()));
            } else {
                String faps = flist[i].getPath();
                new File(new File(todir).getPath() + "/" + faps.substring(new File(oridir).getPath().length()) + "/").mkdirs();
                copyDir(faps, new File(todir).getPath() + "/" + faps.substring(new File(oridir).getPath().length()) + "/");
            }
        }
    }

    public static void delDir(String dir) throws IOException {
        File[] flist = new File(dir).listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isFile()) {
                flist[i].delete();
            } else {
                delDir(flist[i].getPath());
            }
        }
        new File(dir).delete();
    }

    public static double randDouble(double max, double min) {
        return Math.random() * (max - min) + min;
    }
}
