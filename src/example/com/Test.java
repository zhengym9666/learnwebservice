package com;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Test {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        System.out.println(list.isEmpty());


        JSONObject json = new JSONObject();
        json.put("name",new JSONObject());
        JSONObject name = json.getJSONObject("name");
        System.out.println(name==null);

    }

}
