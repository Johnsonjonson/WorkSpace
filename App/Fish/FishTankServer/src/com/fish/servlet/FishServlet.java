package com.fish.servlet;

import com.google.gson.Gson;
import com.fish.data.FishTankData;

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

@WebServlet(name = "PythonServlet",urlPatterns = "/change")
public class FishServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> param = getAllRequestParam(request);
        System.out.println(param);
        boolean needChange  = Boolean.parseBoolean(request.getParameter("change"));
        double curtemp = FishTankData.getCurTemp();
        FishTankData.setNeedChange(needChange);
        String statusMsg = "";
        if(needChange){
            statusMsg = "正在换水";
        }
        Map<String, Object> result = new HashMap<String,Object>();
        result.put("status", needChange);
        result.put("temp", curtemp);
        result.put("msg",statusMsg);
        String resultStr = new Gson().toJson(result);
        String jsonStr = resultStr;
        PrintWriter out = response.getWriter();
        out.print(jsonStr);
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
