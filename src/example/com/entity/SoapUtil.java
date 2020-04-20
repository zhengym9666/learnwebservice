package com.entity;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoapUtil {

    public static Map<String,Object> map = new HashMap<String,Object>();

    /**
     * 将xml字符串转为map,每个标签对应一个key-value
     * @param soap
     * @return
     * @throws DocumentException
     */
    public static   Map parse(String soap) throws DocumentException {
        Document doc = DocumentHelper.parseText(soap);//报文转成doc对象
        Element root = doc.getRootElement();//获取根元素，准备递归解析这个XML树
        getCode(root);
        return map;
    }


    public static void getCode(Element root){
        if(root.elements()!=null){
            List<Element> list = root.elements();//如果当前跟节点有子节点，找到子节点
            for(Element e:list){//遍历每个节点
                if(e.elements().size()>0){
                    getCode(e);//当前节点不为空的话，递归遍历子节点；
                }
                if(e.elements().size()==0){
                    map.put(e.getName(), e.getTextTrim());
                }//如果为叶子节点，那么直接把名字和值放入map
            }
        }
    }

    public static String buildSoap(String soapAction,Map<String,Object> xmlMap) {
        String xml = "";
        xml += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ser=\"http://service.vshop.zjjzfy.com/\">\n" +
                "   <soapenv:Header/>\n" +
                "   <soapenv:Body>\n" +
                "      <ser:" + soapAction + ">\n";
        int i = 0;
        for ( String key : xmlMap.keySet() ) {
            xml += "         <arg" + i + "><![CDATA[" + xmlMap.get( key ) + "]]></arg" + i + ">\n";
            i++;
        }
        xml += "      </ser:" + soapAction + ">\n" +
                "   </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        System.out.println(xml);
        return xml;
    }



}
