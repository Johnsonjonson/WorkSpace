package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import entity.User;


/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String url = null;
		String msg = null;
		User user = null;
		String realcode = request.getSession(true).getAttribute("realcode").toString();
		String inputcode = request.getParameter("checkcode");
		if (realcode.equalsIgnoreCase(inputcode)) {
			UserDAO userDAO = new UserDAO();
			if(request.getParameter("name").equals("admin")){
				if (request.getParameter("pwd").equals("admin")){
					user= new User();
					user.setName("admin");
					user.setPwd("admin");
					user.setRole("admin");
					UserServlet userServlet = new UserServlet();
					userServlet.doPost(request,response);
				}else{
					msg = "密码错误";
				}
			}else {
				try {
					user = userDAO.getByName(request.getParameter("name"));
					if (user == null) {
						url = "/index.jsp";
						msg = "用户名不存在";
					} else if (!user.getPwd().equals(request.getParameter("pwd"))) {
						url = "/index.jsp";
						msg = "密码错误";
					} else {
						request.getSession(true).setAttribute("user", user);
						System.out.println("登陆玩家角色  " + user.getRole());
						if (user.getRole().equals("doctor")) {
							url = "/customersearch.jsp";
							request.setAttribute("m", "search");
							CustomerServlet customerServlet = new CustomerServlet();
							customerServlet.searchAll(request, response);
						} else if (user.getRole().equals("customer")) {
							url = "/patientDetail.jsp";
						}
						msg = "登录成功";
					}
				} catch (Exception e) {
					url = "/index.jsp";
					msg = e.toString();
				}
			}
		} else {
			url = "/index.jsp";
			msg = "验证码输入有误";
		}
		if (user != null) {
			user.setId(1);
		}
		request.setAttribute("msg", msg);
		request.setAttribute("user", user);
//		request.getRequestDispatcher("/patientDetail.jsp").forward(request, response);
		request.getRequestDispatcher(url).forward(request, response);
	}

}
