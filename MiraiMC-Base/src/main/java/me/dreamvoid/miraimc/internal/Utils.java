package me.dreamvoid.miraimc.internal;

import com.google.gson.JsonObject;
import me.dreamvoid.miraimc.MiraiMCConfig;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

public final class Utils {
    static {
        // 此处放置插件自检代码

        if(Boolean.getBoolean("MiraiMC.DeveloperMode")){
            developerMode = true;
            Logger.getLogger("MiraiMC Preload Checker").warning("MiraiMC 开发者模式已启用！");
            Logger.getLogger("MiraiMC Preload Checker").warning("除非你知道你正在做什么，否则请不要启用开发者模式。");
        } else developerMode = false;
    }

    private static boolean findProcess(String processName) {
        BufferedReader bufferedReader = null;
        try {
            Process proc = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq " + processName + "\"");
            bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(processName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception ignored) {}
            }
        }
    }

    public static boolean findClass(String className){
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static Logger logger;
    private static ClassLoader classLoader;
    private static final boolean developerMode;

    public static void setLogger(Logger logger){
        Utils.logger = logger;
    }
    
    public static Logger getLogger(){
        return logger;
    }

    public static void setClassLoader(ClassLoader classLoader) {
        Utils.classLoader = classLoader;
    }
    
    public static ClassLoader getClassLoader(){
        return classLoader;
    }

    public static boolean isDeveloperMode(){
        return developerMode;
    }

    /**
     * Http 相关实用类
     */
    public static final class Http {
        /**
         * 发送HTTP GET请求
         * @param url URL 链接
         * @return 远程服务器返回内容
         * @throws IOException 出现任何连接问题时抛出
         */
        public static String get(String url) throws IOException {
            URL obj = new URL(url.replace(" ", "%20"));
            StringBuilder sb = new StringBuilder();
            HttpURLConnection httpUrlConn = (HttpURLConnection) obj.openConnection();

            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 DreamVoid MiraiMC");
            httpUrlConn.setConnectTimeout(5000);
            httpUrlConn.setReadTimeout(10000);

            InputStream input = httpUrlConn.getInputStream();
            InputStreamReader read = new InputStreamReader(input, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(read);
            String data = br.readLine();
            while (data != null) {
                sb.append(data).append(System.lineSeparator());
                data = br.readLine();
            }
            br.close();
            read.close();
            input.close();
            httpUrlConn.disconnect();

            return sb.toString();
        }

        /**
         * 发送HTTP POST请求
         * @param json Gson对象
         * @param URL 链接
         * @return 远程服务器返回内容
         */
        public static String post(JsonObject json, String URL) throws IOException {
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(URL);
                post.setHeader("Content-Type", "application/json");
                post.addHeader("Authorization", "Basic YWRtaW46");
                StringEntity s = new StringEntity(json.toString(), StandardCharsets.UTF_8);
                s.setContentType(new BasicHeader(org.apache.http.protocol.HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(s);
                // 发送请求
                HttpResponse httpResponse = client.execute(post);
                // 获取响应输入流
                InputStream inStream = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inStream, StandardCharsets.UTF_8));
                StringBuilder strber = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null)
                    strber.append(line).append("\n");
                inStream.close();
                if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    logger.warning("Http request returned bad status code: " + httpResponse.getStatusLine().getStatusCode()+", reason: "+ httpResponse.getStatusLine().getReasonPhrase());
                }
                return strber.toString();
            }
        }
    }

    @NotNull
    public static File getMiraiDir(){
        return MiraiMCConfig.General.MiraiWorkingDir.equals("default") ? new File(MiraiMCConfig.PluginDir,"MiraiBot") : new File(MiraiMCConfig.General.MiraiWorkingDir);
    }
}
