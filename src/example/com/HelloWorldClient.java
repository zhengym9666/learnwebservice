package com;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.StringUtils;

public class HelloWorldClient {

    public static void main(String[] args) {
        // 指定调用WebService的URL（这里是我们发布后点击HelloWorld）
        String url = "http://localhost:8080/services/HelloWorld?wsdl";
        //调用的方法
        String method = "sayHelloWorldFrom";
        //调用方法的参数列表
        Object[] parms = new Object[]{"zhengym"};
        HelloWorldClient helloWorldClient = new HelloWorldClient();
        //调用方法
        String svrAddResult = helloWorldClient.CallMethod(url, method, parms);

        System.out.println(svrAddResult);
    }

    //实现WebService上发布的服务调用
    public String CallMethod(String url, String method, Object[] args) {
        String result = null;

        if(StringUtils.isEmpty(url)) {
            return "url地址为空";
        }

        Call rpcCall = null;

        try {
            //实例websevice调用实例
            Service webService = new Service();
            rpcCall = (Call) webService.createCall();
            rpcCall.setTargetEndpointAddress(new java.net.URL(url));
            rpcCall.setOperationName(method);

            //执行webservice方法

            result = (String) rpcCall.invoke(args);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

}
