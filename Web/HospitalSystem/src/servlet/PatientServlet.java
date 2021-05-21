package servlet;

import com.google.gson.Gson;
import dao.UserDAO;
import dao.UserDataDao;
import entity.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet("/PatientServlet")
public class PatientServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sql = "select * from t_userdata order by time desc LIMIT 10";

        String username = request.getParameter("username");
        int id = Integer.parseInt(request.getParameter("id"));
        username=new String(username.getBytes("ISO-8859-1"),"utf-8");
        System.out.println("username = " +  username  + "   " + id);
        List<Product> list = new ArrayList<Product>();
        List<BloodBean> bloodBeanlist = new ArrayList<BloodBean>();
        List<HeartBean> heartBeanlist = new ArrayList<HeartBean>();
        List<HealthDataBean> healthBeanlist = new ArrayList<HealthDataBean>();
        UserDataDao userDAO = new UserDataDao();
        UserData userData  = null;
        try {
//            userData = userDAO.getDataByName(username);
            userData = userDAO.getDataByID(id);
            ArrayList<Integer> bloodList = userData.getBloodList();
            ArrayList<Integer> heartList = userData.getHeartList();
            ArrayList<String> timeList = userData.getTimeList();
            for (int i = 0; i < bloodList.size(); i++) {
                bloodBeanlist.add(new BloodBean(timeList.get(i),bloodList.get(i)));
                heartBeanlist.add(new HeartBean(timeList.get(i),heartList.get(i)));
                healthBeanlist.add(new HealthDataBean(timeList.get(i),bloodList.get(i),heartList.get(i)));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("这里是doPost");

        Gson gson2 = new Gson();
        String json = gson2.toJson(healthBeanlist);

        System.out.println(json);

        // 将json字符串数据返回给前端
        response.setContentType("text/html; charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
