package com;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class HttpRespDemo extends HttpServlet {

    private static final long serialVersionUID = 5956951482201650484L ;
    private static Log log = LogFactory.getLog(HttpRespDemo.class);
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] buffer = new byte[64 * 1024];
        InputStream in = request.getInputStream();
        String retCode = "0000";
        String retDesc = "操作成功";
        int length = in.read(buffer);
        if (length <= 0) {
            log.error("请求报文为空，系统无法处理！");
        } else {
            String encode = request.getCharacterEncoding();
            if (StringUtils.isBlank(encode)) {
                encode = "UTF-8";
            }
            byte[] data = new byte[length];
            System.arraycopy(buffer, 0, data, 0, length);
            String context = new String(data, encode);
            if (log.isDebugEnabled()) {
                log.debug("接收到的报文为" + context);
            }

            try {
                new JsonParser().parse(context);
                JSONObject req = JSONObject.fromObject(context);
                JSONObject dataJson = req.getJSONObject("data");
                System.out.println(dataJson);

            } catch (JsonParseException e) {
                retDesc ="请求报文不是json格式！";
                retCode = "-2";
                log.info(retDesc);
            }catch (Exception e) {
                retCode = "9999";
                retDesc = "系统异常";
                log.error(e);
            }
        }
        JSONObject result = new JSONObject();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        result.put("resultCode", retCode);
        result.put("resultMessage",retDesc);
        response.getWriter().println(result);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response)throws ServletException, IOException {
        doGet(request,response);
    }

    public static void main(String[] args) throws Exception {
//        String[] cmds = new String[]{"http_publish_resp_test"};
//        HttpFrameWork.main(cmds);

    }

}
