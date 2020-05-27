package com.johnson.servlet;

import com.google.gson.Gson;
import com.johnson.data.JDBCUtil;
import com.johnson.data.LightData;
import com.johnson.utils.PythonSocketUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "PythonServlet",urlPatterns = "/python")
public class PythonServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PythonSocketUtil.startSocket();
        Connection con = JDBCUtil.getConnection();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Boolean status = LightData.getStatus();
        String statusMsg = "路灯";
        if(!status){
            statusMsg = "故障";
        }
        Map<String, String> result = new HashMap<String,String>();
        result.put("status", String.valueOf(status));
        result.put("msg",statusMsg);
        String resultStr = new Gson().toJson(result);
        String jsonStr = resultStr;
        PrintWriter out = response.getWriter();
        out.println(jsonStr);
        out.flush();
        out.close();
    }
}
