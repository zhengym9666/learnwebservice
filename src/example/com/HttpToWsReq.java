package com;


import com.entity.SoapUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class HttpToWsReq {

    static int socketTimeout = 30000;// 请求超时时间
    static int connectTimeout = 30000;// 传输超时时间
    static Logger logger = Logger.getLogger(HttpToWsReq.class);

    public final static String POST_URL = "http://localhost:8080/services/HelloWorld";

    /**
     * 使用SOAP发送消息（HttpClient方式）
     *
     * @param postUrl
     * @param xmlMap
     * @param soapAction
     * @return
     */
    public static String doPostSoap(String postUrl, Map<String,Object> xmlMap,
                                    String soapAction) {
        String retStr = "";
        String xml = "";
        // 创建HttpClientBuilder
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // HttpClient
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
        HttpPost httpPost = new HttpPost(postUrl);
        //  设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout( socketTimeout )
                .setConnectTimeout( connectTimeout ).build();
        httpPost.setConfig(requestConfig);
        try {
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8" );
            httpPost.setHeader("SOAPAction", soapAction );

            xml = SoapUtil.buildSoap(soapAction,xmlMap);

            StringEntity data = new StringEntity( xml, Charset.forName("UTF-8") );
            httpPost.setEntity(data);
            CloseableHttpResponse response = closeableHttpClient
                    .execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                // 打印响应内容
                retStr = EntityUtils.toString(httpEntity, "UTF-8");
            }
            // 释放资源
            closeableHttpClient.close();
        } catch (Exception e) {
            System.out.println("exception in doPostSoap1_1" + e);
        }

//        retStr = retStr.substring( retStr.indexOf("<return>") + 8, retStr.indexOf("</return>"));
        return retStr;

    }

    /**
     * 使用HttpURLConnection方式连接
     * @param soapurl
     * @param xmlMap
     * @return
     * @throws IOException
     */
    public static String urlConnectionUtil(String soapAction,String soapurl,Map<String,Object> xmlMap) throws IOException {
        //第一步：创建服务地址
        //http://172.25.37.31:8080/PeopleSoftService/getPerson.wsdl
        URL url = new URL(soapurl);
        //第二步：打开一个通向服务地址的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //第三步：设置参数
        //3.1发送方式设置：POST必须大写
        connection.setRequestMethod("POST");
        //3.2设置数据格式：content-type
        connection.setRequestProperty("content-type", "text/xml;charset=utf-8");
        //3.3设置输入输出，因为默认新创建的connection没有读写权限，
        connection.setDoInput(true);
        connection.setDoOutput(true);
        //第四步：组织SOAP数据，发送请求
        String xml = SoapUtil.buildSoap(soapAction,xmlMap);
        //将信息以流的方式发送出去
        OutputStream os = connection.getOutputStream();
        os.write(xml.getBytes());
        StringBuilder sb = new StringBuilder();
        //第五步：接收服务端响应，打印
        int responseCode = connection.getResponseCode();
        if(200 == responseCode){//表示服务端响应成功
            //获取当前连接请求返回的数据流
            InputStream is = connection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

//            StringBuilder sb = new StringBuilder();
            String temp = null;
            while(null != (temp = br.readLine())){
                sb.append(temp);
            }
            is.close();
            isr.close();
            br.close();
        }
        os.close();
        return sb.toString();
    }

    public static String URLConnection() {
        URL wsUrl = null;
        try {

            wsUrl = new URL("http://127.0.0.1:8088/helloservice");
            HttpURLConnection conn = (HttpURLConnection) wsUrl.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");

            OutputStream os = conn.getOutputStream();

            //请求体
            String soap = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:q0=\"http://service.webservice.wang.com/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                    + "<soapenv:Body><q0:sayHelloWorldFrom><arg0>tom</arg0></q0:sayHelloWorldFrom></soapenv:Body></soapenv:Envelope>";

            os.write(soap.getBytes());

            InputStream is = conn.getInputStream();

            byte[] b = new byte[1024];
            int len = 0;
            String s = "";
            while( (len = is.read(b))!=-1 ){

                String ss = new String(b,0,len,"UTF-8");
                s += ss;
            }

            //返回的是拦截中的返回体；
            System.out.println(s);

            is.close();
            os.close();
            conn.disconnect();

            return s;

        } catch (MalformedURLException e) {
            System.out.println("创建URL失败");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("URL打开失败");
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws DocumentException, IOException {
        StringBuilder sb = new StringBuilder();
        Map<String,Object> xmlMap = new HashMap<String, Object>();
        xmlMap.put("from","zhengym");

        //httpClient
        /*String result = HttpToWsReq.doPostSoap(POST_URL,
                xmlMap,"sayHelloWorldFrom");*/

        //HttpURLConnection
       /* String result = HttpToWsReq.urlConnectionUtil("sayHelloWorldFrom",POST_URL,xmlMap);
        Map<String,Object> resultMap = SoapUtil.parse(result);
        System.out.println(resultMap.get("sayHelloWorldFromReturn"));*/
       System.out.println(HttpToWsReq.URLConnection());

    }


}
