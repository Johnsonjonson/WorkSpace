package com.johnson.servlet;

import com.google.gson.Gson;
import com.johnson.data.LightData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "UpdateServlet",urlPatterns = "/update")
public class UpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        http://129.204.232.210:8080/unnamed/update?status=1
            doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> param = getAllRequestParam(request);
        System.out.println(param);
        int status = 0;
        try {
            status = Integer.parseInt(request.getParameter("status"));
            LightData.setStatus(status == 1);
            String statusMsg = "路灯";
            if(!(status == 1)){
                statusMsg = "故障";
            }
            Map<String, String> result = new HashMap<String,String>();
            result.put("status", String.valueOf(status == 1));
            result.put("msg",statusMsg);
            String resultStr = new Gson().toJson(result);
            //群发消息
            for (PythonWebSocket item : PythonWebSocket.webSocketSet) {
                try {
                    item.sendMessage(resultStr);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
            param.put("code","200");
        }catch(NumberFormatException e){
            param.put("errorcode","500");
            param.put("errormsg","参数类型错误，请输入正确的请求参数");
        }
        System.out.println(status);
        response.setCharacterEncoding("UTF-8");
        String jsonStr = param.toString();
        PrintWriter out = response.getWriter();
        out.println(jsonStr);
        out.flush();
        out.close();
    }

    private Map<String, String> getAllRequestParam(final HttpServletRequest request) {
        Map<String, String> res = new HashMap<String, String>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
            }
        }
        return res;
    }

}
