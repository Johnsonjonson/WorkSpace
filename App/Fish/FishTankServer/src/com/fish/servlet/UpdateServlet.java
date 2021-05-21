package com.fish.servlet;

import com.fish.data.XmlParseUtil;
import com.google.gson.Gson;
import com.fish.data.FishTankData;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
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
        double temp;  // 温度
        String resp = "0";
        try {
//            status = Integer.parseInt(request.getParameter("change"));
            temp = Double.parseDouble(request.getParameter("temp"));
            boolean needChange = FishTankData.getNeedChange();
            double curtemp = FishTankData.getCurTemp();
            resp = needChange?"1":"0";
            if (curtemp != temp){ // 当前温度不等于传过来的温度
                FishTankData.setCurTemp(temp);
                XmlParseUtil.writeXml(new FishTankData(needChange, temp));
                FishWebSocket.sendMessage(temp,needChange);
            }
            param.put("code","200");
        }catch(Exception e){
            param.put("errorcode","500");
            param.put("errormsg","参数类型错误，请输入正确的请求参数");
            resp = param.toString();
        }
//        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(resp);
//        out.flush();
//        out.close();
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
