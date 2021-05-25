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
        try{
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            User user = new User();
            user.setName(username);
            user.setPwd(password);
            UserDAO userDAO = new UserDAO();
            userDAO.save(user);

//            BusinessServiceImpl service = new BusinessServiceImpl();
//            service.registerUser(user);
            request.setAttribute("message", "注册成功");
            request.setAttribute("user", user);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
//            request.getRequestDispatcher("/message.jsp").forward(request, response);//这里要跳转到首页，并且显示欢迎您，，，待修改

        }catch(Exception e){
            e.printStackTrace();
            request.setAttribute("message", "注册失败");
//            request.getRequestDispatcher("/message.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }

}
