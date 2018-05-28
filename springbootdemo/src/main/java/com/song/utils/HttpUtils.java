package com.song.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Http连接工具类
 * Created by linlei on 2018/5/24
 */
public class HttpUtils {
    /**引入日志*/
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);
    /**
     * JDK自带的http访问方式
     * @param url
     * @return
     */
    public static String easyGet(URL url){
        String msg ="";
        try{
            //1.得到访问地址的URL
            //URL url = new URL("http://localhost:8090/user/show?name=%E6%9D%8E%E5%9B%9B");
            //2.得到网络访问对象java.net.HttpURLConnection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            /*3.设置请求参数（过期时间,输入、输出流、访问方式）,以流的形式进行连接*/
            //设置是否向HttpURLConnection输出
            connection.setDoOutput(false);
            //设置是否向HttpURLConnction读入
            connection.setDoInput(true);
            //设置请求方式
            connection.setRequestMethod("GET");
            //设置是否使用缓存
            connection.setUseCaches(true);
            //设置此HttpURLConnection 实例是否应该自动执行Http重定向
            connection.setInstanceFollowRedirects(true);
            //设置超时时间
            connection.setConnectTimeout(3000);
            //连接
            connection.connect();
            //4.得到响应状态码的返回值 responseCode
            int code = connection.getResponseCode();
            //5.如果返回值正常，数据在网络中是以流的形式得到服务端返回的数据
            if(code == 200){//正常响应
                //从流中读取响应信息
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                while((line = reader.readLine())!=null){ //循环从流中读取
                    msg += line+"\n";
                }
                reader.close();//关闭流
            }
            //6.断开连接，释放资源
            connection.disconnect();

            //显示响应结果
            System.out.print(msg);
        }catch(IOException e){
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * JDK自带的Http访问方式
     * @param url
     * @param params
     * @return
     */
    public static String easyPost(URL url,String params){
        String msg = "";
        try {
            // 1. 获取访问地址URL
            //URL url = new URL("http://localhost:8080/Servlet/do_login.do");
            // 2. 创建HttpURLConnection对象
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            /* 3. 设置请求参数等 */
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            connection.setConnectTimeout(3000);
            // 设置是否输出
            connection.setDoOutput(true);
            // 设置是否读入
            connection.setDoInput(true);
            // 设置是否使用缓存
            connection.setUseCaches(false);
            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            connection.setInstanceFollowRedirects(true);
            // 设置使用标准编码格式编码参数的名-值对
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 连接
            connection.connect();
            /* 4. 处理输入输出 */
            // 写入参数到请求中
            //String params = "username=test&password=123456";
            OutputStream out = connection.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();
            // 从连接中读取响应信息
            int code = connection.getResponseCode();
            if (code == 200) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line;

                while ((line = reader.readLine()) != null) {
                    msg += line + "\n";
                }
                reader.close();
            }
            // 5. 断开连接
            connection.disconnect();

            // 处理结果
//            System.out.println(msg);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

    /**
     * apache项目httpclient
     * @param uri
     * @return
     */
    public static String clientGet(String uri){
        String msg = "";
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpGet对象
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = null;
        try {
            // 3. 执行GET请求
            response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine());
            // 4. 获取响应实体
            HttpEntity entity = response.getEntity();
            // 5. 处理响应实体
            if (entity != null) {
                msg = EntityUtils.toString(entity);
                log.info("长度：" + entity.getContentLength());
                log.info("内容：" + msg);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 6. 释放资源
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

    /**
     * apache项目httpclient
     * @param uri
     * @param params
     * @return
     */
    public static String clientPost(String uri,List<NameValuePair> params){
        String msg = "";
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpPost post = new HttpPost(uri);
        // 3. 设置POST请求传递参数
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
            post.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 4. 执行请求并处理响应
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null){
                msg = EntityUtils.toString(entity);
                log.info("长度：" + entity.getContentLength());
                log.info("内容：" + msg);
            }
            response.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return msg;
    }
}
