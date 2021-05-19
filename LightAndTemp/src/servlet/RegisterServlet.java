package servlet;
import dao.UserDAO;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String url;
        String msg;
        User user = null;
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (username == null || username.equals("")) {
                url = "/register.jsp";
                msg = "请输入用户名";
            } else if (password == null || password.equals("")) {
                url = "/register.jsp";
                msg = "请输入密码";
            } else {
                user = new User();
                user.setName(username);
                user.setPwd(password);
                UserDAO userDAO = new UserDAO();
                userDAO.save(user);
                msg = "注册成功";
                if ("student".equals(user.getRole())) {
                    url = "/index.jsp";
                } else if ("dormitory".equals(user.getRole())) {
                    url = "/index.jsp";
                }else{
                    url = "/index.jsp";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "注册失败";
            url = "/register.jsp";
        }
        request.setAttribute("msg", msg);
        if (user != null)
            request.setAttribute("user", user);
        request.getRequestDispatcher(url).forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }

}
