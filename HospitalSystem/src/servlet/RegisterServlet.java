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
            String phone = request.getParameter("phone");
            String cellphone = request.getParameter("cellphone");
            String role = request.getParameter("role");
            String address = request.getParameter("address");

            User user = new User();
            user.setAddress(address);
            user.setName(username);
            user.setPwd(password);
            user.setTel(phone);
            user.setRole(role);
            user.setId(1);
            UserDAO userDAO = new UserDAO();
            userDAO.save(user);

//            BusinessServiceImpl service = new BusinessServiceImpl();
//            service.registerUser(user);
            request.setAttribute("message", "注册成功");
            request.setAttribute("user", user);
            if ("customer".equals(user.getRole())){
                request.getRequestDispatcher("/patientDetail.jsp").forward(request, response);

            }else if ("doctor".equals(user.getRole())){
                CustomerServlet customerServlet = new CustomerServlet();
                customerServlet.searchAll(request, response);
//                request.getRequestDispatcher("/patientDetail.jsp").forward(request, response);
            }else{

            }
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
