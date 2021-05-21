package servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import entity.User;
import utils.DateUtil;


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
		UserDAO userDAO = new UserDAO();
		try {
			user = userDAO.getByName(request.getParameter("name"));
//			System.out.println("   ==== +"+ user.getIsSwipe());
			if (user == null) {
				url = "/index.jsp";
				msg = "用户名不存在";
			} else if (!user.getPwd().equals(request.getParameter("pwd"))) {
				url = "/index.jsp";
				msg = "密码错误";
			}else {  //已经刷卡

				request.getSession(true).setAttribute("user", user);
//				System.out.println("登陆玩家角色  " + user.getRole());
				url = "/detailInfo.jsp";
//						request.setAttribute("m", "search");
//						UserServlet uerServlet = new UserServlet();
//						uerServlet.doGet(request, response);
				msg = "登录成功";

			}
		} catch (Exception e) {
			url = "/index.jsp";
			msg = e.toString();
		}
		request.setAttribute("msg", msg);
		request.setAttribute("user", user);
		request.getRequestDispatcher(url).forward(request, response);
	}

}
