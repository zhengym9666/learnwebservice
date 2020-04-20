package com;


import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService
public class HelloWorld {

  public String sayHelloWorldFrom(String from) {
    String result = "Hello, world, from " + from;
    System.out.println(result);

    return result;
  }


  public Integer add(int a,int b) {
      return a+b;
  }

  public static void main(String[] args) {
    Endpoint.publish("http://127.0.0.1:8088/helloservice",new HelloWorld());
  }
}
